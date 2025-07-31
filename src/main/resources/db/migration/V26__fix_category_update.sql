-- V26__fix_category_update.sql
-- V2 마이그레이션의 실패를 해결하기 위한 새로운 마이그레이션
-- posts_kr과 posts_jp 테이블의 category 컬럼 업데이트

-- posts_kr 테이블 업데이트 (안전한 방식)
UPDATE posts_kr SET category = 'BlogBuild' 
WHERE category = '블로그 코딩' AND category IS NOT NULL;

-- posts_jp 테이블 업데이트 (안전한 방식)
UPDATE posts_jp SET category = 'BlogBuild' 
WHERE category = '블로그 코딩' AND category IS NOT NULL; 