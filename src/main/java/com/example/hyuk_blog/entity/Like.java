package com.example.hyuk_blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Data
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "post_id", nullable = false)
    private Long postId;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    /**
     * Detached Entity 문제 방지를 위한 안전한 복사 메서드
     */
    public Like copy() {
        Like copy = new Like();
        copy.setId(this.id);
        copy.setPostId(this.postId);
        copy.setUserId(this.userId);
        copy.setCreatedAt(this.createdAt);
        return copy;
    }
} 