-- Fix MySQL row size limit issue by changing VARCHAR columns to TEXT
-- Note: This migration is safe to run multiple times as it only modifies column types
ALTER TABLE resume MODIFY COLUMN introduction_ko TEXT;
ALTER TABLE resume MODIFY COLUMN introduction_ja TEXT;
ALTER TABLE resume MODIFY COLUMN student_life_ko TEXT;
ALTER TABLE resume MODIFY COLUMN student_life_ja TEXT;
ALTER TABLE resume MODIFY COLUMN strengths_weaknesses_ko TEXT;
ALTER TABLE resume MODIFY COLUMN strengths_weaknesses_ja TEXT;
ALTER TABLE resume MODIFY COLUMN effort_experience_ko TEXT;
ALTER TABLE resume MODIFY COLUMN effort_experience_ja TEXT;
ALTER TABLE resume MODIFY COLUMN japan_it_motivation_ko TEXT;
ALTER TABLE resume MODIFY COLUMN japan_it_motivation_ja TEXT;
ALTER TABLE resume MODIFY COLUMN future_plan_ko TEXT;
ALTER TABLE resume MODIFY COLUMN future_plan_ja TEXT; 