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
@RequestMapping("/admin_jp")
public class AdminJpController {
    @Autowired
    private PostService postService;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private InquiryService inquiryService;

    // 관리자 대시보드 (일본어)
    @GetMapping("")
    public String adminDashboard(Model model, HttpSession session) {
        session.setAttribute("lang", "ja"); // 무조건 일본어
        List<PostDto> posts = postService.getAllPosts().stream()
            .filter(post -> post.getTitleJa() != null && !post.getTitleJa().isBlank())
            .toList();
        model.addAttribute("posts", posts);
        model.addAttribute("inquiryCount", inquiryService.getUnreadCount());
        model.addAttribute("inquiries", inquiryService.getAllInquiries());
        model.addAttribute("adminPrefix", "/admin_jp");
        return "admin/dashboard";
    }

    // 새 게시글 작성 폼 (일본어 전용)
    @GetMapping("/post/new")
    public String newPostForm(Model model, HttpSession session) {
        session.setAttribute("lang", "ja");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        model.addAttribute("post", new PostDto());
        model.addAttribute("admin", admin);
        model.addAttribute("categories", Category.values());
        model.addAttribute("formAction", "/admin_jp/post/new");
        return "admin/post-form-jp";
    }

    // 새 게시글 저장 (일본어)
    @PostMapping("/post/new")
    public String createPost(@ModelAttribute PostDto postDto, HttpSession session) {
        session.setAttribute("lang", "ja");
        postService.savePost(postDto);
        return "redirect:/admin_jp";
    }

    // 게시글 수정 폼 (일본어 전용)
    @GetMapping("/post/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model, HttpSession session) {
        session.setAttribute("lang", "ja");
        Optional<PostDto> post = postService.getPostById(id);
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            model.addAttribute("admin", admin);
            model.addAttribute("categories", Category.values());
            model.addAttribute("formAction", "/admin_jp/post/edit/" + id);
            return "admin/post-form-jp";
        }
        return "redirect:/admin_jp";
    }

    // 게시글 수정 (일본어)
    @PostMapping("/post/edit/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute PostDto postDto, HttpSession session) {
        session.setAttribute("lang", "ja");
        postService.updatePost(id, postDto);
        return "redirect:/admin_jp";
    }

    // 게시글 삭제 (일본어)
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id, HttpSession session) {
        session.setAttribute("lang", "ja");
        postService.deletePost(id);
        return "redirect:/admin_jp";
    }

    // 게시글 미리보기 (일본어)
    @GetMapping("/post/preview/{id}")
    public String previewPost(@PathVariable Long id, Model model) {
        Optional<PostDto> post = postService.getPostById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "post-detail";
        }
        return "redirect:/admin_jp";
    }

    // 이력서 관리 폼 (일본어)
    @GetMapping("/resume")
    public String resumeForm(Model model, HttpSession session) {
        session.setAttribute("lang", "ja");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        model.addAttribute("admin", admin);
        model.addAttribute("resume", resumeService.loadResume());
        return "admin/resume-form";
    }

    // 이력서 저장 (일본어)
    @PostMapping("/resume")
    public String saveResume(@ModelAttribute com.example.hyuk_blog.entity.Resume resume, RedirectAttributes redirectAttributes) {
        resumeService.saveResume(resume);
        redirectAttributes.addFlashAttribute("message", "이력서가 저장되었습니다!");
        return "redirect:/admin_jp";
    }

    @GetMapping("/inquiry")
    public String inquiryManage(Model model) {
        model.addAttribute("inquiries", inquiryService.getAllInquiries());
        return "admin/inquiry";
    }

    @GetMapping("/admin_jp/inquiries")
    @ResponseBody
    public List<InquiryDto> getInquiries() {
        return inquiryService.getAllInquiries();
    }

    @PostMapping("/admin_jp/inquiries/read")
    @ResponseBody
    public void markInquiriesRead() {
        inquiryService.markAllRead();
    }

    @GetMapping("/admin_jp/inquiries/recent")
    @ResponseBody
    public List<InquiryDto> getRecentInquiries() {
        return inquiryService.getRecentInquiries(5);
    }
} 