-- SQL Statements used in login and signup
SELECT * from Users;

-- Get hashed password and salt that is stored in DB.
SELECT hashedpassword,passwordsalt FROM Users WHERE username = ?;
SELECT hashedpassword,passwordsalt FROM Users WHERE username = 'admin';

-- Register new user.
INSERT INTO Users (username,hashedpassword,passwordsalt,email,online_status) VALUES (?,?,?,?,0);

-- Update Online Status
UPDATE Users SET online_status = 1 WHERE username = ?;
UPDATE Users SET online_status = 1 WHERE username = 'admin';