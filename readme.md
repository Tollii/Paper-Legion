# Paper Legion
This is a project for Software Engineering 1 with Database Project Assignment made by Team 17 in Spring 2019 at NTNU.
The game is a Player vs Player grid based game. It is a multiplayer game which synchronizes game data over a database.

Team 17 Members: 

Joakim Moe Adolfsen,
Jon Åby Bergquist,
Thomas Bakken Moe,
Andreas Tolnes,
Kristoffer Vanebo,
Eric Younger.

## Dependencies
 - Java 8 with JavaFX
 - jfoenix component library
 - mysql-connector-java

## Installation manual:
```markdown
1. Create tables in MySql database using the 'paperLegion.sql' file in the folder 'mysql'
2. Alter the config file <database/Config.java> and insert jdbc driver and url.
3. Add properties file named 'DatabaseLogin.properties' in the root folder.
  3.1 The properties file should contain the following properties:
  - username
  - password
  - url (database connection string)
```
  
In-game tutorial on how to play the game.

### Screenshots:

<img width="300" alt="Screenshot 2019-05-30 at 23 36 49" src="https://user-images.githubusercontent.com/44582953/58666513-d1ec3600-8333-11e9-9430-c503a67a091c.png"> 
<img width="300" src="https://user-images.githubusercontent.com/44582953/58666636-2b546500-8334-11e9-9d22-71bbcac91419.png">
<img width="300" src="https://user-images.githubusercontent.com/44582953/58666663-4626d980-8334-11e9-9b12-5e62f0369a71.png">
<img width="300" src="https://user-images.githubusercontent.com/44582953/58666701-5b9c0380-8334-11e9-812c-f6508f95f1ab.png">



Have fun!
