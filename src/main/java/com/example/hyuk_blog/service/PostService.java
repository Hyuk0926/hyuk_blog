package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.PostDto;
import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.entity.Post;
import com.example.hyuk_blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder; // LocaleContextHolder import 추가

import java.util.List;
import java.util.Locale; // Locale import 추가
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Post 엔티티를 PostDto로 변환하는 헬퍼 메서드 (Locale 고려)
    private PostDto mapPostToDto(Post post) {
        PostDto dto = PostDto.fromEntity(post);
        Locale currentLocale = LocaleContextHolder.getLocale();

        // 현재 Locale에 따라 적절한 언어의 제목, 요약, 내용을 설정
        if (currentLocale.getLanguage().equals("ja")) {
            dto.setTitleKo(post.getTitleKo()); // 한국어 필드도 전달 (관리자용 폼에서 사용)
            dto.setTitleJa(post.getTitleJa()); // 일본어 필드
            dto.setSummaryKo(post.getSummaryKo());
            dto.setSummaryJa(post.getSummaryJa());
            dto.setContentKo(post.getContentKo());
            dto.setContentJa(post.getContentJa());
        } else { // 기본은 한국어
            dto.setTitleKo(post.getTitleKo()); // 한국어 필드
            dto.setTitleJa(post.getTitleJa()); // 일본어 필드도 전달 (관리자용 폼에서 사용)
            dto.setSummaryKo(post.getSummaryKo());
            dto.setSummaryJa(post.getSummaryJa());
            dto.setContentKo(post.getContentKo());
            dto.setContentJa(post.getContentJa());
        }
        return dto;
    }

    // 모든 공개 게시글 조회
    public List<PostDto> getAllPublishedPosts() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (currentLocale.getLanguage().equals("ja")) {
            // 일본어: 일본어 필드가 비어있지 않은 글만
            return postRepository.findByPublishedOrderByCreatedAtDesc(true)
                .stream()
                .filter(post -> post.getTitleJa() != null && !post.getTitleJa().isBlank())
                .map(this::mapPostToDto)
                .collect(Collectors.toList());
        } else {
            // 한국어: 한국어 필드가 비어있지 않은 글만
            return postRepository.findByPublishedOrderByCreatedAtDesc(true)
                .stream()
                .filter(post -> post.getTitleKo() != null && !post.getTitleKo().isBlank())
                .map(this::mapPostToDto)
                .collect(Collectors.toList());
        }
    }

    // 모든 게시글 조회 (관리자용)
    public List<PostDto> getAllPosts() {
        return postRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::mapPostToDto) // 수정된 헬퍼 메서드 사용
                .collect(Collectors.toList());
    }

    // ID로 게시글 조회
    public Optional<PostDto> getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::mapPostToDto); // 수정된 헬퍼 메서드 사용
    }

    // 게시글 저장
    public PostDto savePost(PostDto postDto) {
        Post post = postDto.toEntity(); // DTO의 모든 언어 필드가 엔티티로 변환됨
        Post savedPost = postRepository.save(post);
        return mapPostToDto(savedPost); // 저장된 엔티티를 다시 DTO로 변환하여 반환
    }

    // 게시글 수정
    public Optional<PostDto> updatePost(Long id, PostDto postDto) {
        return postRepository.findById(id)
                .map(existingPost -> {
                    // 모든 언어 필드를 업데이트
                    existingPost.setTitleKo(postDto.getTitleKo());
                    existingPost.setTitleJa(postDto.getTitleJa());
                    existingPost.setSummaryKo(postDto.getSummaryKo());
                    existingPost.setSummaryJa(postDto.getSummaryJa());
                    existingPost.setContentKo(postDto.getContentKo());
                    existingPost.setContentJa(postDto.getContentJa());
                    existingPost.setImageUrl(postDto.getImageUrl());
                    existingPost.setPublished(postDto.isPublished());
                    existingPost.setCategory(postDto.getCategory());
                    return mapPostToDto(postRepository.save(existingPost)); // 수정된 헬퍼 메서드 사용
                });
    }

    // 게시글 삭제
    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 제목으로 검색 (공개된 게시글만) - 다국어 검색을 지원하려면 쿼리 로직 변경 필요
    // 현재는 단일 필드(title)만 검색하므로, titleKo 또는 titleJa 중 하나만 검색하거나, OR 조건으로 두 필드를 모두 검색하도록 수정해야 합니다.
    public List<PostDto> searchPublishedPosts(String query) {
         Locale currentLocale = LocaleContextHolder.getLocale();
         if (currentLocale.getLanguage().equals("ja")) {
             // 일본어 검색 쿼리 (titleJa 필드 사용)
             return postRepository.findByTitleJaContainingAndPublishedOrderByCreatedAtDesc(query, true)
                     .stream()
                         .filter(post -> post.getTitleJa() != null && !post.getTitleJa().isBlank())
                     .map(this::mapPostToDto)
                     .collect(Collectors.toList());
         } else {
             // 한국어 검색 쿼리 (titleKo 필드 사용)
             return postRepository.findByTitleKoContainingAndPublishedOrderByCreatedAtDesc(query, true)
                     .stream()
                     .filter(post -> post.getTitleKo() != null && !post.getTitleKo().isBlank())
                     .map(this::mapPostToDto)
                     .collect(Collectors.toList());
         }
    }

    // 카테고리로 공개된 게시글 조회
    public List<PostDto> getPublishedPostsByCategory(Category category) {
        return postRepository.findByCategoryAndPublishedOrderByCreatedAtDesc(category, true)
                .stream()
                .map(this::mapPostToDto) // 수정된 헬퍼 메서드 사용
                .collect(Collectors.toList());
    }
}