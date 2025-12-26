package com.ngovantai.example901.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Token không được để trống")
    private String token;

    @NotBlank(message = "Password mới không được để trống")
    @Size(min = 6, message = "Password phải ít nhất 6 ký tự")
    private String newPassword;
}