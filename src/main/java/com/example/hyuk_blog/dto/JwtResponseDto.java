package com.example.hyuk_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
    private String type = "Bearer";
    private String username;
    private String role;
    private String message;

    public JwtResponseDto(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public JwtResponseDto(String token, String username, String role, String message) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.message = message;
    }
} 