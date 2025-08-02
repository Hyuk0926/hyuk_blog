-- Rollback V20 changes: Remove post table and its dependencies
-- This migration undoes what V20 did

-- Step 1: Disable foreign key checks to avoid constraint issues
SET FOREIGN_KEY_CHECKS = 0;

-- Step 2: Drop foreign key constraints from posts_kr and posts_jp
ALTER TABLE posts_kr DROP FOREIGN KEY IF EXISTS FK_POST_KR_ON_POST;
ALTER TABLE posts_jp DROP FOREIGN KEY IF EXISTS FK_POST_JP_ON_POST;

-- Step 3: Drop the post table that was created by V20
DROP TABLE IF EXISTS post;

-- Step 4: Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Note: The data in posts_kr and posts_jp tables remains intact
-- Only the post table and its foreign key constraints are removed 