package com.example.hyuk_blog.service;

import com.example.hyuk_blog.entity.Like;
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
    public boolean toggleLike(String postEncryptedId, Long userId, String lang) {
        boolean exists = likeRepository.existsByPostEncryptedIdAndUserId(postEncryptedId, userId);
        
        if (exists) {
            // 좋아요 취소
            likeRepository.deleteByPostEncryptedIdAndUserId(postEncryptedId, userId);
            return false;
        } else {
            // 좋아요 추가 (detached entity 문제 완전 방지)
            Like like = new Like();
            like.setPostEncryptedId(postEncryptedId);
            like.setUserId(userId);
            safeSave(like);
            return true;
        }
    }
    
    public long getLikeCount(String postEncryptedId, String lang) {
        return likeRepository.countByPostEncryptedId(postEncryptedId);
    }
    
    public boolean isLikedByUser(String postEncryptedId, Long userId, String lang) {
        return likeRepository.existsByPostEncryptedIdAndUserId(postEncryptedId, userId);
    }
    
    /**
     * Detached Entity 문제를 방지하기 위한 안전한 엔티티 처리 메서드
     * 향후 다른 엔티티와의 관계가 추가될 때 사용
     */
    private Like ensureManagedEntity(Like like) {
        if (like.getId() != null) {
            // 이미 관리되는 엔티티인지 확인
            return likeRepository.findById(like.getId()).orElse(like);
        }
        return like;
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
            newLike.setPostEncryptedId(like.getPostEncryptedId());
            newLike.setUserId(like.getUserId());
            return likeRepository.save(newLike);
        }
    }
} 