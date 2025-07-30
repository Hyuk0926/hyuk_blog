-- posts 테이블이 존재하는지 확인하고, 없으면 생성
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title_ko VARCHAR(500) NOT NULL,
    title_ja VARCHAR(500),
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

-- likes 테이블 생성
CREATE TABLE likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_ip VARCHAR(45) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_post_ip (post_id, user_ip),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
); 