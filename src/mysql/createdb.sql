# noinspection SyntaxErrorForFile

# noinspection SyntaxErrorForFile

# noinspection SyntaxErrorForFile

# noinspection SyntaxErrorForFile

SET foreign_key_checks = 0;
DROP TABLE IF EXISTS Users, Statistics, Unit_types, Matches, Turns, Pieces, Obstacles, Units, Movements, Attacks, Abilities;
SET foreign_key_checks = 1;

CREATE TABLE Users(
  user_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username varchar(40) NOT NULL,
  hashedpassword varchar(40) NOT NULL,
  passwordsalt varchar(40) NOT NULL,
  email varchar(40) NOT NULL,
  online_status BOOLEAN NOT NULL
);

CREATE TABLE Stats(
  user_id int NOT NULL PRIMARY KEY,
  games_won int NOT NULL DEFAULT 0,
  games_played int NOT NULL DEFAULT 0,
  FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Unit_types(
  unit_type_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  unit_name varchar(20) NOT NULL,
  max_health int NOT NULL,
  attack int NOT NULL,
  defence_multiplier float NOT NULL,
  min_attack_range int NOT NULL,
  max_attack_range int NOT NULL,
  movement_range int NOT NULL,
  cost int NOT NULL
);

CREATE TABLE Matches(
  match_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  player1 int NOT NULL,
  player2 int,
  game_started BOOLEAN NOT NULL DEFAULT 0,
  surrendered int,
  player1_ready TINYINT(1) NULL DEFAULT NULL,
  player2_ready TINYINT(1) NULL DEFAULT NULL,
  obstacle_amount int NULL DEFAULT NULL,
  password varchar(25) NULL DEFAULT NULL,
  FOREIGN KEY(player1) REFERENCES Users(user_id),
  FOREIGN KEY(player2) REFERENCES Users(user_id)
);

-- ALTER TABLE `Matches` ADD `player1_ready` TINYINT(1) NULL DEFAULT NULL AFTER `surrendered`,
 -- ADD `player2_ready` TINYINT(1) NULL DEFAULT NULL AFTER `player1_ready`;


CREATE TABLE Turns(
  turn_id int NOT NULL,
  match_id int NOT NULL,
  player int NOT NULL,
  PRIMARY KEY(turn_id, match_id, player),
  FOREIGN KEY(match_id) REFERENCES Matches(match_id) ON DELETE CASCADE,
  FOREIGN KEY(player) REFERENCES Users(user_id)
);

CREATE TABLE Pieces(
  piece_id int NOT NULL,
  match_id int NOT NULL,
  player int NOT NULL,
  position_x int NULL DEFAULT NULL,
  position_y int NULL DEFAULT NULL,
  PRIMARY KEY(piece_id, match_id, player),
  FOREIGN KEY(match_id) REFERENCES Matches(match_id) ON DELETE CASCADE,
  FOREIGN KEY(player) REFERENCES Users(user_id)
);

CREATE TABLE Obstacles(
  obstacle_id int NOT NULL,
  match_id int NOT NULL,
  position_x int NULL DEFAULT NULL,
  position_y int NULL DEFAULT NULL,
  PRIMARY KEY(obstacle_id, match_id),
  FOREIGN KEY(match_id) REFERENCES Matches(match_id) ON DELETE CASCADE
);

CREATE TABLE Units(
  piece_id int NOT NULL,
  match_id int NOT NULL,
  player_id int NOT NULL,
  current_health double NOT NULL,
  unit_type_id int NOT NULL,
  PRIMARY KEY(piece_id, match_id, player_id),
  FOREIGN KEY(piece_id, match_id, player_io) REFERENCES Pieces(piece_id, match_id, player) ON DELETE CASCADE,
  FOREIGN KEY(unit_type_id) REFERENCES Unit_types(unit_type_id)
);

CREATE TABLE Movements(
  turn_id int NOT NULL,
  piece_id int NOT NULL,
  match_id int NOT NULL,
  start_position_x int NULL DEFAULT NULL,
  start_position_y int NULL DEFAULT NULL,
  end_position_x int NULL DEFAULT NULL,
  end_position_y int NULL DEFAULT NULL,
  PRIMARY KEY(turn_id, piece_id, match_id),
  FOREIGN KEY(piece_id) REFERENCES Units(piece_id) ON DELETE CASCADE,
  FOREIGN KEY(turn_id, match_id) REFERENCES Turns(turn_id, match_id) ON DELETE CASCADE
);

CREATE TABLE Attacks(
  turn_id int NOT NULL,
  attacking_player_id int NOT NULL,
  attacker_piece_id int NOT NULL,
  receiver_piece_id int NOT NULL,
  match_id int NOT NULL,
  damage_dealt int NOT NULL,
  PRIMARY KEY(turn_id, attacking_player_id, attacking_player_id, attacker_piece_id, match_id),
  FOREIGN KEY(attacking_player_id) REFERENCES Users(user_id),
  FOREIGN KEY(attacker_piece_id) REFERENCES Units(piece_id) ON DELETE CASCADE,
  FOREIGN KEY(receiver_piece_id) REFERENCES Units(piece_id) ON DELETE CASCADE,
  FOREIGN KEY(turn_id, match_id) REFERENCES Turns(turn_id, match_id) ON DELETE CASCADE
);
