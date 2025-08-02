package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.entity.PostKr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostKrRepository extends JpaRepository<PostKr, Long> {
    List<PostKr> findByPublishedOrderByCreatedAtDesc(boolean published);
    List<PostKr> findByPublishedOrderByCreatedAtAsc(boolean published);
    List<PostKr> findByTitleContainingAndPublishedOrderByCreatedAtDesc(String title, boolean published);
    @Query("SELECT p FROM PostKr p ORDER BY p.createdAt DESC")
    List<PostKr> findAllOrderByCreatedAtDesc();
    List<PostKr> findByCategoryAndPublishedOrderByCreatedAtDesc(Category category, boolean published);
    

} 