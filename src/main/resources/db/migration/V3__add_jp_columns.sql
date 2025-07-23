ALTER TABLE posts ADD COLUMN title_jp VARCHAR(200);
ALTER TABLE posts ADD COLUMN summary_jp VARCHAR(500);
ALTER TABLE posts ADD COLUMN content_jp TEXT;

ALTER TABLE resume ADD COLUMN introduction_jp TEXT;
ALTER TABLE resume ADD COLUMN address_jp VARCHAR(255);
ALTER TABLE resume ADD COLUMN student_life_jp TEXT;
ALTER TABLE resume ADD COLUMN strengths_weaknesses_jp TEXT;
ALTER TABLE resume ADD COLUMN effort_experience_jp TEXT;
ALTER TABLE resume ADD COLUMN japan_it_motivation_jp TEXT;
ALTER TABLE resume ADD COLUMN future_plan_jp TEXT;

ALTER TABLE resume_educations ADD COLUMN school_jp VARCHAR(255);
ALTER TABLE resume_educations ADD COLUMN degree_jp VARCHAR(255);

ALTER TABLE resume_experiences ADD COLUMN company_jp VARCHAR(255);
ALTER TABLE resume_experiences ADD COLUMN position_jp VARCHAR(255);
ALTER TABLE resume_experiences ADD COLUMN description_jp VARCHAR(255);
