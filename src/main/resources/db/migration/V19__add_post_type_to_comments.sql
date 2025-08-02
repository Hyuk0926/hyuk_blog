-- Add encrypted_id column to Post tables (nullable first, then make it not null after data migration)
ALTER TABLE posts_kr ADD COLUMN encrypted_id VARCHAR(255);
ALTER TABLE posts_jp ADD COLUMN encrypted_id VARCHAR(255);

-- Generate encrypted IDs for existing posts using KR_id_title and JP_id_title format
-- For Korean posts: KR_id_title -> Base64 (안전한 제목 처리)
UPDATE posts_kr SET encrypted_id = TO_BASE64(CONCAT('KR_', id, '_', 
    COALESCE(
        REGEXP_REPLACE(
            REGEXP_REPLACE(
                REGEXP_REPLACE(title, '[^a-zA-Z0-9]', '_'),
                '_+', '_'
            ),
            '^_|_$', ''
        ), 
        'untitled'
    ))) 
WHERE encrypted_id IS NULL;

-- For Japanese posts: JP_id_title -> Base64 (안전한 제목 처리)
UPDATE posts_jp SET encrypted_id = TO_BASE64(CONCAT('JP_', id, '_', 
    COALESCE(
        REGEXP_REPLACE(
            REGEXP_REPLACE(
                REGEXP_REPLACE(title, '[^a-zA-Z0-9]', '_'),
                '_+', '_'
            ),
            '^_|_$', ''
        ), 
        'untitled'
    ))) 
WHERE encrypted_id IS NULL;

-- Now make encrypted_id columns NOT NULL and UNIQUE
ALTER TABLE posts_kr ALTER COLUMN encrypted_id SET NOT NULL;
ALTER TABLE posts_jp ALTER COLUMN encrypted_id SET NOT NULL;
ALTER TABLE posts_kr ADD CONSTRAINT uk_posts_kr_encrypted_id UNIQUE (encrypted_id);
ALTER TABLE posts_jp ADD CONSTRAINT uk_posts_jp_encrypted_id UNIQUE (encrypted_id);

-- Add post_encrypted_id column to comments table
ALTER TABLE comments ADD COLUMN post_encrypted_id VARCHAR(255);

-- Add post_encrypted_id column to likes table
ALTER TABLE likes ADD COLUMN post_encrypted_id VARCHAR(255);

-- Update existing comments to use encrypted IDs (assuming they are for Korean posts)
UPDATE comments c 
JOIN posts_kr p ON c.post_id = p.id 
SET c.post_encrypted_id = p.encrypted_id 
WHERE c.post_encrypted_id IS NULL;

-- Update existing likes to use encrypted IDs (assuming they are for Korean posts)
UPDATE likes l 
JOIN posts_kr p ON l.post_id = p.id 
SET l.post_encrypted_id = p.encrypted_id 
WHERE l.post_encrypted_id IS NULL;

-- Now make post_encrypted_id columns NOT NULL
ALTER TABLE comments ALTER COLUMN post_encrypted_id SET NOT NULL;
ALTER TABLE likes ALTER COLUMN post_encrypted_id SET NOT NULL;

-- Remove old post_id column from comments table
ALTER TABLE comments DROP COLUMN post_id;

-- Remove old post_id column from likes table
ALTER TABLE likes DROP COLUMN post_id;

-- Add indexes for better performance
CREATE INDEX idx_comments_post_encrypted_id ON comments(post_encrypted_id);
CREATE INDEX idx_likes_post_encrypted_id ON likes(post_encrypted_id);
CREATE INDEX idx_posts_kr_encrypted_id ON posts_kr(encrypted_id);
CREATE INDEX idx_posts_jp_encrypted_id ON posts_jp(encrypted_id); 