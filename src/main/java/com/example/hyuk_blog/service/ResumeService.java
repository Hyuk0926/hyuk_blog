package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.ResumeDto;
import com.example.hyuk_blog.entity.Resume;
import com.example.hyuk_blog.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Transactional
    public void saveResume(ResumeDto resumeDto) {
        Resume resume = resumeRepository.findById(1L).orElse(new Resume());
        // 이력서 ID를 1로 고정
        resume.setId(1L);
        resume.setName(resumeDto.getName());
        resume.setIntroduction(resumeDto.getIntroduction());
        resume.setIntroductionJp(resumeDto.getIntroductionJp());
        resume.setEmail(resumeDto.getEmail());
        resume.setPhone(resumeDto.getPhone());
        resume.setPhotoUrl(resumeDto.getPhotoUrl());
        resume.setBirth(resumeDto.getBirth());
        resume.setAddress(resumeDto.getAddress());
        resume.setAddressJp(resumeDto.getAddressJp());
        resume.setSkills(resumeDto.getSkills());
        resume.setStudentLife(resumeDto.getStudentLife());
        resume.setStudentLifeJp(resumeDto.getStudentLifeJp());
        resume.setStrengthsWeaknesses(resumeDto.getStrengthsWeaknesses());
        resume.setStrengthsWeaknessesJp(resumeDto.getStrengthsWeaknessesJp());
        resume.setEffortExperience(resumeDto.getEffortExperience());
        resume.setEffortExperienceJp(resumeDto.getEffortExperienceJp());
        resume.setJapanItMotivation(resumeDto.getJapanItMotivation());
        resume.setJapanItMotivationJp(resumeDto.getJapanItMotivationJp());
        resume.setFuturePlan(resumeDto.getFuturePlan());
        resume.setFuturePlanJp(resumeDto.getFuturePlanJp());

        // Educations and Experiences
        // Assuming the DTO contains the full list, clear and add new
        resume.getEducations().clear();
        resume.getEducations().addAll(resumeDto.getEducations().stream().map(eduDto -> {
            com.example.hyuk_blog.entity.Education edu = new com.example.hyuk_blog.entity.Education();
            edu.setSchool(eduDto.getSchool());
            edu.setSchoolJp(eduDto.getSchoolJp());
            edu.setDegree(eduDto.getDegree());
            edu.setDegreeJp(eduDto.getDegreeJp());
            edu.setPeriod(eduDto.getPeriod());
            return edu;
        }).collect(Collectors.toList()));

        resume.getExperiences().clear();
        resume.getExperiences().addAll(resumeDto.getExperiences().stream().map(expDto -> {
            com.example.hyuk_blog.entity.Experience exp = new com.example.hyuk_blog.entity.Experience();
            exp.setCompany(expDto.getCompany());
            exp.setCompanyJp(expDto.getCompanyJp());
            exp.setPosition(expDto.getPosition());
            exp.setPositionJp(expDto.getPositionJp());
            exp.setPeriod(expDto.getPeriod());
            exp.setDescription(expDto.getDescription());
            exp.setDescriptionJp(expDto.getDescriptionJp());
            return exp;
        }).collect(Collectors.toList()));

        resumeRepository.save(resume);
    }

    @Transactional(readOnly = true)
    public ResumeDto loadResume(java.util.Locale locale) {
        return resumeRepository.findById(1L)
                .map(resume -> toDto(resume, locale))
                .orElse(new ResumeDto());
    }

    private ResumeDto toDto(Resume resume, java.util.Locale locale) {
        ResumeDto dto = new ResumeDto();
        dto.setName(resume.getName());
        dto.setEmail(resume.getEmail());
        dto.setPhone(resume.getPhone());
        dto.setPhotoUrl(resume.getPhotoUrl());
        dto.setBirth(resume.getBirth());
        dto.setSkills(resume.getSkills());

        if (locale.getLanguage().equals("ja")) {
            dto.setIntroduction(resume.getIntroductionJp());
            dto.setAddress(resume.getAddressJp());
            dto.setStudentLife(resume.getStudentLifeJp());
            dto.setStrengthsWeaknesses(resume.getStrengthsWeaknessesJp());
            dto.setEffortExperience(resume.getEffortExperienceJp());
            dto.setJapanItMotivation(resume.getJapanItMotivationJp());
            dto.setFuturePlan(resume.getFuturePlanJp());

            dto.setEducations(resume.getEducations().stream().map(edu -> {
                ResumeDto.Education eduDto = new ResumeDto.Education();
                eduDto.setSchool(edu.getSchoolJp());
                eduDto.setDegree(edu.getDegreeJp());
                eduDto.setPeriod(edu.getPeriod());
                return eduDto;
            }).collect(Collectors.toList()));

            dto.setExperiences(resume.getExperiences().stream().map(exp -> {
                ResumeDto.Experience expDto = new ResumeDto.Experience();
                expDto.setCompany(exp.getCompanyJp());
                expDto.setPosition(exp.getPositionJp());
                expDto.setPeriod(exp.getPeriod());
                expDto.setDescription(exp.getDescriptionJp());
                return expDto;
            }).collect(Collectors.toList()));
        } else {
            dto.setIntroduction(resume.getIntroduction());
            dto.setAddress(resume.getAddress());
            dto.setStudentLife(resume.getStudentLife());
            dto.setStrengthsWeaknesses(resume.getStrengthsWeaknesses());
            dto.setEffortExperience(resume.getEffortExperience());
            dto.setJapanItMotivation(resume.getJapanItMotivation());
            dto.setFuturePlan(resume.getFuturePlan());

            dto.setEducations(resume.getEducations().stream().map(edu -> {
                ResumeDto.Education eduDto = new ResumeDto.Education();
                eduDto.setSchool(edu.getSchool());
                eduDto.setDegree(edu.getDegree());
                eduDto.setPeriod(edu.getPeriod());
                return eduDto;
            }).collect(Collectors.toList()));

            dto.setExperiences(resume.getExperiences().stream().map(exp -> {
                ResumeDto.Experience expDto = new ResumeDto.Experience();
                expDto.setCompany(exp.getCompany());
                expDto.setPosition(exp.getPosition());
                expDto.setPeriod(exp.getPeriod());
                expDto.setDescription(exp.getDescription());
                return expDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }

    // 기존 loadResume() 메소드는 호환성을 위해 남겨두거나, 컨트롤러에서 항상 Locale을 전달하도록 수정합니다.
    // 여기서는 기존 메소드를 삭제하고, 컨트롤러에서 항상 Locale을 전달하는 것으로 가정합니다.
    @Transactional(readOnly = true)
    public ResumeDto loadResume() {
        return loadResume(java.util.Locale.getDefault());
    }

    private ResumeDto toDto(Resume resume) {
        ResumeDto dto = new ResumeDto();
        dto.setName(resume.getName());
        dto.setIntroduction(resume.getIntroduction());
        dto.setIntroductionJp(resume.getIntroductionJp());
        dto.setEmail(resume.getEmail());
        dto.setPhone(resume.getPhone());
        dto.setPhotoUrl(resume.getPhotoUrl());
        dto.setBirth(resume.getBirth());
        dto.setAddress(resume.getAddress());
        dto.setAddressJp(resume.getAddressJp());
        dto.setSkills(resume.getSkills());
        dto.setStudentLife(resume.getStudentLife());
        dto.setStudentLifeJp(resume.getStudentLifeJp());
        dto.setStrengthsWeaknesses(resume.getStrengthsWeaknesses());
        dto.setStrengthsWeaknessesJp(resume.getStrengthsWeaknessesJp());
        dto.setEffortExperience(resume.getEffortExperience());
        dto.setEffortExperienceJp(resume.getEffortExperienceJp());
        dto.setJapanItMotivation(resume.getJapanItMotivation());
        dto.setJapanItMotivationJp(resume.getJapanItMotivationJp());
        dto.setFuturePlan(resume.getFuturePlan());
        dto.setFuturePlanJp(resume.getFuturePlanJp());

        dto.setEducations(resume.getEducations().stream().map(edu -> {
            ResumeDto.Education eduDto = new ResumeDto.Education();
            eduDto.setSchool(edu.getSchool());
            eduDto.setSchoolJp(edu.getSchoolJp());
            eduDto.setDegree(edu.getDegree());
            eduDto.setDegreeJp(edu.getDegreeJp());
            eduDto.setPeriod(edu.getPeriod());
            return eduDto;
        }).collect(Collectors.toList()));

        dto.setExperiences(resume.getExperiences().stream().map(exp -> {
            ResumeDto.Experience expDto = new ResumeDto.Experience();
            expDto.setCompany(exp.getCompany());
            expDto.setCompanyJp(exp.getCompanyJp());
            expDto.setPosition(exp.getPosition());
            expDto.setPositionJp(exp.getPositionJp());
            expDto.setPeriod(exp.getPeriod());
            expDto.setDescription(exp.getDescription());
            expDto.setDescriptionJp(exp.getDescriptionJp());
            return expDto;
        }).collect(Collectors.toList()));

        return dto;
    }
} 