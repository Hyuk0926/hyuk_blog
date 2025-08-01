package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.JwtLoginRequestDto;
import com.example.hyuk_blog.dto.JwtResponseDto;
import com.example.hyuk_blog.service.JwtAuthService;
import com.example.hyuk_blog.repository.AdminRepository;
import com.example.hyuk_blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JwtAuthController {

    private final JwtAuthService jwtAuthService;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    /**
     * 관리자 JWT 로그인
     */
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody JwtLoginRequestDto loginRequest) {
        try {
            JwtResponseDto response = jwtAuthService.adminLogin(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("Admin login error: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * 사용자 JWT 로그인
     */
    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody JwtLoginRequestDto loginRequest) {
        try {
            JwtResponseDto response = jwtAuthService.userLogin(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("User login error: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * JWT 토큰 검증
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            boolean isValid = jwtAuthService.validateToken(token);
            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);
            
            if (isValid) {
                response.put("username", jwtAuthService.getUsernameFromToken(token));
                response.put("role", jwtAuthService.getRoleFromToken(token));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token validation failed");
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * JWT 토큰 정보 조회
     */
    @GetMapping("/info")
    public ResponseEntity<?> getTokenInfo(@RequestHeader("Authorization") String token) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("username", jwtAuthService.getUsernameFromToken(token));
            response.put("role", jwtAuthService.getRoleFromToken(token));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token info error: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get token info");
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 데이터베이스 계정 정보 확인 (테스트용)
     */
    @GetMapping("/debug/accounts")
    public ResponseEntity<?> getAccountInfo() {
        try {
            Map<String, Object> response = new HashMap<>();
            
            // 관리자 계정 정보
            List<Map<String, Object>> admins = new ArrayList<>();
            adminRepository.findAll().forEach(admin -> {
                Map<String, Object> adminInfo = new HashMap<>();
                adminInfo.put("username", admin.getUsername());
                adminInfo.put("password", admin.getPassword());
                adminInfo.put("active", admin.isActive());
                admins.add(adminInfo);
            });
            response.put("admins", admins);
            
            // 사용자 계정 정보
            List<Map<String, Object>> users = new ArrayList<>();
            userRepository.findAll().forEach(user -> {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("username", user.getUsername());
                userInfo.put("password", user.getPassword());
                userInfo.put("active", user.isActive());
                users.add(userInfo);
            });
            response.put("users", users);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Account info error: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get account info");
            return ResponseEntity.internalServerError().body(error);
        }
    }
} 