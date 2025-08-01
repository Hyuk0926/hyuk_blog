package com.example.hyuk_blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtLoginRequestDto {
    private String username;
    private String password;
} 