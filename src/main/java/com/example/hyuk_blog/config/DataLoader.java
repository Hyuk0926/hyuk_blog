package com.example.hyuk_blog.config;

import com.example.hyuk_blog.entity.Admin;
import com.example.hyuk_blog.entity.Post;
import com.example.hyuk_blog.entity.Inquiry;
import com.example.hyuk_blog.entity.Resume;
import com.example.hyuk_blog.entity.Visitor;
import com.example.hyuk_blog.repository.AdminRepository;
import com.example.hyuk_blog.repository.PostRepository;
import com.example.hyuk_blog.repository.InquiryRepository;
import com.example.hyuk_blog.repository.ResumeRepository;
import com.example.hyuk_blog.repository.VisitorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Iterator;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    @Autowired private InquiryRepository inquiryRepository;
    @Autowired private ResumeRepository resumeRepository;
    @Autowired private VisitorRepository visitorRepository;

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터가 없을 때만 초기 데이터 생성
        if (postRepository.count() == 0) {
            createInitialPosts();
        }
        
        if (adminRepository.count() == 0) {
            createInitialAdmin();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        // 1. inquiry.json 이관
        File inquiryFile = new File("data/inquiry.json");
        if (inquiryFile.exists()) {
            Inquiry[] inquiries = mapper.readValue(inquiryFile, Inquiry[].class);
            for (Inquiry inquiry : inquiries) {
                if (!inquiryRepository.existsById(inquiry.getId())) {
                    inquiryRepository.save(inquiry);
                }
            }
        }

        // 2. resume.json 이관
        File resumeFile = new File("data/resume.json");
        if (resumeFile.exists()) {
            Resume resume = mapper.readValue(resumeFile, Resume.class);
            if (resumeRepository.count() == 0) {
                resumeRepository.save(resume);
            }
        }

        // 3. visitor.json 이관 (daily만 예시)
        File visitorFile = new File("data/visitor.json");
        if (visitorFile.exists()) {
            JsonNode root = mapper.readTree(visitorFile);
            JsonNode daily = root.get("daily");
            if (daily != null) {
                Iterator<String> dates = daily.fieldNames();
                while (dates.hasNext()) {
                    String date = dates.next();
                    int count = daily.get(date).asInt();
                    Visitor v = new Visitor();
                    v.setDate(LocalDate.parse(date));
                    v.setCount(count);
                    visitorRepository.save(v);
                }
            }
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
        
    }
} 