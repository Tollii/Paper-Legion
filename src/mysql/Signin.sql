-- SQL Statements used in login and signup
SELECT * from Users;

-- Get hashed password and salt that is stored in DB.
SELECT hashedpassword,passwordsalt,online_status FROM Users WHERE username = ?;
SELECT hashedpassword,passwordsalt,online_status FROM Users WHERE username = 'admin';

-- Register new user.
INSERT INTO Users (username,hashedpassword,passwordsalt,email,online_status) VALUES (?,?,?,?,0);


-- Update Online Status --

-- login
-- UPDATE Users SET online_status = 1 WHERE username = ?;
UPDATE  Users SET online_status = 1 WHERE username = 'admin';

-- log off
-- UPDATE Users SET online_status = 0 WHERE username = ?;
UPDATE Users SET online_status = 0 WHERE username = 'thomas2';

-- log everyone off
UPDATE Users SET online_status = 0;

-- delete TestUser
DELETE FROM Users WHERE username = 'testUserReg';

-- Shows current process to the database. Close your stuff.
SHOW PROCESSLIST;

UPDATE Users SET online_status = 0 WHERE username = 'tolnes';
UPDATE Matches SET player2 = 1 WHERE player1 = 30;
UPDATE Matches SET game_started = 1 WHERE player1 = 30;

-- create stats for user
INSERT INTO Statistics(user_id, games_won, games_played) VALUES(?,?,?);
INSERT INTO Statistics(user_id, games_won, games_played) VALUES(26,0,0);

-- increments games
update Statistics set games_played=games_played + 1 where user_id = ?;
update Statistics set games_played=games_played + 1 where user_id = 1;

-- increments games won
update Statistics set games_won=games_won + 1 where user_id = ?;
update Statistics set games_won=games_won + 1 where user_id = 1;



