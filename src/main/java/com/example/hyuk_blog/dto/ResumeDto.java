package com.example.hyuk_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {
    private String name;
    private String introduction;
    private String email;
    private String phone;
    private String photoUrl;
    private String birth;
    private String address;
    private List<Education> educations;
    private List<Experience> experiences;
    private String skills;
    private String studentLife; // 학생생활(성장과정)
    private String strengthsWeaknesses; // 장점과 단점
    private String effortExperience; // 인생에서 노력했던 경험
    private String japanItMotivation; // 일본 IT 취업 지망동기
    private String futurePlan; // 장래의 계획 및 포부

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Education {
        private String school;
        private String degree;
        private String period;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Experience {
        private String company;
        private String position;
        private String period;
        private String description;
    }
} 