package com.example.hyuk_blog.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    @Column(length = 2000)
    private String introduction;
    private String birth;
    private String address;
    private String skills;

    @ElementCollection
    private List<Education> educations;

    @ElementCollection
    private List<Experience> experiences;
    @Column(length = 2000)
    private String studentLife;
    @Column(length = 2000)
    private String strengthsWeaknesses;
    @Column(length = 2000)
    private String effortExperience;
    @Column(length = 2000)
    private String japanItMotivation;
    @Column(length = 2000)
    private String futurePlan;
    private String photoUrl;
    // getter/setter 생략

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getBirth() { return birth; }
    public void setBirth(String birth) { this.birth = birth; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public List<Education> getEducations() { return educations; }
    public void setEducations(List<Education> educations) { this.educations = educations; }
    public List<Experience> getExperiences() { return experiences; }
    public void setExperiences(List<Experience> experiences) { this.experiences = experiences; }
    public String getStudentLife() { return studentLife; }
    public void setStudentLife(String studentLife) { this.studentLife = studentLife; }
    public String getStrengthsWeaknesses() { return strengthsWeaknesses; }
    public void setStrengthsWeaknesses(String strengthsWeaknesses) { this.strengthsWeaknesses = strengthsWeaknesses; }
    public String getEffortExperience() { return effortExperience; }
    public void setEffortExperience(String effortExperience) { this.effortExperience = effortExperience; }
    public String getJapanItMotivation() { return japanItMotivation; }
    public void setJapanItMotivation(String japanItMotivation) { this.japanItMotivation = japanItMotivation; }
    public String getFuturePlan() { return futurePlan; }
    public void setFuturePlan(String futurePlan) { this.futurePlan = futurePlan; }
} 