package com.example.hyuk_blog.dto;

import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.entity.PostKr;
import com.example.hyuk_blog.entity.PostJp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import org.springframework.context.i18n.LocaleContextHolder;

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

    // PostKr Entity를 DTO로 변환
    public static PostDto fromKrEntity(PostKr post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitleKo(post.getTitle());
        dto.setImageUrl(post.getImageUrl());
        dto.setPublished(post.isPublished());
        dto.setCategory(post.getCategory());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setSummaryKo(post.getSummary());
        dto.setContentKo(post.getContent());
        return dto;
    }
    // PostJp Entity를 DTO로 변환
    public static PostDto fromJpEntity(PostJp post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitleJa(post.getTitle());
        dto.setImageUrl(post.getImageUrl());
        dto.setPublished(post.isPublished());
        dto.setCategory(post.getCategory());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setSummaryJa(post.getSummary());
        dto.setContentJa(post.getContent());
        System.out.println("[JP DTO] id=" + dto.getId() + ", title=" + dto.getTitleJa() + ", summary=" + dto.getSummaryJa() + ", content=" + dto.getContentJa());
        return dto;
    }
    // DTO를 PostKr Entity로 변환
    public PostKr toKrEntity() {
        PostKr post = new PostKr();
        post.setId(this.id);
        post.setTitle(this.titleKo);
        post.setSummary(this.summaryKo);
        post.setContent(this.contentKo);
        post.setImageUrl(this.imageUrl);
        post.setPublished(this.published);
        post.setCategory(this.category);
        return post;
    }
    // DTO를 PostJp Entity로 변환
    public PostJp toJpEntity() {
        PostJp post = new PostJp();
        post.setId(this.id);
        post.setTitle(this.titleJa);
        post.setSummary(this.summaryJa);
        post.setContent(this.contentJa);
        post.setImageUrl(this.imageUrl);
        post.setPublished(this.published);
        post.setCategory(this.category);
        return post;
    }

    // 현재 언어에 맞는 제목 반환
    public String getTitle() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if ("ja".equals(lang)) {
            return titleJa;
        } else {
            return titleKo;
        }
    }
    // 현재 언어에 맞는 요약 반환
    public String getSummary() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if ("ja".equals(lang)) {
            return summaryJa;
        } else {
            return summaryKo;
        }
    }
    // 현재 언어에 맞는 내용 반환
    public String getContent() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        System.out.println("[getContent] lang=" + lang + ", contentJa=" + contentJa + ", contentKo=" + contentKo);
        if ("ja".equals(lang)) {
            return contentJa;
        } else {
            return contentKo;
        }
    }
}