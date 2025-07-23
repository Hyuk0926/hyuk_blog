package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.PostDto;
import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.service.PostService;
import com.example.hyuk_blog.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.hyuk_blog.dto.ResumeDto;

import java.util.List;
import java.util.Optional;
import com.example.hyuk_blog.dto.InquiryDto;
import com.example.hyuk_blog.service.InquiryService;


import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private InquiryService inquiryService;

    @GetMapping("/about")
    public String about(Model model, HttpServletRequest request) {
        Locale locale = request.getLocale();
        model.addAttribute("resume", resumeService.loadResume(locale));
        return "about";
    }
    
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        Locale locale = request.getLocale();
        List<PostDto> posts = postService.getAllPublishedPosts(locale);
        model.addAttribute("posts", posts);
        model.addAttribute("categories", Category.values());
        return "index";
    }

    @GetMapping("/post/{id}")
    public String postDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        Locale locale = request.getLocale();
        Optional<PostDto> post = postService.getPostById(id, locale);
        if (post.isPresent() && post.get().isPublished()) {
            model.addAttribute("post", post.get());
            return "post-detail";
        }
        return "redirect:/";
    }

    @GetMapping("/projects")
    public String projects() {
        return "projects";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String q, Model model, HttpServletRequest request) {
        Locale locale = request.getLocale();
        List<PostDto> posts = postService.searchPublishedPosts(q, locale);
        model.addAttribute("posts", posts);
        model.addAttribute("searchQuery", q);
        return "search";
    }

    @GetMapping("/resume")
    public String resume(Model model, HttpServletRequest request) {
        Locale locale = request.getLocale();
        model.addAttribute("resume", resumeService.loadResume(locale));
        return "resume";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String contactSubmit(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String subject,
                                @RequestParam String message,
                                Model model) {
        // 문의 저장
        InquiryDto inquiry = new InquiryDto();
        inquiry.setName(name);
        inquiry.setEmail(email);
        inquiry.setSubject(subject);
        inquiry.setMessage(message);
        inquiryService.addInquiry(inquiry);
        model.addAttribute("successMessage", "문의가 성공적으로 접수되었습니다. 빠른 시일 내에 답변드리겠습니다.");
        return "contact";
    }

    @GetMapping("/api/search")
    @ResponseBody
    public List<PostDto> searchApi(@RequestParam String q, HttpServletRequest request) {
        Locale locale = request.getLocale();
        return postService.searchPublishedPosts(q, locale);
    }

    @GetMapping("/api/posts")
    @ResponseBody
    public List<PostDto> getPostsByCategory(@RequestParam(required = false) Category category, HttpServletRequest request) {
        Locale locale = request.getLocale();
        if (category == null) {
            return postService.getAllPublishedPosts(locale);
        }
        return postService.getPublishedPostsByCategory(category, locale);
    }
}