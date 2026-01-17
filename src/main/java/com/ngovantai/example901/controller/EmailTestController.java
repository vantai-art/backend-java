package com.ngovantai.example901.controller;

import com.ngovantai.example901.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ✅ CONTROLLER TEST EMAIL
 * 
 * Để test xem email có gửi được không
 * 
 * Endpoint: GET /api/test-email?email=YOUR_EMAIL@gmail.com
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmailTestController {

    private final EmailService emailService;

    /**
     * Test gửi email
     * 
     * Example: GET http://localhost:8080/api/test-email?email=test@gmail.com
     */
    @GetMapping("/test-email")
    public ResponseEntity<?> testEmail(@RequestParam String email) {
        try {
            System.out.println("📧 [TEST] Sending test email to: " + email);

            // Gửi email test
            String testToken = "TEST-TOKEN-123456";
            emailService.sendPasswordResetEmail(email, testToken);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "✅ Email đã được gửi thành công đến: " + email,
                    "token", testToken,
                    "note", "Kiểm tra inbox hoặc spam folder"));

        } catch (Exception e) {
            System.err.println("❌ [TEST] Failed to send email: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "❌ Không thể gửi email",
                    "error", e.getMessage(),
                    "help", "Kiểm tra lại cấu hình email trong application.properties"));
        }
    }

    /**
     * Kiểm tra cấu hình email hiện tại
     * 
     * Example: GET http://localhost:8080/api/email-config
     */
    @GetMapping("/email-config")
    public ResponseEntity<?> getEmailConfig(
            @org.springframework.beans.factory.annotation.Value("${spring.mail.username}") String username,
            @org.springframework.beans.factory.annotation.Value("${spring.mail.host}") String host,
            @org.springframework.beans.factory.annotation.Value("${spring.mail.port}") int port) {
        return ResponseEntity.ok(Map.of(
                "host", host,
                "port", port,
                "username", username,
                "status", "configured",
                "note", "Password is hidden for security"));
    }
}