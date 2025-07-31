-- V2__update_category_name.sql
-- posts_kr과 posts_jp 테이블의 category 컬럼 업데이트 (안전한 방식으로)

-- posts_kr 테이블 업데이트
UPDATE posts_kr SET category = 'BlogBuild' WHERE category = '블로그 코딩' AND category IS NOT NULL;

-- posts_jp 테이블 업데이트  
UPDATE posts_jp SET category = 'BlogBuild' WHERE category = '블로그 코딩' AND category IS NOT NULL;