package com.example.hyuk_blog.service;

import com.example.hyuk_blog.entity.Like;
import com.example.hyuk_blog.entity.PostType;
import com.example.hyuk_blog.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LikeService {
    
    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Transactional
    public boolean toggleLike(Long postId, PostType postType, Long userId) {
        boolean exists = likeRepository.existsByPostIdAndPostTypeAndUserId(postId, postType, userId);
        
        if (exists) {
            // 좋아요 취소
            likeRepository.deleteByPostIdAndPostTypeAndUserId(postId, postType, userId);
            return false;
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setPostId(postId);
            like.setPostType(postType);
            like.setUserId(userId);
            safeSave(like);
            return true;
        }
    }
    
    public long getLikeCount(Long postId, PostType postType) {
        return likeRepository.countByPostIdAndPostType(postId, postType);
    }
    
    public boolean isLikedByUser(Long postId, PostType postType, Long userId) {
        // userId가 null이면 좋아요하지 않은 것으로 처리
        if (userId == null) {
            return false;
        }
        return likeRepository.existsByPostIdAndPostTypeAndUserId(postId, postType, userId);
    }
    
    /**
     * Detached Entity 문제를 방지하기 위한 안전한 저장 메서드
     */
    private Like safeSave(Like like) {
        try {
            return likeRepository.save(like);
        } catch (Exception e) {
            logger.warn("Detached entity 오류 발생, 새로운 엔티티로 재생성: {}", e.getMessage());
            // detached entity 오류 발생 시 새로운 엔티티로 재생성
            Like newLike = new Like();
            newLike.setPostId(like.getPostId());
            newLike.setPostType(like.getPostType());
            newLike.setUserId(like.getUserId());
            return likeRepository.save(newLike);
        }
    }
} 