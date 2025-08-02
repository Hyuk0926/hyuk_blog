package com.example.hyuk_blog.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    
    private String username;
    private String password;
    private String nickname;
    private String email;
} 