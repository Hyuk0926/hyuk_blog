package com.example.hyuk_blog.config;

import com.example.hyuk_blog.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 활성화
            )
            .securityContext(securityContext -> securityContext
                .requireExplicitSave(false) // 세션 자동 저장
            )
            .authorizeHttpRequests(authorize -> authorize
                // 정적 리소스 허용 (모든 정적 파일)
                .requestMatchers("/css/**", "/js/**", "/img/**", "/svg/**", "/cursor/**", "/favicon.ico").permitAll()
                // 메인 페이지 허용
                .requestMatchers("/", "/index", "/jp", "/home", "/about", "/contact", "/projects", "/search").permitAll()
                // 게시글 조회 허용
                .requestMatchers("/post/**", "/posts/**").permitAll()
                // 사용자 인증 관련 페이지 허용 (로그인, 회원가입, 로그아웃)
                .requestMatchers("/user/login", "/user/register", "/user/logout", "/user/check-username", "/user/check-nickname", "/user/check-email").permitAll()
                // 관리자 로그인 페이지는 모든 사용자 접근 가능
                .requestMatchers("/admin/login", "/admin").permitAll()
                // 관리자 관련 모든 페이지는 인증된 사용자만 접근 (admin 계정 포함)
                .requestMatchers("/admin/**", "/admin_jp/**", "/admin_kr/**").permitAll()
                // API 엔드포인트 설정
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/comments/**").permitAll()
                .requestMatchers("/api/like/**").permitAll()
                .requestMatchers("/api/posts/**").permitAll()
                .requestMatchers("/api/search/**").permitAll()
                .requestMatchers("/visitor/**").permitAll()
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )
            .userDetailsService(customUserDetailsService) // UserDetailsService 등록
            .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
            .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 