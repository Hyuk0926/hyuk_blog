package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.PostDto;
import com.example.hyuk_blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    
    @Autowired
    private PostService postService;
    
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

    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String q, Model model) {
        List<PostDto> posts = postService.searchPublishedPosts(q);
        model.addAttribute("posts", posts);
        model.addAttribute("searchQuery", q);
        return "search";
    }
} 