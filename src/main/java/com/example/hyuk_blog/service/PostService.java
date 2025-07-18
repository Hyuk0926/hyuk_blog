package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.PostDto;
import com.example.hyuk_blog.entity.Post;
import com.example.hyuk_blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    // 모든 공개 게시글 조회
    public List<PostDto> getAllPublishedPosts() {
        return postRepository.findByPublishedOrderByCreatedAtAsc(true)
                .stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 모든 게시글 조회 (관리자용)
    public List<PostDto> getAllPosts() {
        return postRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // ID로 게시글 조회
    public Optional<PostDto> getPostById(Long id) {
        return postRepository.findById(id)
                .map(PostDto::fromEntity);
    }
    
    // 게시글 저장
    public PostDto savePost(PostDto postDto) {
        Post post = postDto.toEntity();
        Post savedPost = postRepository.save(post);
        return PostDto.fromEntity(savedPost);
    }
    
    // 게시글 수정
    public Optional<PostDto> updatePost(Long id, PostDto postDto) {
        return postRepository.findById(id)
                .map(existingPost -> {
                    existingPost.setTitle(postDto.getTitle());
                    existingPost.setSummary(postDto.getSummary());
                    existingPost.setContent(postDto.getContent());
                    existingPost.setImageUrl(postDto.getImageUrl());
                    existingPost.setPublished(postDto.isPublished());
                    return PostDto.fromEntity(postRepository.save(existingPost));
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
    
    // 제목으로 검색 (공개된 게시글만)
    public List<PostDto> searchPublishedPosts(String title) {
        return postRepository.findByTitleContainingAndPublishedOrderByCreatedAtDesc(title, true)
                .stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
} 