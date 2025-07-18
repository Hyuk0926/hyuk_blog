package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // 공개된 게시글만 조회
    List<Post> findByPublishedOrderByCreatedAtDesc(boolean published);
    
    // 제목으로 검색 (공개된 게시글만)
    List<Post> findByTitleContainingAndPublishedOrderByCreatedAtDesc(String title, boolean published);
    
    // 모든 게시글을 생성일 기준 내림차순으로 조회
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllOrderByCreatedAtDesc();
} 