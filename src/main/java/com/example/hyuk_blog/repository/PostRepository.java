package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 공개된 게시글만 조회 (기존 로직 유지 또는 필요에 따라 다국어 필터링 추가)
    List<Post> findByPublishedOrderByCreatedAtDesc(boolean published);

    // 공개된 게시글만 조회 (오름차순)
    List<Post> findByPublishedOrderByCreatedAtAsc(boolean published);

    // 제목으로 검색 (한국어, 공개된 게시글만)
    List<Post> findByTitleKoContainingAndPublishedOrderByCreatedAtDesc(String titleKo, boolean published);

    // 제목으로 검색 (일본어, 공개된 게시글만)
    List<Post> findByTitleJaContainingAndPublishedOrderByCreatedAtDesc(String titleJa, boolean published); // 이 줄을 추가합니다.

    // 모든 게시글을 생성일 기준 내림차순으로 조회
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllOrderByCreatedAtDesc();

    List<Post> findByCategoryAndPublishedOrderByCreatedAtDesc(Category category, boolean published);
}