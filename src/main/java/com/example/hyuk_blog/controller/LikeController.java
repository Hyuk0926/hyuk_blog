package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.UserDto;
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
    
    @PostMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long postId,
            @RequestParam String lang,
            HttpServletRequest request,
            HttpSession session) {
        
        // 로그인 확인
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        boolean isLiked = likeService.toggleLike(postId, user.getId(), lang);
        long likeCount = likeService.getLikeCount(postId, lang);
        
        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        response.put("likeCount", likeCount);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{postId}/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
            @PathVariable Long postId,
            @RequestParam String lang,
            HttpServletRequest request,
            HttpSession session) {
        
        UserDto user = (UserDto) session.getAttribute("user");
        boolean isLiked = likeService.isLikedByUser(postId, user != null ? user.getId() : null, lang);
        long likeCount = likeService.getLikeCount(postId, lang);
        
        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        response.put("likeCount", likeCount);
        
        return ResponseEntity.ok(response);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
} 