package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.UserDto;
import com.example.hyuk_blog.entity.User;
import com.example.hyuk_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // 비밀번호 해싱
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    // 사용자 로그인 인증 (admin 계정도 포함)
    public Optional<UserDto> authenticate(String username, String password) {
        // User 테이블에서 사용자 찾기 (마이그레이션으로 admin 계정도 포함됨)
        Optional<UserDto> user = userRepository.findByUsernameAndActiveTrue(username)
                .filter(u -> {
                    // admin 계정은 해시되지 않은 비밀번호, 일반 사용자는 해시된 비밀번호
                    return u.getPassword().equals(hashPassword(password)) || u.getPassword().equals(password);
                })
                .map(UserDto::fromEntity);
        
        return user;
    }
    
    // 사용자명으로 사용자 조회
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserDto::fromEntity);
    }
    
    // 회원가입
    @Transactional
    public UserDto register(String username, String password, String nickname, String email) {
        // 중복 검사
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }
        if (email != null && !email.isEmpty() && userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPassword(password));
        user.setNickname(nickname);
        user.setEmail(email);
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        return UserDto.fromEntity(savedUser);
    }
    
    // 사용자명 존재 여부 확인
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    // 닉네임 존재 여부 확인
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
    
    // 이메일 존재 여부 확인
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
} 