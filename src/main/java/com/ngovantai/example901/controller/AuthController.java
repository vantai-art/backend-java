package com.ngovantai.example901.controller;

import com.ngovantai.example901.dto.request.*;
import com.ngovantai.example901.dto.response.JwtResponse;
import com.ngovantai.example901.dto.response.MessageResponse;
import com.ngovantai.example901.entity.User;
import com.ngovantai.example901.repository.UserRepository;
import com.ngovantai.example901.security.JwtUtils;
import com.ngovantai.example901.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    // ==================== ĐĂNG KÝ ====================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        // ✅ Check username đã tồn tại
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("❌ Username đã tồn tại"));
        }

        // ✅ Check email đã tồn tại
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("❌ Email đã được sử dụng"));
        }

        // ✅ Tạo user với role (mặc định là USER)
        User.Role userRole = request.getRole() != null ? request.getRole() : User.Role.USER;

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(userRole)
                .isActive(true)
                .build();

        User saved = userRepository.save(user);

        System.out.println("✅ [SIGNUP] Created user: " + saved.getUsername() + " | Role: " + saved.getRole());

        return ResponseEntity.ok(new MessageResponse("✅ Đăng ký thành công!"));
    }

    // ==================== ĐĂNG NHẬP ====================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseGet(() -> userRepository.findByEmail(request.getUsername())
                        .orElseThrow(() -> new RuntimeException("❌ Tên đăng nhập/Email không tồn tại")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("❌ Mật khẩu không đúng"));
        }

        if (!user.getIsActive()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("❌ Tài khoản đã bị khóa"));
        }

        String role = user.getRole().name();
        String token = jwtUtils.generateToken(user.getUsername(), role);

        System.out.println(
                "🔓 [LOGIN] User: " + user.getUsername() + " | Email: " + user.getEmail() + " | Role: " + role);

        return ResponseEntity.ok(JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(role)
                .build());
    }

    // ==================== QUÊN MẬT KHẨU ====================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            String token = authService.createPasswordResetToken(request.getEmail());

            // TODO: Gửi email với link reset
            String resetLink = "http://localhost:3000/auth/reset-password?token=" + token;
            System.out.println("🔗 Reset Password Link: " + resetLink);

            return ResponseEntity.ok(new MessageResponse(
                    "✅ Link đặt lại mật khẩu đã được gửi đến email của bạn"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    // ==================== ĐẶT LẠI MẬT KHẨU ====================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new MessageResponse(
                    "✅ Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    // ==================== ĐỔI MẬT KHẨU (Đã đăng nhập) ====================
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(new MessageResponse("❌ Bạn chưa đăng nhập"));
        }

        try {
            String username = principal.getName();
            authService.changePassword(username, request.getOldPassword(), request.getNewPassword());

            return ResponseEntity.ok(new MessageResponse(
                    "✅ Đổi mật khẩu thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    // ==================== KIỂM TRA TOKEN ====================
    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestParam String token) {
        try {
            String username = jwtUtils.getUsernameFromJwt(token);
            return ResponseEntity.ok(new MessageResponse("✅ Token hợp lệ: " + username));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("❌ Token không hợp lệ"));
        }
    }

    // ==================== THÔNG TIN USER HIỆN TẠI ====================
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(new MessageResponse("❌ Bạn chưa đăng nhập"));
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("❌ User không tồn tại"));

        return ResponseEntity.ok(JwtResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build());
    }
}