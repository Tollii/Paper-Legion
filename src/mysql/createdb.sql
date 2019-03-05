DROP TABLE IF EXISTS Users, Statistics, Unit_types, Matches, Turns, Pieces, Obstacles, Units, Movements, Attcks, Abilities;

CREATE TABLE Users(
  user_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username varchar(40) NOT NULL,
  hashedpassword varchar(40) NOT NULL,
  salt varchar(40) NOT NULL,
  email varchar(40) NOT NULL,
  online_status BOOLEAN NOT NULL
);

CREATE TABLE Statistics(
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
  ability_cooldown int
);

CREATE TABLE Matches(
  match_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  player1 int NOT NULL,
  player2 int,
  game_started BOOLEAN NOT NULL,
  FOREIGN KEY(player1) REFERENCES Users(user_id),
  FOREIGN KEY(player2) REFERENCES Users(user_id)
);

CREATE TABLE Turns(
  turn_id int NOT NULL,
  match_id int NOT NULL,
  player int NOT NULL,
  PRIMARY KEY(turn_id, match_id, player),
  FOREIGN KEY(match_id) REFERENCES Matches(match_id)
);

CREATE TABLE Pieces(
  piece_id int NOT NULL,
  match_id int NOT NULL,
  position point NOT NULL,
  player int NOT NULL,
  PRIMARY KEY(piece_id, match_id),
  FOREIGN KEY(match_id) REFERENCES Matches(match_id)
);

CREATE TABLE Obstacles(
  piece_id int NOT NULL PRIMARY KEY,
  FOREIGN KEY(piece_id) REFERENCES Pieces(piece_id)
);

CREATE TABLE Units(
  piece_id int NOT NULL PRIMARY KEY,
  current_health int NOT NULL,
  current_attack int NOT NULL,
  current_attack_range int NOT NULL,
  current_ability_cooldown int NOT NULL,
  unit_type_id int NOT NULL,
  FOREIGN KEY(piece_id) REFERENCES Pieces(piece_id),
  FOREIGN KEY(unit_type_id) REFERENCES Unit_types(unit_type_id)
);

CREATE TABLE Movements(
  turn_id int NOT NULL,
  piece_id int NOT NULL,
  match_id int NOT NULL,
  start_pos point NOT NULL,
  end_pos point NOT NULL,
  PRIMARY KEY(turn_id, piece_id, match_id),
  FOREIGN KEY(turn_id, match_id) REFERENCES Turns(turn_id, match_id)
);

CREATE TABLE Attacks(
  turn_id int NOT NULL,
  piece_id int NOT NULL,
  match_id int NOT NULL,
  damage_dealt int NOT NULL,
  PRIMARY KEY(turn_id, piece_id, match_id),
  FOREIGN KEY(turn_id, match_id) REFERENCES Turns(turn_id, match_id)
);

CREATE TABLE Abilities(
  turn_id int NOT NULL,
  piece_id int NOT NULL,
  match_id int NOT NULL,
  PRIMARY KEY(turn_id, piece_id, match_id),
  FOREIGN KEY(turn_id, match_id) REFERENCES Turns(turn_id, match_id)
);
