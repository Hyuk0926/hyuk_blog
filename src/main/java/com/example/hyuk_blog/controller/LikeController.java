package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.UserDto;
import com.example.hyuk_blog.dto.AdminDto;
import com.example.hyuk_blog.entity.PostType;
import com.example.hyuk_blog.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    
    @Autowired
    private LikeService likeService;
    
    // 한국어 게시글 좋아요 토글
    @PostMapping("/kr/{postId}")
    public ResponseEntity<Map<String, Object>> toggleLikeKr(
            @PathVariable Long postId,
            HttpSession session) {
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (user == null && admin == null) {
            return ResponseEntity.status(401).build();
        }
        
        Long userId = user != null ? user.getId() : admin.getId();
        
        try {
            boolean isLiked = likeService.toggleLike(postId, PostType.KR, userId);
            long likeCount = likeService.getLikeCount(postId, PostType.KR);
            
            Map<String, Object> response = new HashMap<>();
            response.put("liked", isLiked);
            response.put("likeCount", likeCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in toggleLike: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("error", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 일본어 게시글 좋아요 토글
    @PostMapping("/jp/{postId}")
    public ResponseEntity<Map<String, Object>> toggleLikeJp(
            @PathVariable Long postId,
            HttpSession session) {
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (user == null && admin == null) {
            return ResponseEntity.status(401).build();
        }
        
        Long userId = user != null ? user.getId() : admin.getId();
        
        try {
            boolean isLiked = likeService.toggleLike(postId, PostType.JP, userId);
            long likeCount = likeService.getLikeCount(postId, PostType.JP);
            
            Map<String, Object> response = new HashMap<>();
            response.put("liked", isLiked);
            response.put("likeCount", likeCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in toggleLike: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("error", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 한국어 게시글 좋아요 상태 조회
    @GetMapping("/kr/{postId}/status")
    public ResponseEntity<Map<String, Object>> getLikeStatusKr(
            @PathVariable Long postId,
            HttpSession session) {
        
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        Long userId = user != null ? user.getId() : (admin != null ? admin.getId() : null);
        
        long likeCount = likeService.getLikeCount(postId, PostType.KR);
        
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount);
        
        // 로그인한 사용자만 좋아요 상태 반환
        if (userId != null) {
            boolean isLiked = likeService.isLikedByUser(postId, PostType.KR, userId);
            response.put("liked", isLiked);
        } else {
            response.put("liked", false);
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 일본어 게시글 좋아요 상태 조회
    @GetMapping("/jp/{postId}/status")
    public ResponseEntity<Map<String, Object>> getLikeStatusJp(
            @PathVariable Long postId,
            HttpSession session) {
        
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        Long userId = user != null ? user.getId() : (admin != null ? admin.getId() : null);
        
        long likeCount = likeService.getLikeCount(postId, PostType.JP);
        
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount);
        
        // 로그인한 사용자만 좋아요 상태 반환
        if (userId != null) {
            boolean isLiked = likeService.isLikedByUser(postId, PostType.JP, userId);
            response.put("liked", isLiked);
        } else {
            response.put("liked", false);
        }
        
        return ResponseEntity.ok(response);
    }
} 