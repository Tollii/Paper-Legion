
-- Send turn to database
INSERT INTO Turns(turn_id,match_id, player) VALUES (?,?,?);

-- For polling during opponents turn
-- SELECT player FROM Turns WHERE match_id = ? ORDER BY turn_id DESC LIMIT = 1;
SELECT player FROM Turns  WHERE match_id = 253 ORDER BY turn_id DESC;
INSERT INTO Turns (turn_id,match_id,player) VALUES (1,1,1);
INSERT INTO Turns (turn_id,match_id,player) VALUES (2,1,2);

