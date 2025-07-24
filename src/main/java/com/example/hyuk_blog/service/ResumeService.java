package com.example.hyuk_blog.service;

import com.example.hyuk_blog.entity.Resume;
import com.example.hyuk_blog.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

    public enum ResumeLang { KO, JA }

    public void saveResume(Resume resume) {
        resume.setLang(ResumeLang.KO.name());
        resumeRepository.findByLang(ResumeLang.KO.name()).ifPresent(r -> resumeRepository.deleteById(r.getId()));
        resumeRepository.save(resume);
    }

    public Resume loadResume() {
        return resumeRepository.findByLang(ResumeLang.KO.name()).orElseGet(Resume::new);
    }
}