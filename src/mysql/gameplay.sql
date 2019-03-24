
-- Send turn to database
INSERT INTO Turns(turn_id,match_id, player) VALUES (?,?,?);

-- For polling during opponents turn
-- SELECT player FROM Turns WHERE match_id = ? ORDER BY turn_id DESC LIMIT = 1;
SELECT player FROM Turns  WHERE match_id = 253 ORDER BY turn_id DESC;
INSERT INTO Turns (turn_id,match_id,player) VALUES (1,1,1);
INSERT INTO Turns (turn_id,match_id,player) VALUES (2,1,2);


-- Update units health
UPDATE Units SET current_health = ? WHERE piece_id = ? AND match_id = ? AND player_id = ?;

-- Update unit position
UPDATE Pieces SET position_x = ?, position_y = ? WHERE match_id=? AND piece_id = ? AND player_id = ?;