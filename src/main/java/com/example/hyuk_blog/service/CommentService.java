package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.CommentDto;
import com.example.hyuk_blog.entity.Comment;
import com.example.hyuk_blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    
    @Autowired
    private CommentRepository commentRepository;
    

    
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Transactional
    public CommentDto createComment(Long postId, String content, Long userId, String nickname) {
        // 새로운 Comment 엔티티 생성 (detached entity 문제 방지)
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content.trim());
        comment.setUserId(userId);
        comment.setNickname(nickname);
        
        // 저장 및 반환 (detached entity 문제 완전 방지)
        Comment savedComment = safeSave(comment);
        return convertToDto(savedComment);
    }
    
    @Transactional
    public boolean updateComment(Long commentId, Long userId, String newContent) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            
            // 댓글 작성자이거나 admin 계정인 경우 수정 가능
            if (userId.equals(comment.getUserId()) || isAdminUser(userId)) {
                // detached entity 문제 방지를 위해 내용만 업데이트
                comment.setContent(newContent.trim());
                safeSave(comment);
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
            if (userId.equals(comment.getUserId()) || isAdminUser(userId)) {
                commentRepository.delete(comment);
                return true;
            }
        }
        return false;
    }
    
    // admin 계정인지 확인하는 메서드
    private boolean isAdminUser(Long userId) {
        // admin 계정의 ID는 보통 1, 2 등 작은 숫자이므로 이를 기준으로 판단
        // 마이그레이션으로 User 테이블에 추가된 admin 계정들
        return userId != null && (userId == 1 || userId == 2); // admin, admin_jp 계정 ID
    }
    

    
    public Long getCommentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }
    
    /**
     * Detached Entity 문제를 방지하기 위한 안전한 엔티티 처리 메서드
     * 향후 다른 엔티티와의 관계가 추가될 때 사용
     */
    private Comment ensureManagedEntity(Comment comment) {
        if (comment.getId() != null) {
            // 이미 관리되는 엔티티인지 확인
            return commentRepository.findById(comment.getId()).orElse(comment);
        }
        return comment;
    }
    
    /**
     * Detached Entity 문제를 방지하기 위한 안전한 저장 메서드
     */
    private Comment safeSave(Comment comment) {
        try {
            return commentRepository.save(comment);
        } catch (Exception e) {
            logger.warn("Detached entity 오류 발생, 새로운 엔티티로 재생성: {}", e.getMessage());
            // detached entity 오류 발생 시 새로운 엔티티로 재생성
            Comment newComment = new Comment();
            newComment.setPostId(comment.getPostId());
            newComment.setNickname(comment.getNickname());
            newComment.setContent(comment.getContent());
            newComment.setUserId(comment.getUserId());
            return commentRepository.save(newComment);
        }
    }
    
    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setNickname(comment.getNickname());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUserId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setEdited(!comment.getCreatedAt().equals(comment.getUpdatedAt()));
        return dto;
    }
} 