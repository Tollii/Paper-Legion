select * from Matches

-- Creates a game --
insert into Matches(match_id, player1, player2, game_started) values (default,1,null,0);

-- Join game --
update Matches
set player2=?, game_started=1 where match_id=?;


-- Deletes game if abort --
delete from Matches where player1=2;


