package com.ngovantai.example901.service;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetToken);
}