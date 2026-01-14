package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.entity.PasswordResetToken;
import com.ngovantai.example901.entity.User;
import com.ngovantai.example901.repository.PasswordResetTokenRepository;
import com.ngovantai.example901.repository.UserRepository;
import com.ngovantai.example901.service.AuthService;
import com.ngovantai.example901.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // ✅ Inject EmailService

    @Override
    @Transactional
    public String createPasswordResetToken(String username, String email) {
        // ✅ Tìm user theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("❌ Tên đăng nhập không tồn tại"));

        // ✅ Kiểm tra email có khớp không
        if (!email.equalsIgnoreCase(user.getEmail())) {
            throw new RuntimeException("❌ Tên đăng nhập và email không khớp");
        }

        // ✅ Xóa token cũ (nếu có)
        tokenRepository.deleteByUser(user);

        // ✅ Tạo token mới (UUID)
        String token = UUID.randomUUID().toString();

        // ✅ Lưu token vào DB
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        System.out.println("🔑 [RESET TOKEN] Created for: " + username + " | Email: " + email);

        // ✅ GỬI EMAIL
        emailService.sendPasswordResetEmail(email, token);

        return token;
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // ✅ Tìm token
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("❌ Token không hợp lệ"));

        // ✅ Check token đã dùng chưa
        if (resetToken.getUsed()) {
            throw new RuntimeException("❌ Token đã được sử dụng");
        }

        // ✅ Check token hết hạn chưa
        if (resetToken.isExpired()) {
            throw new RuntimeException("❌ Token đã hết hạn");
        }

        // ✅ Đổi mật khẩu
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // ✅ Đánh dấu token đã dùng
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        System.out.println("✅ [RESET PASSWORD] Success for user: " + user.getUsername());
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        // ✅ Tìm user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("❌ User không tồn tại"));

        // ✅ Check mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("❌ Mật khẩu cũ không đúng");
        }

        // ✅ Đổi mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        System.out.println("✅ [CHANGE PASSWORD] Success for user: " + username);
    }
}