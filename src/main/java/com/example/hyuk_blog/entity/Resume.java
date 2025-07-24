package com.example.hyuk_blog.entity;

import jakarta.persistence.*;
import java.util.List;

import lombok.*;


@Entity
@Data
@NoArgsConstructor // Lombok 사용 시 추가
@AllArgsConstructor // Lombok 사용 시 추가
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameKo;
    private String nameJa;
    private String email;
    private String phone;
    @Column(length = 2000)
    private String introduction; // introduction 필드
    @Column(length = 2000)
    private String introductionKo; // introductionKo 필드
    // introductionJa 필드 삭제

    private String birth;
    private String addressKo;
    private String addressJa;
    private String skills;

    @ElementCollection
    @CollectionTable(name = "resume_education", joinColumns = @JoinColumn(name = "resume_id"))
    private List<Education> educations;

    @ElementCollection
    private List<Experience> experiences;
    
    @Column(length = 2000)
    private String studentLifeKo; // studentLife -> studentLifeKo 로 변경
    @Column(length = 2000) // 새로운 필드 추가
    private String studentLifeJa; // 일본어 필드 추가

    @Column(length = 2000)
    private String strengthsWeaknessesKo; // strengthsWeaknesses -> strengthsWeaknessesKo 로 변경
    @Column(length = 2000) // 새로운 필드 추가
    private String strengthsWeaknessesJa; // 일본어 필드 추가

    @Column(length = 2000)
    private String effortExperienceKo; // effortExperience -> effortExperienceKo 로 변경
    @Column(length = 2000) // 새로운 필드 추가
    private String effortExperienceJa; // 일본어 필드 추가

    @Column(length = 2000)
    private String japanItMotivationKo; // japanItMotivation -> japanItMotivationKo 로 변경
    @Column(length = 2000) // 새로운 필드 추가
    private String japanItMotivationJa; // 일본어 필드 추가

    @Column(length = 2000)
    private String futurePlanKo; // futurePlan -> futurePlanKo 로 변경
    @Column(length = 2000) // 새로운 필드 추가
    private String futurePlanJa; // 일본어 필드 추가

    private String photoUrl;
    private String lang; // KO/JA 이력서 구분 필드 추가

    // Lombok을 사용하지 않는다면, 아래 getter/setter 들도 위 변경된 필드명에 맞춰 수동으로 변경해야 합니다.
    // 예: getIntroduction() -> getIntroductionKo(), setIntroduction() -> setIntroductionKo() 등
    // 또한, getIntroductionJa(), setIntroductionJa() 등 새로운 Getter/Setter도 추가해야 합니다.
    // Lombok(@Getter, @Setter)을 사용하면 이 부분은 자동으로 처리됩니다.

    @Embeddable
    @Data
    public static class Education {
        private String schoolKo;
        private String schoolJa;
        private String degreeKo;
        private String degreeJa;
        private String period;
    }
}