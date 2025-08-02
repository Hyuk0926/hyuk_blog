package com.example.hyuk_blog.service;

import com.example.hyuk_blog.entity.User;
import com.example.hyuk_blog.entity.Admin;
import com.example.hyuk_blog.repository.UserRepository;
import com.example.hyuk_blog.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. User 테이블에서 사용자 찾기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return user;
        }
        
        // 2. User 테이블에 없으면 Admin 테이블에서 확인
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        // Admin 계정에 ADMIN 역할 부여
        return org.springframework.security.core.userdetails.User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!admin.isActive())
                .build();
    }
} 