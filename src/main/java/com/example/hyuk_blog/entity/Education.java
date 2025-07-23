package com.example.hyuk_blog.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Education {
    private String school;
    private String schoolJp;
    private String degree;
    private String degreeJp;
    private String period;

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public String getSchoolJp() { return schoolJp; }
    public void setSchoolJp(String schoolJp) { this.schoolJp = schoolJp; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public String getDegreeJp() { return degreeJp; }
    public void setDegreeJp(String degreeJp) { this.degreeJp = degreeJp; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
} 