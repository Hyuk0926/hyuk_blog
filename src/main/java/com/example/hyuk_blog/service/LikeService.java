package com.example.hyuk_blog.service;

import com.example.hyuk_blog.entity.Like;
import com.example.hyuk_blog.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Transactional
    public boolean toggleLike(Long postId, String userIp, String lang) {
        boolean exists = likeRepository.existsByPostIdAndUserIp(postId, userIp);
        
        if (exists) {
            // 좋아요 취소
            likeRepository.deleteByPostIdAndUserIp(postId, userIp);
            return false;
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setPostId(postId);
            like.setUserIp(userIp);
            likeRepository.save(like);
            return true;
        }
    }
    
    public long getLikeCount(Long postId, String lang) {
        return likeRepository.countByPostId(postId);
    }
    
    public boolean isLikedByUser(Long postId, String userIp, String lang) {
        return likeRepository.existsByPostIdAndUserIp(postId, userIp);
    }
} 