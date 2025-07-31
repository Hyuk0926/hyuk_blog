-- V17__add_user_id_to_comments_and_likes.sql
-- comments와 likes 테이블에 user_id 컬럼 추가

-- comments 테이블에 user_id 컬럼 추가 (이미 존재하는 경우 무시)
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'comments' 
     AND COLUMN_NAME = 'user_id') = 0,
    'ALTER TABLE comments ADD COLUMN user_id BIGINT',
    'SELECT "user_id column already exists in comments table" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- likes 테이블에 user_id 컬럼 추가 (이미 존재하는 경우 무시)
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'likes' 
     AND COLUMN_NAME = 'user_id') = 0,
    'ALTER TABLE likes ADD COLUMN user_id BIGINT',
    'SELECT "user_id column already exists in likes table" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 외래키 제약조건 추가 (이미 존재하는 경우 무시)
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'comments' 
     AND COLUMN_NAME = 'user_id' 
     AND REFERENCED_TABLE_NAME = 'users') = 0,
    'ALTER TABLE comments ADD CONSTRAINT fk_comments_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL',
    'SELECT "foreign key constraint already exists for comments.user_id" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'likes' 
     AND COLUMN_NAME = 'user_id' 
     AND REFERENCED_TABLE_NAME = 'users') = 0,
    'ALTER TABLE likes ADD CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL',
    'SELECT "foreign key constraint already exists for likes.user_id" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt; 