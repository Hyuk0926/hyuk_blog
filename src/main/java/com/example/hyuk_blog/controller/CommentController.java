package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.CommentDto;
import com.example.hyuk_blog.dto.UserDto;
import com.example.hyuk_blog.dto.AdminDto;
import com.example.hyuk_blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
            @RequestParam String content,
            HttpServletRequest request,
            HttpSession session) {
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (user == null && admin == null) {
            return ResponseEntity.status(401).body(null);
        }
        
        // user가 있으면 user 정보 사용, 없으면 admin 정보 사용
        Long userId = user != null ? user.getId() : admin.getId();
        String nickname = user != null ? user.getNickname() : admin.getUsername();
        
        CommentDto comment = commentService.createComment(postId, content, userId, nickname);
        return ResponseEntity.ok(comment);
    }
    
    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable Long commentId,
            @RequestParam String content,
            HttpSession session) {
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (user == null && admin == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        // user가 있으면 user ID 사용, 없으면 admin ID 사용
        Long userId = user != null ? user.getId() : admin.getId();
        
        boolean success = commentService.updateComment(commentId, userId, content);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        
        if (success) {
            response.put("message", "댓글이 수정되었습니다.");
        } else {
            response.put("message", "댓글을 수정할 권한이 없습니다.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable Long commentId,
            HttpSession session) {
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (user == null && admin == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        // user가 있으면 user ID 사용, 없으면 admin ID 사용
        Long userId = user != null ? user.getId() : admin.getId();
        
        boolean success = commentService.deleteComment(commentId, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        
        if (success) {
            response.put("message", "댓글이 삭제되었습니다.");
        } else {
            response.put("message", "댓글을 삭제할 권한이 없습니다.");
        }
        
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