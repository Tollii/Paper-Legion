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
  attack_range int NOT NULL,
  ability_cooldown int,
  cost int NOT NULL
);

CREATE TABLE Matches(
  match_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  player1 int NOT NULL,
  player2 int,
  game_started BOOLEAN NOT NULL DEFAULT 0,
  FOREIGN KEY(player1) REFERENCES Users(user_id),
  FOREIGN KEY(player2) REFERENCES Users(user_id)
);
--legg inn dette
ALTER TABLE `Matches` ADD `player1_ready` TINYINT(1) NULL DEFAULT NULL AFTER `surrendered`,
 ADD `player2_ready` TINYINT(1) NULL DEFAULT NULL AFTER `player1_ready`;

CREATE TABLE Turns(
  turn_id int NOT NULL,
  match_id int NOT NULL,
  player int NOT NULL,
  PRIMARY KEY(turn_id, match_id, player),
  FOREIGN KEY(match_id) REFERENCES Matches(match_id) ON DELETE CASCADE
  FOREIGN KEY(player) REFERENCES Users(user_id);
);

CREATE TABLE Pieces(
  piece_id int NOT NULL,
  match_id int NOT NULL,
  position point NOT NULL,
  player int NOT NULL,
  PRIMARY KEY(piece_id, match_id),
  FOREIGN KEY(match_id) REFERENCES Matches(match_id) ON DELETE CASCADE
);

CREATE TABLE Obstacles(
  piece_id int NOT NULL,
  match_id int NOT NULL,
  PRIMARY KEY(piece_id, match_id),
  FOREIGN KEY(piece_id, match_id) REFERENCES Pieces(piece_id, match_id) ON DELETE CASCADE
);

CREATE TABLE Units(
  piece_id int NOT NULL,
  match_id int NOT NULL,
  current_health double NOT NULL,
  current_attack int NOT NULL,
  current_attack_range int NOT NULL,
  current_ability_cooldown int,
  unit_type_id int NOT NULL,
  PRIMARY KEY(piece_id, match_id),
  FOREIGN KEY(piece_id, match_id) REFERENCES Pieces(piece_id, match_id) ON DELETE CASCADE,
  FOREIGN KEY(unit_type_id) REFERENCES Unit_types(unit_type_id)
);

CREATE TABLE Movements(
  turn_id int NOT NULL,
  piece_id int NOT NULL,
  match_id int NOT NULL,
  start_pos point NOT NULL,
  end_pos point NOT NULL,
  PRIMARY KEY(turn_id, piece_id, match_id),
  FOREIGN KEY(piece_id) REFERENCES Units(piece_id),
  FOREIGN KEY(turn_id, match_id) REFERENCES Turns(turn_id, match_id) ON DELETE CASCADE
);

CREATE TABLE Attacks(
  turn_id int NOT NULL,
  attacking_player_id int NOT NULL,
  attacker int NOT NULL,
  reciever int NOT NULL,
  match_id int NOT NULL,
  damage_dealt int NOT NULL,
  PRIMARY KEY(turn_id, attacking_player_id, attacker, reciever, match_id),
  FOREIGN KEY(attacking_player_id) REFERENCES Users(user_id),
  FOREIGN KEY(attacker) REFERENCES Units(piece_id),
  FOREIGN KEY(reciever) REFERENCES Units(piece_id),
  FOREIGN KEY(turn_id, match_id) REFERENCES Turns(turn_id, match_id) ON DELETE CASCADE
);

CREATE TABLE Abilities(
  turn_id int NOT NULL,
  piece_id int NOT NULL,
  match_id int NOT NULL,
  PRIMARY KEY(turn_id, piece_id, match_id),
  FOREIGN KEY(piece_id) REFERENCES Units(piece_id),
  FOREIGN KEY(turn_id, match_id) REFERENCES Turns(turn_id, match_id) ON DELETE CASCADE
);
