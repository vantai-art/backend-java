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
    private final EmailService emailService;

    /**
     * ✅ QUÊN MẬT KHẨU - GỬI EMAIL VỚI TOKEN
     * Input: username + email
     */
    @Override
    @Transactional
    public String createPasswordResetToken(String username, String email) {
        System.out.println("🔍 [RESET] Looking for user: " + username + " | Email: " + email);

        // ✅ Tìm user theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.err.println("❌ [RESET] Username not found: " + username);
                    return new RuntimeException("❌ Tên đăng nhập không tồn tại");
                });

        System.out.println("✅ [RESET] Found user: " + user.getUsername() + " | Email: " + user.getEmail());

        // ✅ Kiểm tra email có khớp không
        if (!email.equalsIgnoreCase(user.getEmail())) {
            System.err.println("❌ [RESET] Email mismatch. Expected: " + user.getEmail() + " | Got: " + email);
            throw new RuntimeException("❌ Tên đăng nhập và email không khớp");
        }

        // ✅ Xóa token cũ (nếu có)
        try {
            tokenRepository.deleteByUser(user);
            System.out.println("🗑️ [RESET] Deleted old tokens for user: " + username);
        } catch (Exception e) {
            System.err.println("⚠️ [RESET] Error deleting old tokens: " + e.getMessage());
        }

        // ✅ Tạo token mới (UUID)
        String token = UUID.randomUUID().toString();

        // ✅ Lưu token vào DB
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        System.out.println("💾 [RESET] Token saved to database");
        System.out.println("🔑 [RESET] Token: " + token);

        // ✅ GỬI EMAIL
        try {
            System.out.println("📧 [RESET] Sending email to: " + email);
            emailService.sendPasswordResetEmail(email, token);
            System.out.println("✅ [RESET] Email sent successfully");
        } catch (Exception e) {
            System.err.println("❌ [RESET] Failed to send email: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email. Vui lòng kiểm tra lại địa chỉ email hoặc thử lại sau.");
        }

        System.out.println("✅ [RESET] Process completed for: " + username);
        return token;
    }

    /**
     * ✅ ĐẶT LẠI MẬT KHẨU VỚI TOKEN
     */
    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        System.out.println("🔍 [RESET PASSWORD] Verifying token: " + token);

        // ✅ Tìm token
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    System.err.println("❌ [RESET PASSWORD] Token not found");
                    return new RuntimeException("❌ Token không hợp lệ hoặc đã hết hạn");
                });

        System.out.println("✅ [RESET PASSWORD] Token found for user: " + resetToken.getUser().getUsername());

        // ✅ Check token đã dùng chưa
        if (resetToken.getUsed()) {
            System.err.println("❌ [RESET PASSWORD] Token already used");
            throw new RuntimeException("❌ Token đã được sử dụng");
        }

        // ✅ Check token hết hạn chưa
        if (resetToken.isExpired()) {
            System.err.println("❌ [RESET PASSWORD] Token expired");
            throw new RuntimeException("❌ Token đã hết hạn. Vui lòng yêu cầu lại.");
        }

        // ✅ Đổi mật khẩu
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        System.out.println("🔒 [RESET PASSWORD] Password updated");

        // ✅ Đánh dấu token đã dùng
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        System.out.println("✅ [RESET PASSWORD] Success for user: " + user.getUsername());
    }

    /**
     * ✅ ĐỔI MẬT KHẨU (ĐÃ LOGIN)
     */
    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        System.out.println("🔍 [CHANGE PASSWORD] Request from: " + username);

        // ✅ Tìm user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.err.println("❌ [CHANGE PASSWORD] User not found");
                    return new RuntimeException("❌ User không tồn tại");
                });

        // ✅ Check mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            System.err.println("❌ [CHANGE PASSWORD] Old password incorrect");
            throw new RuntimeException("❌ Mật khẩu cũ không đúng");
        }

        // ✅ Đổi mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        System.out.println("✅ [CHANGE PASSWORD] Success for user: " + username);
    }
}