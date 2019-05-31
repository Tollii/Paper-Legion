create table Unit_types
(
  unit_type_id       int auto_increment
    primary key,
  unit_name          varchar(20) not null,
  max_health         int         not null,
  attack             int         not null,
  defence_multiplier float(4, 2) null,
  min_attack_range   int         null,
  max_attack_range   int         null,
  movement_range     int         null,
  cost               int         not null
);

create table Users
(
  user_id        int auto_increment
    primary key,
  username       varchar(40)   not null,
  hashedpassword varbinary(40) not null,
  passwordsalt   varbinary(40) not null,
  email          varchar(40)   not null,
  online_status  tinyint(1)    not null,
  constraint username
    unique (username)
);

create table Matches
(
  match_id        int auto_increment
    primary key,
  player1         int                  not null,
  player2         int                  null,
  game_started    tinyint(1) default 0 not null,
  surrendered     int                  null,
  player1_ready   tinyint(1)           null,
  player2_ready   tinyint(1)           null,
  obstacle_amount int                  null,
  password        varchar(25)          null,
  constraint Matches_ibfk_1
    foreign key (player1) references Users (user_id),
  constraint Matches_ibfk_2
    foreign key (player2) references Users (user_id)
);

create index player1
  on Matches (player1);

create index player2
  on Matches (player2);

create table Obstacles
(
  obstacle_id int not null,
  match_id    int not null,
  position_x  int null,
  position_y  int null,
  primary key (obstacle_id, match_id),
  constraint Obstacles_ibfk_1
    foreign key (match_id) references Matches (match_id)
      on delete cascade
);

create index match_id
  on Obstacles (match_id);

create table Pieces
(
  piece_id   int not null,
  match_id   int not null,
  player_id  int not null,
  position_x int null,
  position_y int null,
  primary key (piece_id, match_id, player_id),
  constraint Pieces_ibfk_1
    foreign key (match_id) references Matches (match_id)
      on delete cascade,
  constraint Pieces_ibfk_2
    foreign key (player_id) references Users (user_id)
);

create index match_id
  on Pieces (match_id);

create index player_id
  on Pieces (player_id);

create table Pieces2
(
  piece_id   int not null,
  match_id   int not null,
  position_x int not null,
  position_y int not null,
  primary key (piece_id, match_id),
  constraint Pieces2_ibfk_1
    foreign key (piece_id, match_id) references Obstacles (obstacle_id, match_id)
);

create table Statistics
(
  user_id      int           not null
    primary key,
  games_won    int default 0 not null,
  games_played int default 0 not null,
  constraint Statistics_ibfk_1
    foreign key (user_id) references Users (user_id)
      on delete cascade
);

create table Turns
(
  turn_id  int not null,
  match_id int not null,
  player   int not null,
  primary key (turn_id, match_id, player),
  constraint Turns_ibfk_1
    foreign key (match_id) references Matches (match_id)
      on delete cascade,
  constraint Turns_ibfk_2
    foreign key (player) references Users (user_id)
);

create index match_id
  on Turns (match_id);

create table Units
(
  piece_id       int    not null,
  match_id       int    not null,
  player_id      int    not null,
  current_health double not null,
  unit_type_id   int    not null,
  primary key (piece_id, match_id, player_id),
  constraint Units___fk
    foreign key (player_id) references Pieces (player_id)
      on delete cascade,
  constraint Units_ibfk_1
    foreign key (piece_id, match_id) references Pieces (piece_id, match_id)
      on delete cascade,
  constraint Units_ibfk_2
    foreign key (unit_type_id) references Unit_types (unit_type_id)
);

create table Attacks
(
  turn_id             int not null,
  attacking_player_id int not null,
  attacker_piece_id   int not null,
  receiver_piece_id   int not null,
  match_id            int not null,
  damage_dealt        int not null,
  primary key (turn_id, attacking_player_id, attacker_piece_id, receiver_piece_id, match_id),
  constraint Attacks_ibfk_1
    foreign key (attacking_player_id) references Users (user_id),
  constraint Attacks_ibfk_2
    foreign key (attacker_piece_id) references Units (piece_id)
      on delete cascade,
  constraint Attacks_ibfk_3
    foreign key (receiver_piece_id) references Units (piece_id)
      on delete cascade,
  constraint Attacks_ibfk_4
    foreign key (turn_id, match_id) references Turns (turn_id, match_id)
      on delete cascade
);

create index attacking_player_id
  on Attacks (attacking_player_id);

create index turn_id
  on Attacks (turn_id, match_id);

create table Movements
(
  turn_id     int not null,
  piece_id    int not null,
  match_id    int not null,
  start_pos_x int null,
  start_pos_y int null,
  end_pos_x   int null,
  end_pos_y   int null,
  primary key (turn_id, piece_id, match_id),
  constraint Movements_ibfk_1
    foreign key (piece_id) references Units (piece_id)
      on delete cascade,
  constraint Movements_ibfk_2
    foreign key (turn_id, match_id) references Turns (turn_id, match_id)
      on delete cascade
);

create index turn_id
  on Movements (turn_id, match_id);

create index unit_type_id
  on Units (unit_type_id);

INSERT INTO Unit_types (unit_type_id, unit_name, max_health, attack, defence_multiplier, min_attack_range, max_attack_range, movement_range, cost) VALUES (1, 'Swordsman', 90, 50, 1, 1, 1, 2, 200);
INSERT INTO Unit_types (unit_type_id, unit_name, max_health, attack, defence_multiplier, min_attack_range, max_attack_range, movement_range, cost) VALUES (2, 'Archer', 60, 50, 1, 2, 3, 1, 200);
INSERT INTO Unit_types (unit_type_id, unit_name, max_health, attack, defence_multiplier, min_attack_range, max_attack_range, movement_range, cost) VALUES (3, 'Juggernaut', 120, 50, 2, 1, 1, 1, 250);
INSERT INTO Unit_types (unit_type_id, unit_name, max_health, attack, defence_multiplier, min_attack_range, max_attack_range, movement_range, cost) VALUES (4, 'Catapult', 50, 50, 1, 4, 8, 1, 350);