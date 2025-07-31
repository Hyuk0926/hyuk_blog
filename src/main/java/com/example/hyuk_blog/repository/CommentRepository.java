package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    @Query("SELECT c FROM Comment c WHERE c.postId = :postId ORDER BY c.createdAt ASC")
    List<Comment> findByPostIdOrderByCreatedAtAsc(@Param("postId") Long postId);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.postId = :postId")
    Long countByPostId(@Param("postId") Long postId);
    

} 