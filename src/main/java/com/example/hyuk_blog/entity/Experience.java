package com.example.hyuk_blog.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Experience {
    private String company;
    private String companyJp;
    private String position;
    private String positionJp;
    private String period;
    private String description;
    private String descriptionJp;

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getCompanyJp() { return companyJp; }
    public void setCompanyJp(String companyJp) { this.companyJp = companyJp; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getPositionJp() { return positionJp; }
    public void setPositionJp(String positionJp) { this.positionJp = positionJp; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDescriptionJp() { return descriptionJp; }
    public void setDescriptionJp(String descriptionJp) { this.descriptionJp = descriptionJp; }
} 