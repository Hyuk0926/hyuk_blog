package com.example.hyuk_blog.config;

import com.example.hyuk_blog.entity.Admin;
import com.example.hyuk_blog.entity.Inquiry;
import com.example.hyuk_blog.entity.Resume;
import com.example.hyuk_blog.entity.User;
import com.example.hyuk_blog.repository.AdminRepository;
import com.example.hyuk_blog.repository.InquiryRepository;
import com.example.hyuk_blog.repository.ResumeRepository;
import com.example.hyuk_blog.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Iterator;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired private InquiryRepository inquiryRepository;
    @Autowired private ResumeRepository resumeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        
        if (adminRepository.count() == 0) {
            createInitialAdmin();
        } else {
            // 기존 관리자 계정 비밀번호 암호화 업데이트
            updateExistingAdminPasswords();
        }
        
        if (userRepository.count() == 0) {
            createInitialUser();
        } else {
            // 기존 사용자 계정 비밀번호 업데이트
            updateExistingUserPasswords();
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

        // 2. resumeKR.json, resumeJP.json 이관
        File resumeKRFile = new File("data/resumeKR.json");
        if (resumeKRFile.exists()) {
            Resume resumeKR = mapper.readValue(resumeKRFile, Resume.class);
            resumeKR.setLang("KO");
            if (resumeRepository.findByLang("KO").isEmpty()) {
                resumeRepository.save(resumeKR);
            }
        }
        File resumeJPFile = new File("data/resumeJP.json");
        if (resumeJPFile.exists()) {
            Resume resumeJP = mapper.readValue(resumeJPFile, Resume.class);
            resumeJP.setLang("JA");
            if (resumeRepository.findByLang("JA").isEmpty()) {
                resumeRepository.save(resumeJP);
            }
        }

        // 3. visitor.json은 JSON 파일 기반으로 관리하므로 JPA Entity 사용하지 않음
    }
    
    private void createInitialAdmin() {
        // 환경 변수에서 관리자 정보를 가져옴
        String adminUsername = System.getenv("ADMIN_USERNAME");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        String adminEmail = System.getenv("ADMIN_EMAIL");
        
        if (adminUsername == null || adminPassword == null) {
            System.out.println("경고: ADMIN_USERNAME 또는 ADMIN_PASSWORD 환경 변수가 설정되지 않았습니다.");
            System.out.println("관리자 계정 생성을 건너뜁니다.");
            System.out.println("환경 변수를 설정한 후 애플리케이션을 재시작하세요:");
            System.out.println("ADMIN_USERNAME=your_admin_username");
            System.out.println("ADMIN_PASSWORD=your_secure_password");
            System.out.println("ADMIN_EMAIL=your_admin_email@example.com");
            return;
        }
        
        Admin admin = new Admin();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setName("Administrator");
        admin.setEmail(adminEmail != null ? adminEmail : "admin@example.com");
        admin.setActive(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        adminRepository.save(admin);
        
        System.out.println("관리자 계정이 생성되었습니다.");
        System.out.println("아이디: " + adminUsername);
    }

    private void createInitialUser() {
        // 테스트용 사용자 계정 생성은 비활성화
        // 실제 운영 환경에서는 사용자가 직접 회원가입을 통해 계정을 생성해야 함
        System.out.println("테스트용 사용자 계정 생성을 건너뜁니다.");
    }

    private void updateExistingAdminPasswords() {
        System.out.println("기존 관리자 계정 확인 중...");
        
        // 기존 admin 계정들 확인
        long adminCount = adminRepository.count();
        System.out.println("총 관리자 계정 수: " + adminCount);
        
        adminRepository.findAll().forEach(admin -> {
            System.out.println("기존 관리자 계정: " + admin.getUsername() + " (ID: " + admin.getId() + ")");
            
            // BCrypt로 암호화되지 않은 경우 경고만 출력
            if (!admin.getPassword().startsWith("$2a$")) {
                System.out.println("경고: " + admin.getUsername() + " 계정의 비밀번호가 암호화되지 않았습니다.");
                System.out.println("환경 변수를 설정하여 비밀번호를 업데이트하세요:");
                System.out.println("ADMIN_USERNAME=" + admin.getUsername());
                System.out.println("ADMIN_PASSWORD=your_secure_password");
            }
        });
        
        // 환경 변수로 설정된 비밀번호가 있으면 해당 계정 업데이트
        String adminUsername = System.getenv("ADMIN_USERNAME");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        
        if (adminUsername != null && adminPassword != null) {
            final String finalAdminUsername = adminUsername;
            final String finalAdminPassword = adminPassword;
            
            adminRepository.findByUsername(finalAdminUsername).ifPresent(admin -> {
                admin.setPassword(passwordEncoder.encode(finalAdminPassword));
                adminRepository.save(admin);
                System.out.println(finalAdminUsername + " 계정 비밀번호를 환경 변수 값으로 업데이트 완료");
            });
        }
    }
    
    private void updateExistingUserPasswords() {
        // 테스트용 사용자 계정 비밀번호 업데이트는 비활성화
        System.out.println("테스트용 사용자 계정 비밀번호 업데이트를 건너뜁니다.");
    }

    private void createInitialPosts() {
        
    }
} 