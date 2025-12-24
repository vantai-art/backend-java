package com.ngovantai.example901.dto.request;

import com.ngovantai.example901.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private User.Role role;
}