package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.CommentDto;
import com.example.hyuk_blog.dto.UserDto;
import com.example.hyuk_blog.dto.AdminDto;
import com.example.hyuk_blog.entity.PostType;
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
    
    // 한국어 게시글 댓글 조회
    @GetMapping("/kr/{postId}")
    public ResponseEntity<Map<String, Object>> getCommentsKr(@PathVariable Long postId, HttpSession session) {
        try {
            System.out.println("Getting comments for KR postId: " + postId);
            List<CommentDto> comments = commentService.getCommentsByPostKrId(postId);
            System.out.println("Found " + comments.size() + " comments");
            
            // 현재 로그인한 사용자 정보 가져오기
            UserDto user = (UserDto) session.getAttribute("user");
            AdminDto admin = (AdminDto) session.getAttribute("admin");
            Long currentUserId = user != null ? user.getId() : (admin != null ? admin.getId() : null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("comments", comments);
            response.put("currentUserId", currentUserId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error getting comments: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    // 일본어 게시글 댓글 조회
    @GetMapping("/jp/{postId}")
    public ResponseEntity<Map<String, Object>> getCommentsJp(@PathVariable Long postId, HttpSession session) {
        try {
            System.out.println("Getting comments for JP postId: " + postId);
            List<CommentDto> comments = commentService.getCommentsByPostJpId(postId);
            System.out.println("Found " + comments.size() + " comments");
            
            // 현재 로그인한 사용자 정보 가져오기
            UserDto user = (UserDto) session.getAttribute("user");
            AdminDto admin = (AdminDto) session.getAttribute("admin");
            Long currentUserId = user != null ? user.getId() : (admin != null ? admin.getId() : null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("comments", comments);
            response.put("currentUserId", currentUserId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error getting comments: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    // 한국어 게시글 댓글 작성
    @PostMapping("/kr/{postId}")
    public ResponseEntity<Map<String, Object>> createCommentKr(
            @PathVariable Long postId,
            @RequestParam String content,
            HttpSession session) {
        
        System.out.println("=== Comment KR Request Debug ===");
        System.out.println("PostId: " + postId);
        System.out.println("Content: " + content);
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (user == null && admin == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        Long userId = user != null ? user.getId() : admin.getId();
        String nickname = user != null ? user.getNickname() : admin.getUsername();
        
        try {
            CommentDto commentDto = commentService.createComment(postId, PostType.KR, content, userId, nickname);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "댓글이 작성되었습니다.");
            response.put("comment", commentDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in createComment: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "댓글 작성에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 일본어 게시글 댓글 작성
    @PostMapping("/jp/{postId}")
    public ResponseEntity<Map<String, Object>> createCommentJp(
            @PathVariable Long postId,
            @RequestParam String content,
            HttpSession session) {
        
        System.out.println("=== Comment JP Request Debug ===");
        System.out.println("PostId: " + postId);
        System.out.println("Content: " + content);
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        if (user == null && admin == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        Long userId = user != null ? user.getId() : admin.getId();
        String nickname = user != null ? user.getNickname() : admin.getUsername();
        
        try {
            CommentDto commentDto = commentService.createComment(postId, PostType.JP, content, userId, nickname);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "댓글이 작성되었습니다.");
            response.put("comment", commentDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in createComment: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "댓글 작성에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
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
} 