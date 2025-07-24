-- V1__create_initial_tables.sql

-- admins 테이블 생성 (기존 Admin.java 엔티티 기반)
CREATE TABLE admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    email VARCHAR(200),
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6)
);

-- posts 테이블 생성 (기존 Post.java 엔티티 기반, 다국어 필드 포함)
CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title_ko VARCHAR(200) NOT NULL,
    title_ja VARCHAR(200),
    summary_ko VARCHAR(500),
    summary_ja VARCHAR(500),
    content_ko TEXT,
    content_ja TEXT,
    image_url VARCHAR(200),
    published BOOLEAN NOT NULL,
    category VARCHAR(50),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6)
);

-- inquiry 테이블 생성 (기존 Inquiry.java 엔티티 기반)
CREATE TABLE inquiry (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    subject VARCHAR(255),
    message VARCHAR(255),
    created_at TIMESTAMP(6)
);

CREATE TABLE resume (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    introduction_ko TEXT,
    introduction_ja TEXT,
    email VARCHAR(255),
    phone VARCHAR(255),
    photo_url VARCHAR(255),
    birth VARCHAR(255),
    address VARCHAR(255),
    skills VARCHAR(255),
    student_life_ko TEXT,
    student_life_ja TEXT,
    strengths_weaknesses_ko TEXT,
    strengths_weaknesses_ja TEXT,
    effort_experience_ko TEXT,
    effort_experience_ja TEXT,
    japan_it_motivation_ko TEXT,
    japan_it_motivation_ja TEXT,
    future_plan_ko TEXT,
    future_plan_ja TEXT
);

-- resume_educations 테이블 생성 (Resume.java의 @ElementCollection 기반)
CREATE TABLE resume_educations (
    resume_id BIGINT NOT NULL,
    educations VARCHAR(255), -- 이 필드는 실제 데이터에 따라 정확한 타입과 길이가 필요합니다.
    degree VARCHAR(255),
    period VARCHAR(255),
    school VARCHAR(255),
    FOREIGN KEY (resume_id) REFERENCES resume(id)
);

-- resume_experiences 테이블 생성 (Resume.java의 @ElementCollection 기반)
CREATE TABLE resume_experiences (
    resume_id BIGINT NOT NULL,
    experiences VARCHAR(255), -- 이 필드는 실제 데이터에 따라 정확한 타입과 길이가 필요합니다.
    company VARCHAR(255),
    description VARCHAR(255),
    period VARCHAR(255),
    position VARCHAR(255),
    FOREIGN KEY (resume_id) REFERENCES resume(id)
);

-- visitor 테이블 생성 (기존 Visitor.java 엔티티 기반)
CREATE TABLE visitor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    count INT NOT NULL
);