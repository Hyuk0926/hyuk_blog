package com.example.hyuk_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (REST API 사용)
            .authorizeHttpRequests(authorize -> authorize
                // 댓글 조회(GET) API는 누구나 접근 가능하도록 허용
                .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll() 
                
                // 좋아요 조회/수정(POST) API는 인증된 사용자만 가능
                .requestMatchers("/api/like/**").authenticated()

                // 공개 페이지들
                .requestMatchers("/", "/index", "/jp", "/about", "/projects", "/contact", "/resume", "/resume/ko", "/search").permitAll()
                .requestMatchers("/post/**").permitAll()
                
                // 정적 리소스들
                .requestMatchers("/css/**", "/js/**", "/img/**", "/svg/**", "/images/**").permitAll()
                
                // 로그인/회원가입 페이지 및 처리
                .requestMatchers("/user/login", "/user/register", "/user/logout").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/login", "/user/register").permitAll()
                
                // 관리자 로그인 페이지
                .requestMatchers("/admin/login").permitAll()
                
                // API 엔드포인트들 (일부는 공개)
                .requestMatchers("/api/posts/**", "/api/search/**", "/visitor/**").permitAll()

                // 나머지 모든 요청은 인증이 필요함
                .anyRequest().authenticated() 
            )
            .formLogin(AbstractHttpConfigurer::disable) // Spring Security 기본 로그인 폼 비활성화
            .logout(AbstractHttpConfigurer::disable); // Spring Security 기본 로그아웃 비활성화

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 