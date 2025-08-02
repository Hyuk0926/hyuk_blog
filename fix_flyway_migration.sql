-- Flyway 마이그레이션 실패 문제 해결 스크립트
-- 이 스크립트는 실패한 V2 마이그레이션을 성공으로 표시합니다.

-- 1. Flyway 스키마 히스토리 테이블 확인
SELECT * FROM flyway_schema_history WHERE version = '2';

-- 2. 실패한 V2 마이그레이션을 성공으로 업데이트
UPDATE flyway_schema_history 
SET success = 1, 
    installed_on = CURRENT_TIMESTAMP,
    execution_time = 0,
    checksum = NULL
WHERE version = '2' AND success = 0;

-- 3. 업데이트 결과 확인
SELECT * FROM flyway_schema_history WHERE version = '2';

-- 4. 현재 posts_kr과 posts_jp 테이블의 category 컬럼 상태 확인
SELECT 'posts_kr' as table_name, category, COUNT(*) as count 
FROM posts_kr 
WHERE category = '블로그 코딩' 
GROUP BY category;

SELECT 'posts_jp' as table_name, category, COUNT(*) as count 
FROM posts_jp 
WHERE category = '블로그 코딩' 
GROUP BY category;

-- 5. 수동으로 category 업데이트 (필요한 경우)
UPDATE posts_kr SET category = 'BlogBuild' WHERE category = '블로그 코딩';
UPDATE posts_jp SET category = 'BlogBuild' WHERE category = '블로그 코딩'; 