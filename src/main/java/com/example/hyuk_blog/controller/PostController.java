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

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private AdminController adminController;

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("resume", adminController.getResume());
        return "about";
    }
    
    @GetMapping("/")
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

} 