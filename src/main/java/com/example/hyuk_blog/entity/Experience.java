package com.example.hyuk_blog.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Experience {
    private String company;
    private String position;
    private String period;
    private String description;

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 