package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.CommentDto;
import com.example.hyuk_blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }
    
    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long postId,
            @RequestParam String nickname,
            @RequestParam String password,
            @RequestParam String content,
            HttpServletRequest request) {
        
        String userIp = getClientIpAddress(request);
        CommentDto comment = commentService.createComment(postId, nickname, password, content, userIp);
        return ResponseEntity.ok(comment);
    }
    
    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable Long commentId,
            @RequestParam String password,
            @RequestParam String content) {
        
        boolean success = commentService.updateComment(commentId, password, content);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        
        if (success) {
            response.put("message", "댓글이 수정되었습니다.");
        } else {
            response.put("message", "비밀번호가 일치하지 않습니다.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable Long commentId,
            @RequestParam String password) {
        
        boolean success = commentService.deleteComment(commentId, password);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        
        if (success) {
            response.put("message", "댓글이 삭제되었습니다.");
        } else {
            response.put("message", "비밀번호가 일치하지 않습니다.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{commentId}/verify")
    public ResponseEntity<Map<String, Object>> verifyPassword(
            @PathVariable Long commentId,
            @RequestParam String password) {
        
        boolean isValid = commentService.verifyPassword(commentId, password);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        
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