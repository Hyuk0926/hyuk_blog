package com.example.hyuk_blog.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Education {
    private String school;
    private String degree;
    private String period;

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
} 