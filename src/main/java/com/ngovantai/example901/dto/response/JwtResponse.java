package com.ngovantai.example901.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private String type = "Bearer"; // Token type
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String role;

    // Constructor không có type (backward compatible)
    public JwtResponse(String token, Long id, String username, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.role = role;
    }
}