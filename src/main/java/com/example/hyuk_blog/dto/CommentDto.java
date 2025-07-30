package com.example.hyuk_blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private Long postId;
    private String nickname;
    private String content;
    private String userIp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isEdited;
} 