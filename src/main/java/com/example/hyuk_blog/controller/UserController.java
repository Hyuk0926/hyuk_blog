package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.UserDto;
import com.example.hyuk_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }
    
    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       HttpSession session, 
                       Model model,
                       @RequestParam(required = false) String redirectUrl) {
        
        Optional<UserDto> user = userService.authenticate(username, password);
        
        if (user.isPresent()) {
            // 로그인 성공 - 기존 admin 세션 제거
            session.removeAttribute("admin");
            session.setAttribute("user", user.get());
            
            // 리다이렉트 URL이 있으면 해당 페이지로, 없으면 메인 페이지로
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            } else {
                return "redirect:/";
            }
        } else {
            // 로그인 실패
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "user/login";
        }
    }
    
    // 회원가입 페이지
    @GetMapping("/register")
    public String registerForm() {
        return "user/register";
    }
    
    // 회원가입 처리
    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          @RequestParam String nickname,
                          @RequestParam(required = false) String email,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        
        // 비밀번호 확인
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "user/register";
        }
        
        try {
            userService.register(username, password, nickname, email);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/user/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user/register";
        }
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
    
    // 사용자명 중복 확인 (AJAX)
    @PostMapping("/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam String username) {
        return !userService.existsByUsername(username);
    }
    
    // 닉네임 중복 확인 (AJAX)
    @PostMapping("/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        return !userService.existsByNickname(nickname);
    }
    
    // 이메일 중복 확인 (AJAX)
    @PostMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        if (email == null || email.isEmpty()) {
            return true; // 이메일은 선택사항이므로 빈 값은 허용
        }
        return !userService.existsByEmail(email);
    }
} 