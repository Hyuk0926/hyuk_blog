-- V15__remove_user_ip_from_likes.sql
-- likes 테이블에서 user_ip 컬럼 제거

-- user_ip 컬럼이 존재하는 경우에만 삭제
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'likes' 
     AND COLUMN_NAME = 'user_ip') > 0,
    'ALTER TABLE likes DROP COLUMN user_ip',
    'SELECT "user_ip column does not exist in likes table" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt; 