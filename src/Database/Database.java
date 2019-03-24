package Database;

import dragAndDrop.Move;
import dragAndDrop.PieceSetup;
import dragAndDrop.ProtoUnitType;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static Database.Variables.*;


public class Database {

    //Class variables

    private static BasicConnectionPool connectionPool = null;

    public Database() {

        try {
            System.out.println("Creating pool");
            connectionPool = BasicConnectionPool.create();
        } catch (SQLException e) {
            //Prints out the error
            e.printStackTrace();
        }
    }

    /*
              _       _                     _    _
  /\/\   __ _| |_ ___| |__  _ __ ___   __ _| | _(_)_ __   __ _
 /    \ / _` | __/ __| '_ \| '_ ` _ \ / _` | |/ / | '_ \ / _` |
/ /\/\ \ (_| | || (__| | | | | | | | | (_| |   <| | | | | (_| |
\/    \/\__,_|\__\___|_| |_|_| |_| |_|\__,_|_|\_\_|_| |_|\__, |
                                                         |___/
     */

    public int matchMaking_search(int player_id) {
        Connection myConn = connectionPool.getConnection();
        String sqlString = "SELECT * FROM Matches where game_started=0";
        ResultSet results = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConn.prepareStatement(sqlString);
            results = preparedStatement.executeQuery();
            int match_id = -1;
            while (results.next()) {
                match_id = results.getInt("match_id");
            }
            if (match_id > 0) {
                System.out.println("Match Found: " + match_id);
                //Returns a boolean that does nothing
                joinGame(match_id, player_id);
                return match_id;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeResSet(results);
            connectionPool.releaseConnection(myConn);
        }
    }

    public boolean joinGame(int match_id, int player2) {
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "update Matches set player2=?, game_started=1 where match_id=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, player2);
            preparedStatement.setInt(2, match_id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Joined game");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }

    public int createGame(int player_id) {
        int match_id = -1;
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "insert into Matches(match_id, player1, player2, game_started) values (default,?,null,0);";
        PreparedStatement preparedStatement = null;
        ResultSet match_id_result = null;
        try {
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, player_id);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.println("Created Game");
                String getMatchIdQuery = "select * from Matches where player1=? and game_started=0";
                preparedStatement = myConn.prepareStatement(getMatchIdQuery);
                preparedStatement.setInt(1, player_id);
                match_id_result = preparedStatement.executeQuery();
                while (match_id_result.next()) {
                    match_id = match_id_result.getInt("match_id");
                }
            }
            return match_id;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeResSet(match_id_result);
            connectionPool.releaseConnection(myConn);
        }
    }

