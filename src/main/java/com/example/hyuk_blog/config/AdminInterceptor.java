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
        
        // 세션이 없으면 로그인 페이지로 리다이렉트
        if (session == null) {
            response.sendRedirect("/admin/login");
            return false;
        }
        
        // admin 세션이나 user 세션 중 하나라도 있으면 접근 허용
        if (session.getAttribute("admin") != null || session.getAttribute("user") != null) {
            return true;
        }
        
        // 둘 다 없으면 로그인 페이지로 리다이렉트
        response.sendRedirect("/admin/login");
        return false;
    }
} 