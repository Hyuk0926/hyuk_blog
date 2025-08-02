-- V18__drop_unused_tables.sql

-- 외래 키 제약조건 검사를 일시적으로 비활성화
SET FOREIGN_KEY_CHECKS = 0;

-- 사용하지 않는 테이블들 삭제
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS resume_educations;
DROP TABLE IF EXISTS resume_experiences;

-- 외래 키 제약조건 검사를 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;