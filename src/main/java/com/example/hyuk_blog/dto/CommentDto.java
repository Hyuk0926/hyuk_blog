package com.example.hyuk_blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private Long postjpId;
    private Long postkrId;
//    private String postEncryptedId;
    private String nickname;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isEdited;
} 