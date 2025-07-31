-- V2__update_category_name.sql
-- posts 테이블의 category 컬럼 업데이트 (안전한 방식으로)

-- 해당 데이터가 존재하는 경우에만 업데이트
UPDATE posts SET category = 'BlogBuild' WHERE category = '블로그 코딩' AND category IS NOT NULL;