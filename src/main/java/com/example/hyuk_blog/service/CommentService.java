package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.CommentDto;
import com.example.hyuk_blog.entity.Comment;
import com.example.hyuk_blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Transactional
    public CommentDto createComment(Long postId, String nickname, String password, String content, String userIp) {
        // 닉네임이 비어있으면 IP 주소 사용
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = userIp;
        }
        
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setNickname(nickname.trim());
        comment.setPassword(hashPassword(password));
        comment.setContent(content.trim());
        comment.setUserIp(userIp);
        
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }
    
    @Transactional
    public boolean updateComment(Long commentId, String password, String newContent) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            if (hashPassword(password).equals(comment.getPassword())) {
                comment.setContent(newContent.trim());
                commentRepository.save(comment);
                return true;
            }
        }
        return false;
    }
    
    @Transactional
    public boolean deleteComment(Long commentId, String password) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            if (hashPassword(password).equals(comment.getPassword())) {
                commentRepository.delete(comment);
                return true;
            }
        }
        return false;
    }
    
    public boolean verifyPassword(Long commentId, String password) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            return hashPassword(password).equals(comment.getPassword());
        }
        return false;
    }
    
    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setNickname(comment.getNickname());
        dto.setContent(comment.getContent());
        dto.setUserIp(comment.getUserIp());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setEdited(!comment.getCreatedAt().equals(comment.getUpdatedAt()));
        return dto;
    }
} 