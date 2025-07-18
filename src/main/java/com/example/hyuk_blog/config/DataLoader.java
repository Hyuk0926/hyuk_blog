package com.example.hyuk_blog.config;

import com.example.hyuk_blog.entity.Admin;
import com.example.hyuk_blog.entity.Post;
import com.example.hyuk_blog.repository.AdminRepository;
import com.example.hyuk_blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터가 없을 때만 초기 데이터 생성
        if (postRepository.count() == 0) {
            createInitialPosts();
        }
        
        if (adminRepository.count() == 0) {
            createInitialAdmin();
        }
    }
    
    private void createInitialAdmin() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("Shka991204!");
        admin.setName("Takahara Yuuki"); // 이름을 명확히 지정
        admin.setEmail("ehc28260@gmail.com");
        admin.setActive(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        adminRepository.save(admin);
        
        System.out.println("기본 관리자 계정이 생성되었습니다!");
        System.out.println("아이디: admin");
        System.out.println("비밀번호: Shka991204!");
        System.out.println("이름: Takahara Yuuki");
    }

    private void createInitialPosts() {
        // 샘플 게시글 1
        Post post1 = new Post();
        post1.setTitle("Spring Boot 기초 - 프로젝트 설정");
        post1.setSummary("Spring Boot 프로젝트를 처음부터 설정하는 방법을 알아봅니다.");
        post1.setContent("""
            <h2>Spring Boot란?</h2>
            <p>Spring Boot는 Spring Framework를 기반으로 한 애플리케이션 개발을 간소화하는 도구</p>
            
            <h3>주요 특징</h3>
            <ul>
                <li>자동 설정 (Auto Configuration)</li>
                <li>내장 서버 (Embedded Server)</li>
                <li>스타터 의존성 (Starter Dependencies)</li>
                <li>Actuator</li>
            </ul>
            
            <h3>프로젝트 생성</h3>
            <p>Spring Initializr를 사용하여 프로젝트를 생성 가능</p>
            <pre><code>https://start.spring.io/</code></pre>
            
            <h3>기본 의존성</h3>
            <ul>
                <li>spring-boot-starter-web</li>
                <li>spring-boot-starter-data-jpa</li>
                <li>spring-boot-starter-thymeleaf</li>
            </ul>
            """);
        post1.setImageUrl("https://via.placeholder.com/400x200/007bff/ffffff?text=Spring+Boot");
        post1.setPublished(true);
        post1.setCreatedAt(LocalDateTime.now());
        post1.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post1);

        
    }
} 