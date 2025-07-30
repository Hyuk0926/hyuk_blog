-- 한국어 포스트 테이블 생성
CREATE TABLE posts_kr (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500),
    summary VARCHAR(500),
    content TEXT,
    image_url VARCHAR(200),
    published BOOLEAN NOT NULL DEFAULT FALSE,
    category VARCHAR(50),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6)
);

-- 일본어 포스트 테이블 생성
CREATE TABLE posts_jp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500),
    summary VARCHAR(500),
    content TEXT,
    image_url VARCHAR(200),
    published BOOLEAN NOT NULL DEFAULT FALSE,
    category VARCHAR(50),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6)
); 