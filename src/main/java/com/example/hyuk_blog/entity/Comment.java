package com.example.hyuk_blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", length = 10)
    private PostType postType;
    
    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        try {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
        } catch (Exception e) {
            System.err.println("Error in onCreate: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        try {
            updatedAt = LocalDateTime.now();
        } catch (Exception e) {
            System.err.println("Error in onUpdate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postjp_id")
    private PostJp postJp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postkr_id")
    private PostKr postKr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}