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
@RequestMapping("/admin/jp")
public class AdminJpController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private ResumeService resumeService;

    @Autowired
    private InquiryService inquiryService;

    // 일본어 관리자 대시보드
    @GetMapping("")
    public String adminJpDashboard(Model model, HttpSession session) {
        // 세션에 lang을 일본어로 설정
        session.setAttribute("lang", "ja");
        
        String lang = "ja";
        List<PostDto> posts = postService.getAllPosts(lang);
        model.addAttribute("posts", posts);
        model.addAttribute("inquiryCount", inquiryService.getUnreadCount());
        model.addAttribute("inquiries", inquiryService.getAllInquiries());
        model.addAttribute("adminPrefix", "/admin/jp");
        model.addAttribute("lang", "ja");
        return "admin/dashboard";
    }
    
    // 새 일본어 게시글 작성 폼
    @GetMapping("/post/new")
    public String newJpPostForm(Model model, HttpSession session) {
        session.setAttribute("lang", "ja"); // 무조건 일본어
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        model.addAttribute("post", new PostDto());
        model.addAttribute("admin", admin);
        model.addAttribute("categories", Category.values());
        model.addAttribute("formAction", "/admin/jp/post/new");
        return "admin/post-form-jp";
    }
    
    // 새 일본어 게시글 저장
    @PostMapping("/post/new")
    public String createJpPost(@ModelAttribute PostDto postDto) {
        postService.savePost(postDto, "ja");
        return "redirect:/admin/jp";
    }
    
    // 일본어 게시글 수정 폼
    @GetMapping("/post/edit/{id}")
    public String editJpPostForm(@PathVariable Long id, Model model, HttpSession session) {
        Optional<PostDto> post = postService.getPostById(id, "ja");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            model.addAttribute("admin", admin);
            model.addAttribute("categories", Category.values());
            model.addAttribute("formAction", "/admin/jp/post/edit/" + id);
            return "admin/post-form-jp";
        }
        return "redirect:/admin/jp";
    }
    
    // 일본어 게시글 수정
    @PostMapping("/post/edit/{id}")
    public String updateJpPost(@PathVariable Long id, @ModelAttribute PostDto postDto) {
        postService.updatePost(id, postDto, "ja");
        return "redirect:/admin/jp";
    }
    
    // 일본어 게시글 삭제
    @PostMapping("/post/delete/{id}")
    public String deleteJpPost(@PathVariable Long id) {
        postService.deletePost(id, "ja");
        return "redirect:/admin/jp";
    }
    
    // 일본어 게시글 미리보기
    @GetMapping("/post/preview/{id}")
    public String previewJpPost(@PathVariable Long id, Model model) {
        Optional<PostDto> post = postService.getPostById(id, "ja");
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "post-detail";
        }
        return "redirect:/admin/jp";
    }

    // 일본어 이력서 관리 폼 (GET)
    @GetMapping("/resume")
    public String resumeJpForm(Model model, HttpSession session) {
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        model.addAttribute("admin", admin);
        model.addAttribute("resume", resumeService.loadResume());
        return "admin/resume-form-jp";
    }

    // 일본어 이력서 저장 (POST)
    @PostMapping("/resume")
    public String saveJpResume(@ModelAttribute com.example.hyuk_blog.entity.Resume resume, RedirectAttributes redirectAttributes) {
        resumeService.saveResume(resume);
        redirectAttributes.addFlashAttribute("message", "履歴書が保存されました！");
        return "redirect:/admin/jp";
    }

    @GetMapping("/inquiry")
    public String inquiryJpManage(Model model) {
        model.addAttribute("inquiries", inquiryService.getAllInquiries());
        return "admin/inquiry";
    }

    @GetMapping("/admin/inquiries")
    @ResponseBody
    public List<InquiryDto> getJpInquiries() {
        return inquiryService.getAllInquiries();
    }

    @PostMapping("/admin/inquiries/read")
    @ResponseBody
    public void markJpInquiriesRead() {
        inquiryService.markAllRead();
    }

    @GetMapping("/admin/inquiries/recent")
    @ResponseBody
    public List<InquiryDto> getRecentJpInquiries() {
        return inquiryService.getRecentInquiries(5);
    }
} 