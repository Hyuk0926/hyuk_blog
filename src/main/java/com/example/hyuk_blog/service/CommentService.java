package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.CommentDto;
import com.example.hyuk_blog.entity.Comment;
import com.example.hyuk_blog.entity.PostKr;
import com.example.hyuk_blog.entity.PostJp;
import com.example.hyuk_blog.repository.CommentRepository;
import com.example.hyuk_blog.repository.PostKrRepository;
import com.example.hyuk_blog.repository.PostJpRepository;
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
    

    
//    public List<CommentDto> getCommentsByPostEncryptedId(String postEncryptedId) {
//        List<Comment> comments = commentRepository.findByPostEncryptedIdOrderByCreatedAtAsc(postEncryptedId);
//        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
//    }
    
    @Transactional
    public void createComment(CommentDto commentDto, String content, Long userId, String nickname) {
        logger.info("Creating comment - postEncryptedId: {}, content: {}, userId: {}, nickname: {}", 
                   content, userId, nickname);
        
        try {
            // 1. encrypted_id로 게시글 존재 여부 확인
//            boolean postExists = findPostByEncryptedId(postEncryptedId);
//            if (!postExists) {
//                throw new EntityNotFoundException("Post not found with encrypted_id: " + postEncryptedId);
//            }

            PostJp postJp = postJpRepository.findById(commentDto.getPostjpId()).orElseThrow(EntityNotFoundException::new);
            PostKr postKr = postKrRepository.findById(commentDto.getPostkrId()).orElseThrow(EntityNotFoundException::new);

            // 2. 새로운 Comment 엔티티를 생성합니다.
            Comment comment = new Comment();

            if(postJp.equals(userId)) {

                System.out.println("일본");

//                comment.setContent(content);
//                comment.setUserId(userId);
//                comment.setPostJp(postJp);
//                comment.setNickname(nickname);

            } else if(postKr.equals(userId)) {

                System.out.println("한국");

//                comment.setContent(content);
//                comment.setUserId(userId);
//                comment.setPostKr(postKr);
//                comment.setNickname(nickname);

            }


//            comment.setContent(content.trim());
//            comment.setPostEncryptedId(postEncryptedId);
            
            logger.info("Comment entity created: {}", comment);

            // 3. 댓글을 저장합니다.
            Comment savedComment = commentRepository.save(comment);
            logger.info("Comment saved successfully: {}", savedComment);
            
            //CommentDto dto = convertToDto(savedComment);
            //logger.info("Comment DTO created: {}", dto);

            // return dto;
        } catch (Exception e) {
            logger.error("Error creating comment: {}", e.getMessage());
        }

    }
    
    @Transactional
    public boolean updateComment(Long commentId, Long userId, String newContent) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            
            // 댓글 작성자이거나 admin 계정인 경우 수정 가능
            if (userId.equals(comment.getUserId()) || isAdminUser(userId)) {
                // 내용만 업데이트
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
    

    
    public Long getCommentCount(String postEncryptedId) {
        return commentRepository.countByPostEncryptedId(postEncryptedId);
    }
    
    /**
     * encrypted_id로 게시글 존재 여부를 확인하는 메서드
     */
    private boolean findPostByEncryptedId(String postEncryptedId) {
        logger.info("Finding post by encrypted_id: {}", postEncryptedId);
        
        if (postEncryptedId == null || postEncryptedId.trim().isEmpty()) {
            return false;
        }
        
        // PostKr에서 검색
        Optional<PostKr> postKr = postKrRepository.findByEncryptedId(postEncryptedId);
        if (postKr.isPresent()) {
            logger.info("Found PostKr: {}", postKr.get().getTitle());
            return true;
        }
        
        // PostJp에서 검색
        Optional<PostJp> postJp = postJpRepository.findByEncryptedId(postEncryptedId);
        if (postJp.isPresent()) {
            logger.info("Found PostJp: {}", postJp.get().getTitle());
            return true;
        }
        
        logger.warn("Post not found with encrypted_id: {}", postEncryptedId);
        return false;
    }



//    private CommentDto convertToDto(Comment comment) {
//        CommentDto dto = new CommentDto();
//        dto.setId(comment.getId());
//        dto.setPostEncryptedId(comment.getPostEncryptedId());
//        dto.setNickname(comment.getNickname());
//        dto.setContent(comment.getContent());
//        dto.setUserId(comment.getUserId());
//        dto.setCreatedAt(comment.getCreatedAt());
//        dto.setUpdatedAt(comment.getUpdatedAt());
//        dto.setEdited(!comment.getCreatedAt().equals(comment.getUpdatedAt()));
//        return dto;
//    }
    

} 