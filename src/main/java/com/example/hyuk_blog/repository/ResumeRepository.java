package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByLang(String lang);
} 