    public boolean pollGameStarted(int match_id) {
        int gameStarted = 0;
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "select * from Matches where match_id=? and game_started=1";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, match_id);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                gameStarted = result.getInt("game_started");
            }
            if (gameStarted == 1) {
                return true;
            } else {
                System.out.println(match_id);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeResSet(result);
            connectionPool.releaseConnection(myConn);
        }
    }

    public boolean abortMatch(int player_id) {
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "delete from Matches where player1=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, player_id);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }

    /*
     __      _
    / _\ ___| |_ _   _ _ __
    \ \ / _ \ __| | | | '_ \
    _\ \  __/ |_| |_| | |_) |
    \__/\___|\__|\__,_| .__/
                      |_|
     */

    public static ProtoUnitType importUnitType(int unitIdInput) {
        String sqlString = "SELECT * FROM Unit_types WHERE unit_type_id = ?";
        Connection myConn = connectionPool.getConnection();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ////UNIT INFO////
        String type;
        double hp;
        int attack;
        int abilityCooldown;
        double defenceMultiplier;
        int minAttackRange;
        int maxAttackRange;
        int movementRange;

        try {
            preparedStatement = myConn.prepareStatement(sqlString);
            preparedStatement.setInt(1, unitIdInput);

            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            type = resultSet.getString("unit_name");
            System.out.println(type);
            hp = (double) resultSet.getFloat("max_health");
            System.out.println(hp);
            attack = resultSet.getInt("attack");
            abilityCooldown = resultSet.getInt("ability_cooldown");
            defenceMultiplier = resultSet.getDouble("defence_multiplier");
            minAttackRange = resultSet.getInt("min_attack_range");
            maxAttackRange = resultSet.getInt("max_attack_range");
            movementRange = resultSet.getInt("movement_range");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeResSet(resultSet);
            connectionPool.releaseConnection(myConn);
        }
        return new ProtoUnitType(type, hp, attack, abilityCooldown, defenceMultiplier, minAttackRange, maxAttackRange, movementRange, "", "");
    }

    public ArrayList<Integer> fetchUnitTypeList() {

        ArrayList<Integer> outputList = new ArrayList<>();

        String sqlString = "SELECT unit_type_id FROM Unit_types";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = myConn.prepareStatement(sqlString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                outputList.add(resultSet.getInt("unit_type_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            Cleaner.closeResSet(resultSet);
            connectionPool.releaseConnection(myConn);
        }

        return outputList;
    }

    public void getPlayers() {

        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "select * from Matches where match_id=?";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, match_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //Stores player 1 and 2 as variables.
                player1 = resultSet.getInt("player1");
                player2 = resultSet.getInt("player2");

                //Stores opponent_id as variables.
                if (player1 == user_id) {
                    opponent_id = player2;
                } else {
                    opponent_id = player1;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeResSet(resultSet);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }

    //Inserts pieces into Database
    public void insertPieces() {
        Connection myConn = connectionPool.getConnection();
        ArrayList<String> piecesPlayer1 = new ArrayList<String>();
        ArrayList<String> piecesPlayer2 = new ArrayList<String>();
        PreparedStatement playerInsert1 = null;
        PreparedStatement playerInsert2 = null;


        //Player 1
        String sqlPlayer1piece = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(1,?,?,0,0);";
        String sqlPlayer1piece2 = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(2,?,?,3,0);";
        String sqlPlayer1piece3 = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(3,?,?,6,0);";
        String sqlPlayer1piece4 = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(4,?,?,2,1);";
        String sqlPlayer1piece5 = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(5,?,?,4,1);";
        String unit_player1 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (1,?,?, 120, 50, 1,1,1,1);";
        String unit_player2 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (2,?,?, 120, 50, 1,1,1,1);";
        String unit_player3 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (3,?,?, 120, 50, 1,1,1,1);";
        String unit_player4 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (4,?,?, 60, 50, 2,3,1,2);";
        String unit_player5 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (5,?,?, 60, 50, 2,3,1,2);";
        piecesPlayer1.add(sqlPlayer1piece);
        piecesPlayer1.add(sqlPlayer1piece2);
        piecesPlayer1.add(sqlPlayer1piece3);
        piecesPlayer1.add(sqlPlayer1piece4);
        piecesPlayer1.add(sqlPlayer1piece5);
        piecesPlayer1.add(unit_player1);
        piecesPlayer1.add(unit_player2);
        piecesPlayer1.add(unit_player3);
        piecesPlayer1.add(unit_player4);
        piecesPlayer1.add(unit_player5);

        //Player 2
        String sqlPlayer2piece = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(1,?,?,0,6);";
        String sqlPlayer2piece2 = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(2,?,?,3,6);";
        String sqlPlayer2piece3 = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(3,?,?,6,6);";
        String sqlPlayer2piece4 = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(4,?,?,2,5);";
        String sqlPlayer2piece5 = "insert into Pieces(piece_id, match_id, player_id, position_x, position_y) values(5,?,?,4,5);";
        String unit_player2_1 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (1,?,?, 120, 50, 1,1,1,1);";
        String unit_player2_2 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (2,?,?, 120, 50, 1,1,1,1);";
        String unit_player2_3 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (3,?,?, 120, 50, 1,1,1,1);";
        String unit_player2_4 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (4,?,?, 60, 50, 2,3,1,2);";
        String unit_player2_5 = "insert into Units (piece_id, match_id, player_id, current_health, current_attack, current_min_attack_range, current_max_attack_range, current_ability_cooldown, unit_type_id) values (5,?,?, 60, 50, 2,3,1,2);";
        piecesPlayer2.add(sqlPlayer2piece);
        piecesPlayer2.add(sqlPlayer2piece2);
        piecesPlayer2.add(sqlPlayer2piece3);
        piecesPlayer2.add(sqlPlayer2piece4);
        piecesPlayer2.add(sqlPlayer2piece5);
        piecesPlayer2.add(unit_player2_1);
        piecesPlayer2.add(unit_player2_2);
        piecesPlayer2.add(unit_player2_3);
        piecesPlayer2.add(unit_player2_4);
        piecesPlayer2.add(unit_player2_5);

        try {
                for (int i = 0; i < piecesPlayer1.size(); i++) {
                    playerInsert1 = myConn.prepareStatement(piecesPlayer1.get(i));
                    playerInsert1.setInt(1, match_id);
                    playerInsert1.setInt(2, player1);

                    playerInsert1.executeUpdate();
                }

            for (int i = 0; i < piecesPlayer2.size(); i++) {
                playerInsert2 = myConn.prepareStatement(piecesPlayer2.get(i));
                playerInsert2.setInt(1, match_id);
                playerInsert2.setInt(2, player2);

                playerInsert2.executeUpdate();
            }



        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(playerInsert1);
            Cleaner.closeStatement(playerInsert2);
            connectionPool.releaseConnection(myConn);
        }
    }

    //puts the units from the database into an arraylist
    public ArrayList<PieceSetup> importPlacementPieces() {
        ArrayList<PieceSetup> piecesImport = new ArrayList<PieceSetup>();
        ResultSet result = null;
        PreparedStatement preparedStatement = null;
        int pieceId;
        int match_idDB;
        int player_id;
        int positionX;
        int positionY;
        int unit_type_id;
        double current_hp;

        Connection myConn = connectionPool.getConnection();
        String sqlsetning = "select Pieces.piece_id, Pieces.match_id, Pieces.player_id,position_x, position_y, unit_type_id, current_health from Pieces \n" +
                "right join Units U on Pieces.piece_id = U.piece_id and Pieces.match_id = U.match_id and Pieces.player_id = U.player_id\n" +
                "where Pieces.match_id=?;";
        try {
            preparedStatement = myConn.prepareStatement(sqlsetning);
            preparedStatement.setInt(1, match_id); //This is the correct one
            // preparedStatement.setInt(1,250); //for test purposes;
            result = preparedStatement.executeQuery();
            while (result.next()) {
                pieceId = result.getInt("piece_id");
                player_id = result.getInt("player_id");
                positionX = result.getInt("position_x");
                positionY = result.getInt("position_y");
                unit_type_id = result.getInt("unit_type_id");
                current_hp = result.getDouble("current_health");

                PieceSetup piece = new PieceSetup(pieceId, match_id, player_id, positionX, positionY, unit_type_id, current_hp);
                piecesImport.add(piece);
            }

            return piecesImport;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeResSet(result);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return null;
    }
    /*
       ___                           _
      / _ \__ _ _ __ ___   ___ _ __ | | __ _ _   _
     / /_\/ _` | '_ ` _ \ / _ \ '_ \| |/ _` | | | |
    / /_\\ (_| | | | | | |  __/ |_) | | (_| | |_| |
    \____/\__,_|_| |_| |_|\___| .__/|_|\__,_|\__, |
                              |_|            |___/
     */

    public int sendTurn(int turn) {
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        String stmt = "SELECT player1,player2 FROM Matches WHERE match_id = ?";
        String stmt2 = "INSERT INTO Turns(turn_id,match_id,player) VALUES (?,?,?);";

        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, match_id);
            rs = preparedStatement.executeQuery();
            rs.next();

            preparedStatement = myConn.prepareStatement(stmt2);
            preparedStatement.setInt(
                    1, turn);
            preparedStatement.setInt(2, match_id);

            //Exception case for turn 1. Has to add the first turn for player 1.
            if (turn == 1) {
                preparedStatement.setInt(3, user_id);
            } else {
                preparedStatement.setInt(3, opponent_id);
            }
            if (preparedStatement.executeUpdate() > 0) {
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeResSet(rs);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    public int getTurnPlayer() {
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String stmt = "SELECT player FROM Turns WHERE match_id = ? ORDER BY turn_id DESC LIMIT 1;";
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, match_id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getInt("player");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    public void sendHealthInfo(int pieceID, double currentHealth) {
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String stmt = "UPDATE Units SET current_health = ? WHERE piece_id = ? AND match_id = ? AND player_id = ?";
        try {


            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setDouble(1, currentHealth); //"Arrays begin at 1"
            preparedStatement.setInt(2, pieceID);
            preparedStatement.setInt(3, match_id);
            preparedStatement.setInt(4, opponent_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }
    public boolean exportPieceMoveList(ArrayList<Move> movementList) {

        int turnId = movementList.get(0).getTurnId();       //TurnID is the same for all entries in the list
        int matchId = movementList.get(0).getMatchId();      //MatchID is the same for all entries in the list

        Connection myConn = connectionPool.getConnection();
        String sqlString = "UPDATE Pieces SET position_x = ?, position_y = ?  WHERE match_id=? AND piece_id = ? AND player_id = ? ;";

        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = myConn.prepareStatement(sqlString);

            myConn.setAutoCommit(false);

            for (int i = 0; i < movementList.size(); i++) {
                preparedStatement.setInt(1, movementList.get(i).getEndPosX());
                preparedStatement.setInt(2, movementList.get(i).getEndPosY());
                preparedStatement.setInt(3, matchId);
                preparedStatement.setInt(4, movementList.get(i).getPieceId());
                preparedStatement.setInt(5, user_id);


                preparedStatement.executeUpdate();
            }

            myConn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            Cleaner.rollBack(myConn);
            return false;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
            Cleaner.setAutoCommit(myConn);
        }
        return true;
    }

    /*public boolean exportMoveList(ArrayList<Move> movementList) {

        int turnId = movementList.get(0).getTurnId();       //TurnID is the same for all entries in the list
        int matchId = movementList.get(0).getMatchId();      //MatchID is the same for all entries in the list

        Connection myConn = connectionPool.getConnection();
        String sqlString = "INSERT INTO Movements VALUES (?,?,?,?,?,?,?);";

        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = myConn.prepareStatement(sqlString);

            myConn.setAutoCommit(false);

            for (int i = 0; i < movementList.size(); i++) {
                preparedStatement.setInt(1, turnId); //"Arrays begin at 1"
                preparedStatement.setInt(2, movementList.get(i).getPieceId());
                preparedStatement.setInt(3, matchId);
                preparedStatement.setInt(4, movementList.get(i).getStartPosX());
                preparedStatement.setInt(5, movementList.get(i).getStartPosY());
                preparedStatement.setInt(6, movementList.get(i).getEndPosX());
                preparedStatement.setInt(7, movementList.get(i).getStartPosY());

                preparedStatement.executeUpdate();
            }

            myConn.commit();
            Cleaner.setAutoCommit(myConn);


        } catch (SQLException e) {
            e.printStackTrace();
            Cleaner.rollBack(myConn);
            return false;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return true;
    }*/

    //TODO
    public boolean exportAttackList() {
        return false;
    }

    public ArrayList<Move> importMoveList(int turnIDInput, int matchIdInput, int playerIdInput) {

        ArrayList<Move> outputList = new ArrayList<>();

        Connection myConn = connectionPool.getConnection();
        String sqlString = "SELECT turn_id, Movements.piece_id, Movements.match_id, start_pos_x, start_pos_y, end_pos_x, end_pos_y FROM Movements JOIN Pieces ON Pieces.piece_id = Movements.piece_id WHERE player_id = playerIdInput AND Movements.match_id = matchIdInput AND turn_id = turnIdInput;";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            preparedStatement = myConn.prepareStatement(sqlString);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                outputList.add(new Move(resultSet.getInt("turn_id"), resultSet.getInt("piece_id"), resultSet.getInt("match_id"), resultSet.getInt("start_pos_x"), resultSet.getInt("start_pos_y"), resultSet.getInt("end_pos_x"), resultSet.getInt("end_pos_y")));
                resultSet.next();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }

        return outputList;
    }

    //TODO
    public boolean importAttackList() {
        return false;
    }
    /*
     __ _             _
    / _(_) __ _ _ __ (_)_ __
    \ \| |/ _` | '_ \| | '_ \
    _\ \ | (_| | | | | | | | |
    \__/_|\__, |_| |_|_|_| |_|
          |___/
     */

    //Should probably return more than a boolean so we can identify who is logged in.
    public int login(String username, String password) {

        Connection myConn = connectionPool.getConnection();

        //SELECT statement finds hashed password and salt from the entered user.
        String stmt = "SELECT user_id,hashedpassword,passwordsalt,online_status FROM Users WHERE username = ?";
        String stmt2 = "UPDATE Users SET online_status = 1 WHERE user_id = ?";

        PreparedStatement preparedQuery = null;
        PreparedStatement preparedUpdate = null;
        ResultSet rs = null;
        try {
            preparedQuery = myConn.prepareStatement(stmt);
            preparedQuery.setString(1, username);

            rs = preparedQuery.executeQuery();
            rs.next();
            int userId = rs.getInt("user_id");
            byte[] hash = rs.getBytes("hashedpassword");
            byte[] salt = rs.getBytes("passwordsalt");
            int loginStatus = rs.getInt("online_status");

            //Checks if the user is already logged in. If not the user is logged in.
            if (verifyPassword(password, hash, salt) && loginStatus == 0) {
                preparedUpdate = myConn.prepareStatement(stmt2);
                preparedUpdate.setInt(1, userId);
                preparedUpdate.executeUpdate();
                return userId;
            } else {
                System.out.println("User is already logged in");
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedQuery);
            Cleaner.closeStatement(preparedUpdate);
            Cleaner.closeResSet(rs);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    public boolean logout(int userId) {
        //Logs out the user. Sets online_status to 0.
        Connection myConn = connectionPool.getConnection();
        String stmt = "UPDATE Users SET online_status = 0 WHERE user_id = ?";
        PreparedStatement preparedQuery = null;
        try {
            preparedQuery = myConn.prepareStatement(stmt);
            preparedQuery.setInt(1, userId);
            preparedQuery.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedQuery);
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }

    public int signUp(String user, String password, String email) {

        Connection con = connectionPool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT username FROM Users WHERE username = ?");
            ps.setString(1, user);
            rs = ps.executeQuery();

            if (rs.next()) {
                return 0;
                // Brukeren finnes
            } else {
                //random is used to generate salt by creating a unique set of bytes.
                byte[] salt = new byte[16];
                SecureRandom random = new SecureRandom();
                random.nextBytes(salt);

                byte[] hash = generateHash(password, salt);

                addUserToDatabase(user, email, hash, salt);

                ps = con.prepareStatement("SELECT user_id FROM Users ORDER BY user_id DESC LIMIT 1;");
                rs = ps.executeQuery();
                rs.next();
                addUserToStatistics(rs.getInt("user_id"));
                return 1;
                //-1 feil
                //1 registrering godkjent
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(ps);
            Cleaner.closeResSet(rs);
            connectionPool.releaseConnection(con);
        }
        return -1;
    }

    private byte[] generateHash(String password, byte[] salt) {
        try {
            //PBKDF is used to create the hashed password. The salt is used as parameter.
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Compares entered password with the one stored in the DB.
    private boolean verifyPassword(String password, byte[] hash, byte[] salt) {

        byte[] enteredPassword;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            enteredPassword = factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return false;
        }
        return Arrays.equals(enteredPassword, hash);
    }

    private int addUserToDatabase(String username, String email, byte[] hash, byte[] salt) {
        String stmt = "INSERT INTO Users (username,hashedpassword,passwordsalt,email,online_status) VALUES (?,?,?,?,?)";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1, username);
            preparedStatement.setBytes(2, hash);
            preparedStatement.setBytes(3, salt);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, 0);
            if (preparedStatement.executeUpdate() > 0) return 1;
            else return -1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }


    public String getMyName(int used_id) {
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String stmt = "SELECT username FROM Users WHERE user_id = ?;";
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return "error";
    }

    public String getMyEmail(int used_id) {
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String stmt = "SELECT email FROM Users WHERE user_id = ?;";
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getString("email");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return "error";
    }

    /*
     __ _        _
    / _\ |_ __ _| |_ ___
    \ \| __/ _` | __/ __|
    _\ \ || (_| | |_\__ \
    \__/\__\__,_|\__|___/
     */

    public String getGamesPlayed(int user_id) {
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String value = "";
        String stmt = "SELECT games_played FROM Statistics WHERE user_id = ?;";
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            rs = preparedStatement.executeQuery();
            rs.next();
            value += rs.getInt("games_played");
            return value;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return "error";
    }


    public String getGamesWon(int user_id) {
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String value = "";
        String stmt = "SELECT games_won FROM Statistics WHERE user_id = ?;";
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            rs = preparedStatement.executeQuery();
            rs.next();
            value += rs.getInt("games_won");
            return value;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return "error";
    }

    public int addUserToStatistics(int user_id) {

        String stmt;
        PreparedStatement preparedStatement = null;
        Connection myConn = connectionPool.getConnection();
        stmt = "INSERT INTO Statistics(user_id, games_won, games_played) VALUES(?,?,?);";
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, 0);
            if (preparedStatement.executeUpdate() > 0) return 1;
            else return -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*
     __ _           _      _
    / _\ |__  _   _| |_ __| | _____      ___ __
    \ \| '_ \| | | | __/ _` |/ _ \ \ /\ / / '_ \
    _\ \ | | | |_| | || (_| | (_) \ V  V /| | | |
    \__/_| |_|\__,_|\__\__,_|\___/ \_/\_/ |_| |_|
     */

    public void close() throws SQLException {
        connectionPool.shutdown();
    }
}
