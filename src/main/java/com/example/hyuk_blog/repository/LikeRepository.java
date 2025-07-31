package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.postEncryptedId = :postEncryptedId")
    long countByPostEncryptedId(@Param("postEncryptedId") String postEncryptedId);
    

    
    Optional<Like> findByPostEncryptedIdAndUserId(String postEncryptedId, Long userId);
    
    boolean existsByPostEncryptedIdAndUserId(String postEncryptedId, Long userId);
    
    void deleteByPostEncryptedIdAndUserId(String postEncryptedId, Long userId);
} 