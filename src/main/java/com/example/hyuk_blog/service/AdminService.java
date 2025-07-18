package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.AdminDto;
import com.example.hyuk_blog.entity.Admin;
import com.example.hyuk_blog.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    // 관리자 로그인 인증
    public Optional<AdminDto> authenticate(String username, String password) {
        return adminRepository.findByUsernameAndActiveTrue(username)
                .filter(admin -> admin.getPassword().equals(password))
                .map(AdminDto::fromEntity);
    }
    
    // 사용자명으로 관리자 조회
    public Optional<AdminDto> findByUsername(String username) {
        return adminRepository.findByUsername(username)
                .map(AdminDto::fromEntity);
    }
    
    // 관리자 저장
    public AdminDto saveAdmin(AdminDto adminDto) {
        Admin admin = adminDto.toEntity();
        Admin savedAdmin = adminRepository.save(admin);
        return AdminDto.fromEntity(savedAdmin);
    }
    
    // 사용자명 존재 여부 확인
    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }
} 