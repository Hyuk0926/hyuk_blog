package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 사용자명으로 사용자 조회
    Optional<User> findByUsername(String username);
    
    // 사용자명과 활성 상태로 사용자 조회
    Optional<User> findByUsernameAndActiveTrue(String username);
    
    // 사용자명 존재 여부 확인
    boolean existsByUsername(String username);
    
    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);
    
    // 닉네임 존재 여부 확인
    boolean existsByNickname(String nickname);
} 