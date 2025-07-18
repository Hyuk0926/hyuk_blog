package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.PostDto;
import com.example.hyuk_blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.hyuk_blog.dto.ResumeDto;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import com.example.hyuk_blog.dto.InquiryDto;
import com.example.hyuk_blog.service.InquiryService;

@Controller
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private AdminController adminController;

    @Autowired
    private InquiryService inquiryService;

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("resume", adminController.getResume());
        return "about";
    }
    
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<PostDto> posts = postService.getAllPublishedPosts();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/post/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        Optional<PostDto> post = postService.getPostById(id);
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
    public String search(@RequestParam String q, Model model) {
        List<PostDto> posts = postService.searchPublishedPosts(q);
        model.addAttribute("posts", posts);
        model.addAttribute("searchQuery", q);
        return "search";
    }

    @GetMapping("/resume")
    public String resume(Model model) {
        ResumeDto resume = new ResumeDto();
        model.addAttribute("resume", resume);
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

} 