package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.AdminDto;
import com.example.hyuk_blog.dto.PostDto;
import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.service.PostService;
import com.example.hyuk_blog.service.ResumeService;
import com.example.hyuk_blog.service.InquiryService;
import com.example.hyuk_blog.dto.InquiryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private ResumeService resumeService;

    @Autowired
    private InquiryService inquiryService;

    // 관리자 대시보드
    @GetMapping("")
    public String adminDashboard(Model model, HttpSession session) {
        String lang = (String) session.getAttribute("lang");
        List<PostDto> posts;
        if ("ja".equals(lang)) {
            posts = postService.getAllPosts().stream()
                .filter(post -> post.getTitleJa() != null && !post.getTitleJa().isBlank())
                .toList();
        } else {
            posts = postService.getAllPosts().stream()
                .filter(post -> post.getTitleKo() != null && !post.getTitleKo().isBlank())
                .toList();
        }
        model.addAttribute("posts", posts);
        model.addAttribute("inquiryCount", inquiryService.getUnreadCount());
        model.addAttribute("inquiries", inquiryService.getAllInquiries());
        return "admin/dashboard";
    }
    
    // 새 게시글 작성 폼
    @GetMapping("/post/new")
    public String newPostForm(Model model, HttpSession session) {
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        model.addAttribute("post", new PostDto());
        model.addAttribute("admin", admin);
        model.addAttribute("categories", Category.values());
        return "admin/post-form";
    }
    
    // 새 게시글 저장
    @PostMapping("/post/new")
    public String createPost(@ModelAttribute PostDto postDto) {
        postService.savePost(postDto);
        return "redirect:/admin";
    }
    
    // 게시글 수정 폼
    @GetMapping("/post/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model, HttpSession session) {
        Optional<PostDto> post = postService.getPostById(id);
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            model.addAttribute("admin", admin);
            model.addAttribute("categories", Category.values());
            return "admin/post-form";
        }
        return "redirect:/admin";
    }
    
    // 게시글 수정
    @PostMapping("/post/edit/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute PostDto postDto) {
        postService.updatePost(id, postDto);
        return "redirect:/admin";
    }
    
    // 게시글 삭제
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/admin";
    }
    
    // 게시글 미리보기
    @GetMapping("/post/preview/{id}")
    public String previewPost(@PathVariable Long id, Model model) {
        Optional<PostDto> post = postService.getPostById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "post-detail";
        }
        return "redirect:/admin";
    }

    // 이력서 관리 폼 (GET)
    @GetMapping("/resume")
    public String resumeForm(Model model, HttpSession session) {
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        model.addAttribute("admin", admin);
        model.addAttribute("resume", resumeService.loadResume());
        return "admin/resume-form";
    }

    // 이력서 저장 (POST)
    @PostMapping("/resume")
    public String saveResume(@ModelAttribute com.example.hyuk_blog.entity.Resume resume, RedirectAttributes redirectAttributes) {
        resumeService.saveResume(resume);
        redirectAttributes.addFlashAttribute("message", "이력서가 저장되었습니다!");
        return "redirect:/admin";
    }

    // 새 JP 게시글 작성 폼
    @GetMapping("/post/jp/new")
    public String newPostFormJp(Model model, HttpSession session) {
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        model.addAttribute("post", new PostDto());
        model.addAttribute("admin", admin);
        model.addAttribute("categories", Category.values());
        return "admin/jp/post-form";
    }

    // 새 JP 게시글 저장
    @PostMapping("/post/jp/new")
    public String createPostJp(@ModelAttribute PostDto postDto) {
        postService.savePost(postDto);
        return "redirect:/admin";
    }

    // JP 게시글 수정 폼
    @GetMapping("/post/jp/edit/{id}")
    public String editPostFormJp(@PathVariable Long id, Model model, HttpSession session) {
        Optional<PostDto> post = postService.getPostById(id);
        AdminDto admin = (AdminDto) session.getAttribute("admin");

        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            model.addAttribute("admin", admin);
            model.addAttribute("categories", Category.values());
            return "admin/jp/post-form";
        }
        return "redirect:/admin";
    }

    // JP 게시글 수정
    @PostMapping("/post/jp/edit/{id}")
    public String updatePostJp(@PathVariable Long id, @ModelAttribute PostDto postDto) {
        postService.updatePost(id, postDto);
        return "redirect:/admin";
    }

    // 이력서 조회 (외부에서 호출 가능)
    public com.example.hyuk_blog.entity.Resume getResume() {
        return resumeService.loadResume();
    }

    @GetMapping("/inquiry")
    public String inquiryManage(Model model) {
        model.addAttribute("inquiries", inquiryService.getAllInquiries());
        return "admin/inquiry";
    }

    @GetMapping("/admin/inquiries")
    @ResponseBody
    public List<InquiryDto> getInquiries() {
        return inquiryService.getAllInquiries();
    }

    @PostMapping("/admin/inquiries/read")
    @ResponseBody
    public void markInquiriesRead() {
        inquiryService.markAllRead();
    }

    @GetMapping("/admin/inquiries/recent")
    @ResponseBody
    public List<InquiryDto> getRecentInquiries() {
        return inquiryService.getRecentInquiries(5);
    }
} 