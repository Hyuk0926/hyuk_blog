package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.CommentDto;
import com.example.hyuk_blog.entity.Comment;
import com.example.hyuk_blog.entity.PostKr;
import com.example.hyuk_blog.entity.PostJp;
import com.example.hyuk_blog.entity.PostType;
import com.example.hyuk_blog.entity.User;
import com.example.hyuk_blog.repository.CommentRepository;
import com.example.hyuk_blog.repository.PostKrRepository;
import com.example.hyuk_blog.repository.PostJpRepository;
import com.example.hyuk_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostKrRepository postKrRepository;
    
    @Autowired
    private PostJpRepository postJpRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // 한국어 게시글 댓글 조회
    public List<CommentDto> getCommentsByPostKrId(Long postId) {
        List<Comment> comments = commentRepository.findByPostKrIdOrderByCreatedAtAsc(postId);
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    // 일본어 게시글 댓글 조회
    public List<CommentDto> getCommentsByPostJpId(Long postId) {
        List<Comment> comments = commentRepository.findByPostJpIdOrderByCreatedAtAsc(postId);
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Transactional
    public CommentDto createComment(Long postId, PostType postType, String content, Long userId, String nickname) {
        logger.info("Creating comment - postId: {}, postType: {}, content: {}, userId: {}, nickname: {}", 
                   postId, postType, content, userId, nickname);
        
        try {
            Comment comment = new Comment();
            comment.setContent(content.trim());
            comment.setNickname(nickname);
            comment.setPostType(postType);
            
            // User 엔티티를 찾아서 설정
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            comment.setUser(user);
            
            // 게시글 타입에 따라 연결
            if (postType == PostType.KR) {
                PostKr postKr = postKrRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("PostKr not found with id: " + postId));
                comment.setPostKr(postKr);
            } else if (postType == PostType.JP) {
                PostJp postJp = postJpRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("PostJp not found with id: " + postId));
                comment.setPostJp(postJp);
            }
            
            Comment savedComment = commentRepository.save(comment);
            logger.info("Comment saved successfully: {}", savedComment);
            
            return convertToDto(savedComment);
        } catch (Exception e) {
            logger.error("Error creating comment: {}", e.getMessage());
            throw e;
        }
    }
    
    @Transactional
    public boolean updateComment(Long commentId, Long userId, String newContent) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            
            // 댓글 작성자이거나 admin 계정인 경우 수정 가능
            if (userId.equals(comment.getUser() != null ? comment.getUser().getId() : null) || isAdminUser(userId)) {
                comment.setContent(newContent.trim());
                commentRepository.save(comment);
                return true;
            }
        }
        return false;
    }
    
    @Transactional
    public boolean deleteComment(Long commentId, Long userId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            
            // 댓글 작성자이거나 admin 계정인 경우 삭제 가능
            if (userId.equals(comment.getUser() != null ? comment.getUser().getId() : null) || isAdminUser(userId)) {
                commentRepository.delete(comment);
                return true;
            }
        }
        return false;
    }
    
    // 한국어 게시글 댓글 수 조회
    public Long getCommentCountByPostKrId(Long postId) {
        return commentRepository.countByPostKrId(postId);
    }
    
    // 일본어 게시글 댓글 수 조회
    public Long getCommentCountByPostJpId(Long postId) {
        return commentRepository.countByPostJpId(postId);
    }
    
    // admin 계정인지 확인하는 메서드
    private boolean isAdminUser(Long userId) {
        return userId != null && (userId == 1 || userId == 2); // admin, admin_jp 계정 ID
    }
    
    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setNickname(comment.getNickname());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUser() != null ? comment.getUser().getId() : null);
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setEdited(!comment.getCreatedAt().equals(comment.getUpdatedAt()));
        
        // 게시글 타입에 따라 ID 설정
        if (comment.getPostType() == PostType.KR && comment.getPostKr() != null) {
            dto.setPostkrId(comment.getPostKr().getId());
        } else if (comment.getPostType() == PostType.JP && comment.getPostJp() != null) {
            dto.setPostjpId(comment.getPostJp().getId());
        }
        
        return dto;
    }
} 