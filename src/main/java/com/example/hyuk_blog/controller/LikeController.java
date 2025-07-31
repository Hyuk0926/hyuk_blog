package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.UserDto;
import com.example.hyuk_blog.dto.AdminDto;
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
    
    @PostMapping("/{postEncryptedId}")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable String postEncryptedId,
            @RequestParam(required = false) String lang,
            HttpServletRequest request,
            HttpSession session) {
        
        System.out.println("=== Like Request Debug ===");
        System.out.println("PostEncryptedId: " + postEncryptedId);
        System.out.println("Lang: " + lang);
        System.out.println("Session ID: " + (session != null ? session.getId() : "null"));
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        System.out.println("User: " + (user != null ? user.getUsername() : "null"));
        System.out.println("Admin: " + (admin != null ? admin.getUsername() : "null"));
        
        if (user == null && admin == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        // user가 있으면 user ID 사용, 없으면 admin ID 사용
        Long userId = user != null ? user.getId() : admin.getId();
        System.out.println("UserId: " + userId);
        
        try {
            boolean isLiked = likeService.toggleLike(postEncryptedId, userId, lang);
            long likeCount = likeService.getLikeCount(postEncryptedId, lang);
            
            Map<String, Object> response = new HashMap<>();
            response.put("liked", isLiked);
            response.put("likeCount", likeCount);
            
            System.out.println("Response: " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in toggleLike: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("error", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @GetMapping("/{postEncryptedId}/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
            @PathVariable String postEncryptedId,
            @RequestParam String lang,
            HttpServletRequest request,
            HttpSession session) {
        
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        Long userId = user != null ? user.getId() : (admin != null ? admin.getId() : null);
        
        boolean isLiked = likeService.isLikedByUser(postEncryptedId, userId, lang);
        long likeCount = likeService.getLikeCount(postEncryptedId, lang);
        
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