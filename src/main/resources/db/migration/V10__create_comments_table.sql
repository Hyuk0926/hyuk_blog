CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    nickname VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    user_ip VARCHAR(45) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
); 