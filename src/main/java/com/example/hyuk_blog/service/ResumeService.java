package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.ResumeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ResumeService {
    private static final String RESUME_FILE = "data/resume.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveResume(ResumeDto resume) {
        try {
            File file = new File(RESUME_FILE);
            file.getParentFile().mkdirs();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, resume);
        } catch (IOException e) {
            throw new RuntimeException("이력서 저장 실패", e);
        }
    }

    public ResumeDto loadResume() {
        try {
            File file = new File(RESUME_FILE);
            if (file.exists()) {
                return objectMapper.readValue(file, ResumeDto.class);
            } else {
                return new ResumeDto();
            }
        } catch (IOException e) {
            throw new RuntimeException("이력서 불러오기 실패", e);
        }
    }
} 