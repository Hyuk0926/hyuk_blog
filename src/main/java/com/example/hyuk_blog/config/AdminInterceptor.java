package com.example.hyuk_blog.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        
        // 세션이 없거나 관리자 로그인 정보가 없으면 로그인 페이지로 리다이렉트
        if (session == null || session.getAttribute("admin") == null) {
            if (uri.startsWith("/admin_jp")) {
                response.sendRedirect("/admin/login"); // admin_jp도 동일 로그인 사용
            } else {
                response.sendRedirect("/admin/login");
            }
            return false;
        }
        
        return true;
    }
} 