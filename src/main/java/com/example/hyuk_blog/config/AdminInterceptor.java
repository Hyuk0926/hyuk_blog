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
        
        // 세션이 없거나 관리자 로그인 정보가 없으면 로그인 페이지로 리다이렉트
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect("/admin/login");
            return false;
        }
        
        return true;
    }
} 