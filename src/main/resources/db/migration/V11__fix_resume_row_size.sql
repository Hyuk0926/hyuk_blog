-- Fix MySQL row size limit issue by changing VARCHAR(2000) columns to TEXT
ALTER TABLE resume MODIFY COLUMN introduction TEXT;
ALTER TABLE resume MODIFY COLUMN introductionKo TEXT;
ALTER TABLE resume MODIFY COLUMN studentLifeKo TEXT;
ALTER TABLE resume MODIFY COLUMN studentLifeJa TEXT;
ALTER TABLE resume MODIFY COLUMN strengthsWeaknessesKo TEXT;
ALTER TABLE resume MODIFY COLUMN strengthsWeaknessesJa TEXT;
ALTER TABLE resume MODIFY COLUMN effortExperienceKo TEXT;
ALTER TABLE resume MODIFY COLUMN effortExperienceJa TEXT;
ALTER TABLE resume MODIFY COLUMN japanItMotivationKo TEXT;
ALTER TABLE resume MODIFY COLUMN japanItMotivationJa TEXT;
ALTER TABLE resume MODIFY COLUMN futurePlanKo TEXT;
ALTER TABLE resume MODIFY COLUMN futurePlanJa TEXT; 