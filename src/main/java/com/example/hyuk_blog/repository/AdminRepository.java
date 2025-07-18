package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    // 사용자명으로 관리자 조회
    Optional<Admin> findByUsername(String username);
    
    // 사용자명과 활성 상태로 관리자 조회
    Optional<Admin> findByUsernameAndActiveTrue(String username);
    
    // 사용자명 존재 여부 확인
    boolean existsByUsername(String username);
} 