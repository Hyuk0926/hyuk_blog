-- V16__remove_password_and_user_ip_from_comments.sql
-- comments 테이블에서 password와 user_ip 컬럼 제거

-- password 컬럼이 존재하는 경우에만 삭제
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'comments' 
     AND COLUMN_NAME = 'password') > 0,
    'ALTER TABLE comments DROP COLUMN password',
    'SELECT "password column does not exist in comments table" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- user_ip 컬럼이 존재하는 경우에만 삭제
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'comments' 
     AND COLUMN_NAME = 'user_ip') > 0,
    'ALTER TABLE comments DROP COLUMN user_ip',
    'SELECT "user_ip column does not exist in comments table" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt; 