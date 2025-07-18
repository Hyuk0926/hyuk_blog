package com.example.hyuk_blog.dto;

import com.example.hyuk_blog.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Entity를 DTO로 변환
    public static AdminDto fromEntity(Admin admin) {
        AdminDto dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setPassword(admin.getPassword());
        dto.setName(admin.getName());
        dto.setEmail(admin.getEmail());
        dto.setActive(admin.isActive());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());
        return dto;
    }
    
    // DTO를 Entity로 변환
    public Admin toEntity() {
        Admin admin = new Admin();
        admin.setId(this.id);
        admin.setUsername(this.username);
        admin.setPassword(this.password);
        admin.setName(this.name);
        admin.setEmail(this.email);
        admin.setActive(this.active);
        return admin;
    }
} 