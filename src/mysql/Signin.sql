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
UPDATE Users SET online_status = 0 WHERE username = 'admin';

-- log everyone off
UPDATE Users SET online_status = 0;

-- delete TestUser
DELETE FROM Users WHERE username = 'testUserReg';

-- Shows current process to the database. Close your stuff.
SHOW PROCESSLIST;

UPDATE Users SET online_status = 0 WHERE username = 'tolnes';
UPDATE Matches SET player2 = 1 WHERE player1 = 21;
UPDATE Matches SET game_started = 1 WHERE player1 = 21;

-- For polling during opponents turn
-- SELECT player FROM Turns WHERE match_id = ? ORDER BY turn_id DESC LIMIT = 1;
SELECT player FROM Turns  WHERE match_id = 1 ORDER BY turn_id DESC LIMIT = 1;
INSERT INTO Turns (turn_id,match_id,player) VALUES (1,1,1);
INSERT INTO Turns (turn_id,match_id,player) VALUES (2,1,2);

