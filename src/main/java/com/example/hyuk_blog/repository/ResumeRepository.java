package com.example.hyuk_blog.repository;

import com.example.hyuk_blog.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
} 