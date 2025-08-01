package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.JwtLoginRequestDto;
import com.example.hyuk_blog.dto.JwtResponseDto;
import com.example.hyuk_blog.entity.Admin;
import com.example.hyuk_blog.entity.User;
import com.example.hyuk_blog.repository.AdminRepository;
import com.example.hyuk_blog.repository.UserRepository;
import com.example.hyuk_blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthService {

    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 관리자 JWT 로그인
     */
    public JwtResponseDto adminLogin(JwtLoginRequestDto loginRequest) {
        log.info("관리자 로그인 시도: {}", loginRequest.getUsername());
        
        Admin admin = adminRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> {
                    log.error("관리자 계정을 찾을 수 없음: {}", loginRequest.getUsername());
                    return new BadCredentialsException("Invalid username or password");
                });

        log.info("관리자 계정 찾음: {}", admin.getUsername());
        log.info("입력된 비밀번호: {}", loginRequest.getPassword());
        log.info("저장된 비밀번호 해시: {}", admin.getPassword());

        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            log.error("비밀번호 불일치: {}", loginRequest.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }

        log.info("관리자 로그인 성공: {}", admin.getUsername());
        String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN");
        return new JwtResponseDto(token, admin.getUsername(), "ADMIN", "관리자 로그인 성공");
    }

    /**
     * 사용자 JWT 로그인
     */
    public JwtResponseDto userLogin(JwtLoginRequestDto loginRequest) {
        log.info("사용자 로그인 시도: {}", loginRequest.getUsername());
        
        // 1. User 테이블에서 사용자 찾기
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        
        // 2. User 테이블에 없으면 Admin 테이블에서 확인
        if (user == null) {
            Admin admin = adminRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> {
                        log.error("사용자 계정을 찾을 수 없음: {}", loginRequest.getUsername());
                        return new BadCredentialsException("Invalid username or password");
                    });
            
            // Admin 계정 비밀번호 검증
            boolean adminPasswordValid = false;
            
            // BCrypt 검증
            if (passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
                adminPasswordValid = true;
            }
            // 평문 비교 (기존 admin 계정 호환성)
            else if (admin.getPassword().equals(loginRequest.getPassword())) {
                adminPasswordValid = true;
            }
            
            if (!adminPasswordValid) {
                log.error("Admin 비밀번호 불일치: {}", loginRequest.getUsername());
                throw new BadCredentialsException("Invalid username or password");
            }
            
            log.info("Admin 로그인 성공: {}", admin.getUsername());
            String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN");
            return new JwtResponseDto(token, admin.getUsername(), "ADMIN", "관리자 로그인 성공");
        }

        log.info("사용자 계정 찾음: {}", user.getUsername());
        log.info("입력된 비밀번호: {}", loginRequest.getPassword());
        log.info("저장된 비밀번호 해시: {}", user.getPassword());

        // 기존 사용자 계정들의 비밀번호 검증 (다양한 암호화 방식 지원)
        boolean passwordValid = false;
        
        // 1. BCrypt 검증 시도
        try {
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                passwordValid = true;
            }
        } catch (Exception e) {
            log.debug("BCrypt 검증 실패, 다른 방식 시도: {}", e.getMessage());
        }
        
        // 2. SHA-256 검증 (기존 사용자 계정들)
        if (!passwordValid && user.getPassword().length() == 64) { // SHA-256 해시 길이
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(loginRequest.getPassword().getBytes("UTF-8"));
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                String hashedInput = hexString.toString();
                log.debug("SHA-256 해시 비교: 입력={}, 저장={}", hashedInput, user.getPassword());
                if (user.getPassword().equals(hashedInput)) {
                    passwordValid = true;
                    log.info("SHA-256 비밀번호 검증 성공: {}", user.getUsername());
                }
            } catch (Exception e) {
                log.debug("SHA-256 검증 실패: {}", e.getMessage());
            }
        }
        
        // 3. BCrypt 검증 (기존 사용자 계정들)
        if (!passwordValid && user.getPassword().startsWith("$2a$")) { // BCrypt로 암호화된 경우
            try {
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    passwordValid = true;
                    log.info("BCrypt 비밀번호 검증 성공: {}", user.getUsername());
                }
            } catch (Exception e) {
                log.debug("BCrypt 검증 실패: {}", e.getMessage());
            }
        }
        
        // 3. 평문 비교 (임시, 보안상 위험)
        if (!passwordValid && user.getPassword().equals(loginRequest.getPassword())) {
            passwordValid = true;
            log.warn("평문 비밀번호 사용 중: {}", user.getUsername());
        }

        if (!passwordValid) {
            log.error("비밀번호 불일치: {}", loginRequest.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }

        log.info("사용자 로그인 성공: {}", user.getUsername());
        String token = jwtUtil.generateToken(user.getUsername(), "USER");
        return new JwtResponseDto(token, user.getUsername(), "USER", "사용자 로그인 성공");
    }

    /**
     * JWT 토큰 검증
     */
    public boolean validateToken(String token) {
        try {
            String cleanToken = jwtUtil.removeBearerPrefix(token);
            String username = jwtUtil.extractUsername(cleanToken);
            return jwtUtil.validateToken(cleanToken, username);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * JWT 토큰에서 사용자 정보 추출
     */
    public String getUsernameFromToken(String token) {
        try {
            String cleanToken = jwtUtil.removeBearerPrefix(token);
            return jwtUtil.extractUsername(cleanToken);
        } catch (Exception e) {
            log.error("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * JWT 토큰에서 역할 추출
     */
    public String getRoleFromToken(String token) {
        try {
            String cleanToken = jwtUtil.removeBearerPrefix(token);
            return jwtUtil.extractRole(cleanToken);
        } catch (Exception e) {
            log.error("Failed to extract role from token: {}", e.getMessage());
            return null;
        }
    }
} 