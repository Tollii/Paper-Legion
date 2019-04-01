
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



select Pieces.piece_id, Pieces.match_id, Pieces.player_id,position_x, position_y, unit_type_id, current_health from Pieces
right join Units U on Pieces.piece_id = U.piece_id and Pieces.match_id = U.match_id and Pieces.player_id = U.player_id
where Pieces.match_id=345
Order by piece_id,player_id;

-- obstacles
insert into Obstacles(obstacle_id, match_id, position_x, position_y) values(?,?,?,?);
insert into Obstacles(obstacle_id, match_id, position_x, position_y) values(2,295,1,1);

ALTER TABLE Obstacles DROP FOREIGN KEY piece_id;

ALTER TABLE Obstacles DROP INDEX obstacle_id;

ALTER TABLE Obstacles ADD FOREIGN KEY(match_id) REFERENCES Matches(match_id) ON DELETE CASCADE;