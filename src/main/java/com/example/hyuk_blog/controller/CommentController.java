package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.dto.CommentDto;
import com.example.hyuk_blog.dto.UserDto;
import com.example.hyuk_blog.dto.AdminDto;
import com.example.hyuk_blog.entity.Comment;
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
    
    @GetMapping("/{postEncryptedId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable String postEncryptedId) {
        try {
            System.out.println("Getting comments for postEncryptedId: " + postEncryptedId);
            List<CommentDto> comments = commentService.getCommentsByPostEncryptedId(postEncryptedId);
            System.out.println("Found " + comments.size() + " comments");
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            System.err.println("Error getting comments: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
//    @GetMapping("/test")
//    public ResponseEntity<String> test() {
//        try {
//            System.out.println("Testing comment service...");
//            return ResponseEntity.ok("Comment controller is working!");
//        } catch (Exception e) {
//            System.err.println("Test failed: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Test failed: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/test-db")
//    public ResponseEntity<String> testDb() {
//        try {
//            System.out.println("Testing database connection...");
//            // 간단한 테스트 - 댓글 개수 조회 (임시로 테스트용 encrypted_id 사용)
//            Long count = commentService.getCommentCount("test_encrypted_id");
//            System.out.println("Comment count for test post: " + count);
//            return ResponseEntity.ok("Database is working. Count: " + count);
//        } catch (Exception e) {
//            System.err.println("Database test failed: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Database test failed: " + e.getMessage());
//        }
//    }
    
    @GetMapping("/check-posts")
    public ResponseEntity<String> checkPosts() {
        try {
            System.out.println("Checking available posts...");
            // 실제 존재하는 게시글 ID들을 확인하는 로직을 추가할 수 있습니다
            return ResponseEntity.ok("Check server logs for available post IDs");
        } catch (Exception e) {
            System.err.println("Check posts failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Check posts failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/{postEncryptedId}")
    public String createComment(@RequestParam String content, HttpServletRequest request, HttpSession session, CommentDto commentDto) {
        
        System.out.println("=== Comment Request Debug ===");
        System.out.println("Content: " + content);
        System.out.println("Session ID: " + (session != null ? session.getId() : "null"));
        
        // 로그인 확인 (user 또는 admin)
        UserDto user = (UserDto) session.getAttribute("user");
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        
        System.out.println("User: " + (user != null ? user.getUsername() : "null"));
        System.out.println("Admin: " + (admin != null ? admin.getUsername() : "null"));
        
        // 임시로 세션 체크 우회 (테스트용)
        Long userId = null;
        String nickname = "테스트사용자";
        
        if (user != null) {
            userId = user.getId();
            nickname = user.getNickname();
        } else if (admin != null) {
            userId = admin.getId();
            nickname = admin.getUsername();
        } else {
            System.out.println("No user or admin found in session");
            System.out.println("Session attributes: " + (session != null ? session.getAttributeNames() : "null"));
        }
        
        System.out.println("UserId: " + userId);
        System.out.println("Nickname: " + nickname);
        
        try {
            System.out.println("Calling commentService.createComment with:");
//            System.out.println("  postEncryptedId: " + postEncryptedId);
            System.out.println("  content: " + content);
            System.out.println("  userId: " + userId);
            System.out.println("  nickname: " + nickname);
            
            // 실제 저장 코드로 복원
            commentService.createComment(commentDto, content, userId, nickname);

            return "redirect:/index";
        } catch (Exception e) {
            System.err.println("Error in createComment: " + e.getMessage());
            System.err.println("Exception type: " + e.getClass().getName());
            System.err.println("Stack trace:");
            e.printStackTrace();
            return null;
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