package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.postId = :postId")
    long countByPostId(@Param("postId") Long postId);
    
    Optional<Like> findByPostIdAndUserIp(Long postId, String userIp);
    
    boolean existsByPostIdAndUserIp(Long postId, String userIp);
    
    void deleteByPostIdAndUserIp(Long postId, String userIp);
} 