package com.example.hyuk_blog.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts_kr")
@Data
@NoArgsConstructor
public class PostKr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postkr_id")
    private Long id;

    @Column(length = 500)
    private String title;

    @Column(length = 500)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 200)
    private String imageUrl;

    @Column(nullable = false)
    private boolean published = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Category category;

//    @Column(name = "encrypted_id", unique = true, nullable = true)
//    private String encryptedId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


//    @PrePersist
//    protected void onCreate() {
//        if (encryptedId == null && this.id != null) {
//            this.encryptedId = generateEncryptedId();
//        }
//    }
//
//    private String generateEncryptedId() {
//        // KR_id_제목 형식으로 암호화 (한국어 제목 안전 처리)
//        String safeTitle = "untitled";
//        if (this.title != null && !this.title.trim().isEmpty()) {
//            // 한국어 제목에서 영숫자만 추출하고 나머지는 언더스코어로 변경
//            safeTitle = this.title.replaceAll("[^a-zA-Z0-9]", "_")
//                                 .replaceAll("_+", "_")  // 연속된 언더스코어를 하나로
//                                 .replaceAll("^_|_$", ""); // 앞뒤 언더스코어 제거
//            if (safeTitle.isEmpty()) {
//                safeTitle = "untitled";
//            }
//        }
//
//        String rawId = "KR_" + this.id + "_" + safeTitle;
//
//        // Base64 인코딩으로 URL 안전한 문자열 생성
//        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(rawId.getBytes());
//    }
} 