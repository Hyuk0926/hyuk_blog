package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.entity.PostJp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostJpRepository extends JpaRepository<PostJp, Long> {
    List<PostJp> findByPublishedOrderByCreatedAtDesc(boolean published);
    List<PostJp> findByPublishedOrderByCreatedAtAsc(boolean published);
    List<PostJp> findByTitleContainingAndPublishedOrderByCreatedAtDesc(String title, boolean published);
    @Query("SELECT p FROM PostJp p ORDER BY p.createdAt DESC")
    List<PostJp> findAllOrderByCreatedAtDesc();
    List<PostJp> findByCategoryAndPublishedOrderByCreatedAtDesc(Category category, boolean published);
    
    Optional<PostJp> findByEncryptedId(String encryptedId);
} 