package com.ngovantai.example901.service;

public interface AuthService {

    String createPasswordResetToken(String username, String email);

    void resetPassword(String token, String newPassword);

    void changePassword(String username, String oldPassword, String newPassword);
}