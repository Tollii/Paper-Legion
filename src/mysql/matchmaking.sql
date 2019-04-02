-- Creates a game --
insert into Matches(match_id, player1, player2, game_started) values (default,1,null,0);

-- Join game --
update Matches
set player2=?, game_started=1 where match_id=?;


-- Deletes game if abort --
delete from Matches where player1=2;



select Pieces.piece_id, Pieces.match_id, Pieces.player_id,position_x, position_y, unit_type_id, current_health from Pieces
join Units U on Pieces.piece_id = U.piece_id and Pieces.match_id = U.match_id and Pieces.player_id = U.player_id
where Pieces.match_id=317;

SELECT COUNT(*) FROM Units WHERE match_id = 342;
