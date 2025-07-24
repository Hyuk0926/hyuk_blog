package com.example.hyuk_blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String titleKo; // 한국어 제목
    @Column(length = 500)
    private String titleJa; // 일본어 제목

    @Column(length = 500)
    private String summaryKo; // 한국어 요약 추가
    @Column(length = 500)
    private String summaryJa; // 일본어 요약 추가

    @Column(columnDefinition = "TEXT")
    private String contentKo; // 한국어 내용 추가
    @Column(columnDefinition = "TEXT")
    private String contentJa; // 일본어 내용 추가

    @Column(length = 200)
    private String imageUrl;

    @Column(nullable = false)
    private boolean published = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}