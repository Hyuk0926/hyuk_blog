package com.example.hyuk_blog.dto;

import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String imageUrl;
    private boolean published;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Entity를 DTO로 변환
    public static PostDto fromEntity(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSummary(post.getSummary());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setPublished(post.isPublished());
        dto.setCategory(post.getCategory());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
    
    // DTO를 Entity로 변환
    public Post toEntity() {
        Post post = new Post();
        post.setId(this.id);
        post.setTitle(this.title);
        post.setSummary(this.summary);
        post.setContent(this.content);
        post.setImageUrl(this.imageUrl);
        post.setPublished(this.published);
        post.setCategory(this.category);
        return post;
    }
}
