package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            // ✅ Link reset password (có thể dùng cho web hoặc app)
            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("🔐 Dat lai mat khau - Food App");
            message.setText(
                    "Xin chao,\n\n" +
                            "Ban da yeu cau dat lai mat khau cho tai khoan cua minh.\n\n" +
                            "Ma xac thuc cua ban la: " + resetToken + "\n\n" +
                            "Hoac ban co the click vao link ben duoi de dat lai mat khau:\n" +
                            resetLink + "\n\n" +
                            "Ma nay se het han sau 15 phut.\n\n" +
                            "Neu ban khong yeu cau dat lai mat khau, vui long bo qua email nay.\n\n" +
                            "Tran trong,\n" +
                            "Food App Team");

            mailSender.send(message);

            System.out.println("✅ [EMAIL] Sent password reset email to: " + toEmail);
            System.out.println("🔑 [EMAIL] Reset token: " + resetToken);

        } catch (Exception e) {
            System.err.println("❌ [EMAIL] Failed to send email: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email. Vui lòng thử lại sau. Lỗi: " + e.getMessage());
        }
    }

    /**
     * ✅ GỬI EMAIL CHÀO MỪNG (OPTIONAL)
     */
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("🎉 Chao mung ban den voi Food App!");
            message.setText(
                    "Xin chao " + username + ",\n\n" +
                            "Chao mung ban da dang ky tai khoan thanh cong!\n\n" +
                            "Hay bat dau kham pha cac mon an ngon va dat hang ngay bay gio.\n\n" +
                            "Tran trong,\n" +
                            "Food App Team");

            mailSender.send(message);
            System.out.println("✅ [EMAIL] Sent welcome email to: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ [EMAIL] Failed to send welcome email: " + e.getMessage());
            // Không throw exception để không block quá trình đăng ký
        }
    }

    /**
     * ✅ GỬI EMAIL XÁC NHẬN ĐƠN HÀNG (OPTIONAL)
     */
    public void sendOrderConfirmationEmail(String toEmail, Long orderId, String orderDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("✅ Xac nhan don hang #" + orderId);
            message.setText(
                    "Xin chao,\n\n" +
                            "Don hang #" + orderId + " cua ban da duoc xac nhan.\n\n" +
                            "Chi tiet don hang:\n" +
                            orderDetails + "\n\n" +
                            "Cam on ban da su dung dich vu cua chung toi!\n\n" +
                            "Tran trong,\n" +
                            "Food App Team");

            mailSender.send(message);
            System.out.println("✅ [EMAIL] Sent order confirmation to: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ [EMAIL] Failed to send order confirmation: " + e.getMessage());
        }
    }
}