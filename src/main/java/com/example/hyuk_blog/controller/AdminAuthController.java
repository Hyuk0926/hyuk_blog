package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.AdminDto;
import com.example.hyuk_blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {
    
    @Autowired
    private AdminService adminService;
    
    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }
    
    // 로그인

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       HttpSession session, 
                       Model model) {
        
        Optional<AdminDto> admin = adminService.authenticate(username, password);
        
        if (admin.isPresent()) {
            // 로그인 성공
            session.setAttribute("admin", admin.get());
            // 계정에 따라 언어 세션 저장
            if ("admin_jp".equals(username)) {
                session.setAttribute("lang", "ja");
            } else {
                session.setAttribute("lang", "ko");
            }
            return "redirect:/admin";
        } else {
            // 로그인 실패
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "admin/login";
        }
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
} 