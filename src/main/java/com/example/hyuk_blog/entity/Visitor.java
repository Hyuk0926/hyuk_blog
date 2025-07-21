package com.example.hyuk_blog.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int count;
    // getter/setter 생략

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
} 