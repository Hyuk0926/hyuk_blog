-- Add existing admin accounts as regular users for comment functionality
-- This allows admin accounts to also function as regular users for commenting

-- Insert admin accounts as users (if they don't already exist)
-- Using a safer approach that works with both MySQL and H2
INSERT INTO users (username, password, nickname, email, active, created_at, updated_at)
SELECT 
    username,
    password,
    CASE WHEN name IS NULL OR name = '' THEN username ELSE name END as nickname,
    email,
    active,
    created_at,
    updated_at
FROM admins
WHERE active = true
AND NOT EXISTS (
    SELECT 1 FROM users u WHERE u.username = admins.username
); 