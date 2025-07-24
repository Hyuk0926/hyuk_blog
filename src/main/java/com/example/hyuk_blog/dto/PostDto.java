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
    private String titleKo; // 한국어 제목 추가
    private String titleJa; // 일본어 제목 추가
    private String summaryKo; // 한국어 요약 추가
    private String summaryJa; // 일본어 요약 추가
    private String contentKo; // 한국어 내용 추가
    private String contentJa; // 일본어 내용 추가
    private String imageUrl;
    private boolean published;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity를 DTO로 변환
    public static PostDto fromEntity(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitleKo(post.getTitleKo());
        dto.setTitleJa(post.getTitleJa());
        dto.setSummaryKo(post.getSummaryKo());
        dto.setSummaryJa(post.getSummaryJa());
        dto.setContentKo(post.getContentKo());
        dto.setContentJa(post.getContentJa());
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
        post.setTitleKo(this.titleKo);
        post.setTitleJa(this.titleJa);
        post.setSummaryKo(this.summaryKo);
        post.setSummaryJa(this.summaryJa);
        post.setContentKo(this.contentKo);
        post.setContentJa(this.contentJa);
        post.setImageUrl(this.imageUrl);
        post.setPublished(this.published);
        post.setCategory(this.category);
        return post;
    }
}