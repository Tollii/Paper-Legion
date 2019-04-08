package database;

import gameplay.*;
import menus.Controller.Match;
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

import static database.Variables.*;

/**
 * This class contains all of the MYSQL statements that is used for the whole program, from login to gameplay.
 * All methods conatining any SQL lines use the Cleaner class for cleanup at the end of it's execution.
 * Most if, not all statements uses preparedStatement.
 */


public class Database {


    private static ConnectionPool connectionPool = null;

    /**
     * Initializes the connection pool on creation of a Database object
     */
    public Database() {

        try {
            System.out.println("Creating pool");
            connectionPool = ConnectionPool.create();
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

    /**
     * Imports the values for a unit type and returns them as a ProtoUnitType
     *
     * @param unitIdInput int to identify which unit type to import
     * @return ProtoUnitType
     * @see ProtoUnitType
     */
    public static ProtoUnitType importUnitType(int unitIdInput) {
        String sqlString = "SELECT * FROM Unit_types WHERE unit_type_id = ?";
        Connection myConn = connectionPool.getConnection();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ////UNIT INFO////
        String type;
        int unitTypeId;
        double hp;
        int attack;
        double defenceMultiplier;
        int minAttackRange;
        int maxAttackRange;
        int movementRange;
        int cost;

        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlString);
            preparedStatement.setInt(1, unitIdInput);

            resultSet = preparedStatement.executeQuery();
            myConn.commit();
            resultSet.next();
            type = resultSet.getString("unit_name");
            unitTypeId = resultSet.getInt("unit_type_id");
            hp = (double) resultSet.getFloat("max_health");
            attack = resultSet.getInt("attack");
            defenceMultiplier = resultSet.getDouble("defence_multiplier");
            minAttackRange = resultSet.getInt("min_attack_range");
            maxAttackRange = resultSet.getInt("max_attack_range");
            movementRange = resultSet.getInt("movement_range");
            cost = resultSet.getInt("cost");

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(resultSet);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return new ProtoUnitType(type, unitTypeId, hp, attack, defenceMultiplier, minAttackRange, maxAttackRange, movementRange, cost, "", "", null, null);
    }

    /**
     * A select statement that puts all created matches that has not been started yet in a list.
     * Checks if the match is password protected or not, and updates the value in that specified match accordingly.
     *
     * @return a list of current matches that has been created, but not started.
     */
    public ArrayList<Match> findGamesAvailable(){
        ArrayList<Match> matches = new ArrayList<Match>();
        String sqlString = "select match_id, username, password from Matches inner join Users on Matches.player1 = Users.user_id where game_started=0;";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = myConn.prepareStatement(sqlString);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int match_id = resultSet.getInt("match_id");
                String playername = resultSet.getString("username");
                boolean passwordProtected;
                String password = resultSet.getString("password");
                if(password != null){
                 passwordProtected = true;
                } else{
                    passwordProtected = false;
                }

                matches.add(new Match(match_id,playername, passwordProtected, password));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(resultSet);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return matches;
    }

    /**
     * Searches through all the games that has been created and not started that is NOT password protected.
     * Joins the first one available.
     *
     * @param player_id id of the player that is searching for games.
     * @return -1 on error, returns match_id on success.
     */
    public int quickMatch_search(int player_id) {
        Connection myConn = connectionPool.getConnection();
        String sqlString = "SELECT * FROM Matches where game_started=0 and password is null";
        ResultSet results = null;
        PreparedStatement preparedStatement = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlString);
            results = preparedStatement.executeQuery();
            myConn.commit();
            int match_id = -1;
            while (results.next()) {
                match_id = results.getInt("match_id");
            }
            if (match_id > 0) {
                System.out.println("Match Found: " + match_id);
                //Returns a boolean that does nothing
                joinGame(match_id, player_id);
                myConn.commit();
                return match_id;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeResSet(results);
            connectionPool.releaseConnection(myConn);
        }
    }

    /**
     * A method to join selected match, using match_id to identify match.
     *
     * @param match_id match id of the match that is being joined.
     * @param player2 player id of the user joining the game.
     * @return false on failure, true on success.
     */

    public boolean joinGame(int match_id, int player2) {
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "update Matches set player2=?, game_started=1 where match_id=?;";
        PreparedStatement preparedStatement = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, player2);
            preparedStatement.setInt(2, match_id);
            int result = preparedStatement.executeUpdate();
            myConn.commit();
            if (result == 1) {
                System.out.println("Joined game");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }

    /**
     *
     * Creates a game. If no password is given as a parameter, it will default to null and be an open game
     * which everyone can join.
     *
     * @param player_id player id of the player creating the game
     * @param password if game is password protected, use a string. If not, it will default to null.
     * @return match_id on success, -1 on failure
     */
    public int createGame(int player_id, String password) {
        int match_id = -1;
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "insert into Matches(match_id, player1, player2, game_started, password) values (default,?,null,0, ?);";
        PreparedStatement preparedStatement = null;
        ResultSet match_id_result = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, player_id);
            if(password.equalsIgnoreCase("null")){
                preparedStatement.setString(2, null);
            } else{
                preparedStatement.setString(2, password);

            }

            int result = preparedStatement.executeUpdate();
            myConn.commit();
            if (result > 0) {
                System.out.println("Created Game");
                String getMatchIdQuery = "select * from Matches where player1=? and game_started=0";
                preparedStatement = myConn.prepareStatement(getMatchIdQuery);
                preparedStatement.setInt(1, player_id);
                match_id_result = preparedStatement.executeQuery();
                myConn.commit();
                while (match_id_result.next()) {
                    match_id = match_id_result.getInt("match_id");
                }
            }
            return match_id;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(match_id_result);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }

    /**
     * Polls once whether the game has started or not. Used when a game has been created to check
     * if another player has joined your game.
     *
     * @param match_id id of the match being polled.
     * @return true when an opponent has been found, false if not.
     */
    public boolean pollGameStarted(int match_id) {
        int gameStarted = 0;
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "select * from Matches where match_id=? and game_started=1";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, match_id);
            myConn.commit();
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
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeResSet(result);
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

    /**
     * Deletes game(s) that has been created by the player_id given as a parameter.
     *
     * @param player_id id of the player's match that'll be deleted
     * @return true on success, false on failure.
     */
    public boolean abortMatch(int player_id) {
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "delete from Matches where player1=?;";
        PreparedStatement preparedStatement = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, player_id);
            int result = preparedStatement.executeUpdate();
            myConn.commit();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }

    /**
     * Imports a list of UnitTypeIds. This is used to identify which Units to import.
     * @return ArrayList. Returns an integer ArrayList of the different UnitTypeIds
     */
    public ArrayList<Integer> fetchUnitTypeList() {

        ArrayList<Integer> outputList = new ArrayList<>();

        String sqlString = "SELECT unit_type_id FROM Unit_types";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlString);
            resultSet = preparedStatement.executeQuery();
            myConn.commit();
            while (resultSet.next()) {
                outputList.add(resultSet.getInt("unit_type_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeResSet(resultSet);
            connectionPool.releaseConnection(myConn);
        }

        return outputList;
    }

    /**
     * Updates the variable class with your opponents player_id
     */
    public void getPlayers() {

        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "select * from Matches where match_id=?";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, match_id);
            resultSet = preparedStatement.executeQuery();
            myConn.commit();
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
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(resultSet);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }

    /**
     * Used at the end of the placement phase, this method is used to export the placed units and their position to the database.
     *
     * @param exportUnitList ArrayList. A list of Units
     * @param exportPositionXList ArrayList. Position x for the exported units
     * @param exportPositionYList ArrayList. Position Y for the exported units
     * @see Unit
     * @see GameMain placementPhaseFinished
     */

    public void exportPlacementUnits(ArrayList<Unit> exportUnitList, ArrayList<Integer> exportPositionXList, ArrayList<Integer> exportPositionYList) {
        Connection myConn = connectionPool.getConnection();
        String sqlSetningPieces = "INSERT INTO Pieces VALUES(?,?,?,?,?);";
        String sqlSetningUnits = "INSERT INTO Units VALUES(?,?,?,?,?,?,?,?);";

        PreparedStatement preparedStatement = null;

        try {
            myConn.setAutoCommit(false);

            for (int i = 0; i < exportUnitList.size(); i++) {

                preparedStatement = myConn.prepareStatement(sqlSetningPieces);

                preparedStatement.setInt(1, exportUnitList.get(i).getPieceId());    //"Arrays begin at 1"
                preparedStatement.setInt(2, match_id);
                preparedStatement.setInt(3, user_id);
                preparedStatement.setInt(4, exportPositionXList.get(i));
                preparedStatement.setInt(5, exportPositionYList.get(i));

                preparedStatement.executeUpdate();

                preparedStatement = myConn.prepareStatement(sqlSetningUnits);

                preparedStatement.setInt(1, exportUnitList.get(i).getPieceId());
                preparedStatement.setInt(2, match_id);
                preparedStatement.setInt(3, user_id);
                preparedStatement.setDouble(4, exportUnitList.get(i).getHp());
                preparedStatement.setInt(5, exportUnitList.get(i).getAttack());
                preparedStatement.setInt(6, exportUnitList.get(i).getMinAttackRange());
                preparedStatement.setInt(7, exportUnitList.get(i).getMaxAttackRange());
                preparedStatement.setInt(8, exportUnitList.get(i).getUnitTypeId());

                preparedStatement.executeUpdate();
            }

            //If the loop is successful, commit.
            myConn.commit();

        }catch(SQLException e){
            e.printStackTrace();
            Cleaner.rollBack(myConn);
        }finally {

            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }

    /**
     * Imports the result of the opponents players placement phase.
     * The values are imported as a List of PieceSetup.
     *
     * @return ArrayList. List of PieceSetup, contains Unit values and position X, Y.
     * @see PieceSetup
     */
    public ArrayList<PieceSetup> importPlacementUnits() {

        ArrayList<PieceSetup> outputList = null;
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "SELECT Pieces.piece_id, Pieces.match_id, Pieces.player_id, position_x, position_y, unit_type_id FROM Pieces JOIN Units U ON Pieces.piece_id = U.piece_id AND Pieces.match_id = U.match_id AND Pieces.player_id = U.player_id WHERE Pieces.match_id = ? AND Pieces.player_id = ?;";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;


        try {
                preparedStatement = myConn.prepareStatement(sqlSetning);

                preparedStatement.setInt(1, match_id);    //"Arrays begin at 1"
                preparedStatement.setInt(2, opponent_id);

                resultSet = preparedStatement.executeQuery();

                outputList = new ArrayList<>();

                while(resultSet.next()){

                    outputList.add(new PieceSetup(resultSet.getInt("piece_id"), resultSet.getInt("match_id"), resultSet.getInt("player_id"), resultSet.getInt("position_x"), resultSet.getInt("position_y"), resultSet.getInt("unit_type_id")));
                }


        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }

        return outputList;
    }

    /**
     * Checks if your opponent is ready during the placement phase of the game.
     *
     *
     * @return true if opponent is ready, false if not.
     */
    public boolean checkIfOpponentReady(){

        Boolean returnBoolean = false;

        Connection myConn = connectionPool.getConnection();

        String sqlSetning = "";
        ResultSet resultSet = null;

        if(opponent_id == player1){
            sqlSetning = "SELECT player1_ready AS ready FROM Matches WHERE match_id = ?";
        }else{
            sqlSetning = "SELECT player2_ready AS ready FROM Matches WHERE match_id = ?";
        }

        PreparedStatement preparedStatement = null;

        try{

            preparedStatement = myConn.prepareStatement(sqlSetning);

            preparedStatement.setInt(1, match_id);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                returnBoolean = resultSet.getBoolean("ready");
            }

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            Cleaner.closeResSet(resultSet);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }

        return returnBoolean;

    }

    /**
     * Sets the status of the player as ready
     *
     * @param ready
     */
    public void setReady(boolean ready){

        Connection myConn = connectionPool.getConnection();

        String sqlSetning;

        if(user_id == player1){
            sqlSetning = "UPDATE Matches SET player1_ready = ?";
        }else{
            sqlSetning = "UPDATE Matches SET player2_ready = ?";
        }

        PreparedStatement preparedStatement = null;

        try{

            preparedStatement = myConn.prepareStatement(sqlSetning);

            if(ready){

                preparedStatement.setInt(1, 1);

            }else{

                preparedStatement.setInt(1, 0);

            }


            preparedStatement.executeUpdate();


        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }

/*
    //Inserts pieces into database
    public void insertPieces() {
        Connection myConn = connectionPool.getConnection();
        Connection myConn2 = connectionPool.getConnection();
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
//            myConn.setAutoCommit(false);
//            myConn2.setAutoCommit(false);

            for (int i = 0; i < piecesPlayer1.size(); i++) {
                playerInsert1 = myConn.prepareStatement(piecesPlayer1.get(i));
                playerInsert1.setInt(1, match_id);
                playerInsert1.setInt(2, player1);

                playerInsert1.executeUpdate();
            }

            for (int i = 0; i < piecesPlayer2.size(); i++) {
                playerInsert2 = myConn2.prepareStatement(piecesPlayer2.get(i));
                playerInsert2.setInt(1, match_id);
                playerInsert2.setInt(2, player2);

                playerInsert2.executeUpdate();
            }

            myConn.commit();
            myConn2.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            Cleaner.setAutoCommit(myConn);
//            Cleaner.setAutoCommit(myConn2);
            Cleaner.closeStatement(playerInsert1);
            Cleaner.closeStatement(playerInsert2);
            connectionPool.releaseConnection(myConn);
            connectionPool.releaseConnection(myConn2);
        }
    }
    */

    /*
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
                "join Units U on Pieces.piece_id = U.piece_id and Pieces.match_id = U.match_id and Pieces.player_id = U.player_id\n" +
                "where Pieces.match_id=?;";
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlsetning);
            preparedStatement.setInt(1, match_id); //This is the correct one
            // preparedStatement.setInt(1,250); //for test purposes;
            result = preparedStatement.executeQuery();
            myConn.commit();
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
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(result);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return null;
    }
    */

    /**
     * Sends a list of obstacles to the Obstacles table on the MYSQL database
     *
     * @param obstacles list of obstacles
     */
    public void exportObstacles(ArrayList<Obstacle> obstacles) {
        Connection myConn = connectionPool.getConnection();
        Connection myConn2 = connectionPool.getConnection();
        PreparedStatement player = null;
        PreparedStatement matchUpdate = null;

        //The statement
        String obstaclesInsertStmt = "insert into Obstacles(obstacle_id, match_id, position_x, position_y) values(?,?,?,?);";
        String obstaclesAmountStmt = "UPDATE Matches SET obstacle_amount = ? WHERE  match_id = ?;";

        try {
            //         myConn.setAutoCommit(false);
            //         myConn2.setAutoCommit(false);

            for (int i = 0; i < obstacles.size(); i++) {
                player = myConn.prepareStatement(obstaclesInsertStmt);
                player.setInt(1, i+1);
                player.setInt(2, match_id);
                player.setInt(3, obstacles.get(i).getPosX());
                player.setInt(4, obstacles.get(i).getPosY());

                player.executeUpdate();
            }

            matchUpdate = myConn2.prepareStatement(obstaclesAmountStmt);
            matchUpdate.setInt(1,obstacles.size());
            matchUpdate.setInt(2,match_id);

            matchUpdate.executeUpdate();

            //          myConn.commit();
            //         myConn2.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(player);
            connectionPool.releaseConnection(myConn);
        }
    }

    /**
     * Gets obstacles and their position from the MYSQL database.
     *
     * @return imported list of obstacles
     */
    public ArrayList<Obstacle> importObstacles() {
        ArrayList<Obstacle> ObstacleImport = new ArrayList<>();
        ResultSet result = null;
        PreparedStatement preparedStatement = null;
        int positionX;
        int positionY;
        int obstacleID;

        Connection myConn = connectionPool.getConnection();
        String sqlsetning = "SELECT obstacle_id, position_x, position_y FROM Obstacles WHERE match_id = ?";
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlsetning);
            preparedStatement.setInt(1, match_id); //This is the correct one
            result = preparedStatement.executeQuery();
            myConn.commit();
            while (result.next()) {
                obstacleID = result.getInt("obstacle_id");
                positionX = result.getInt("position_x");
                positionY = result.getInt("position_y");

                Obstacle piece = new Obstacle(positionX, positionY, obstacleID);
                ObstacleImport.add(piece);
            }
            return ObstacleImport;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(result);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return null;
    }

    /**
     *  Checks if the correct amount of obstacles have been added in accordance with the # of obstacles in the database in a given match
     *
     * @return true if local obstacles and server obstacles are equal. False if not.
     */
    public Boolean obstaclesHaveBeenAdded() {

        Integer obstacle_amount;
        int obstacle_in_db;

        ResultSet result = null;
        ResultSet result2 = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;

        Connection myConn = connectionPool.getConnection();
        Connection myConn2 = connectionPool.getConnection();

        String sqlsetning = "SELECT obstacle_amount FROM Matches WHERE match_id = ?";
        String sqlCount = "SELECT COUNT(*) AS counting FROM Obstacles WHERE match_id = ?";
        try {
            myConn.setAutoCommit(false);
            myConn2.setAutoCommit(false);

            preparedStatement = myConn.prepareStatement(sqlsetning);

            preparedStatement2 = myConn.prepareStatement(sqlCount);

            preparedStatement.setInt(1, match_id); //This is the correct one

            preparedStatement2.setInt(1,match_id);

            result = preparedStatement.executeQuery();
            result2 = preparedStatement2.executeQuery();

            myConn.commit();
            myConn2.commit();

            result2.next();
            result.next();
            if (!result.wasNull()) {
                obstacle_amount = result.getInt("obstacle_amount");
                obstacle_in_db = result2.getInt("counting");

                return obstacle_amount == obstacle_in_db && obstacle_in_db != 0;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.setAutoCommit(myConn2);
            Cleaner.closeResSet(result);
            Cleaner.closeResSet(result2);
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeStatement(preparedStatement2);
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

    /**
     * Updates the database with a new turn. Handles exception case with round 1.
     *
     * @param turn turn #
     * @return 1 on success, -1 on failure.
     */

    public int sendTurn(int turn) {
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        String stmt = "INSERT INTO Turns(turn_id,match_id,player) VALUES (?,?,?);";

        try {
            myConn.setAutoCommit(false);

            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, turn);
            preparedStatement.setInt(2, match_id);

            //Exception case for turn 1. Has to add the first turn for player 1.
            if (turn == 1) {
                preparedStatement.setInt(3, user_id);
            } else {
                preparedStatement.setInt(3, opponent_id);
            }
            if (preparedStatement.executeUpdate() > 0) {
                myConn.commit();
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    /**
     * Imports the latest turn stored on the database.
     *
     * @return player id on success, -1 on failure.
     */
    public int getTurnPlayer() {
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String stmt = "SELECT player FROM Turns WHERE match_id = ? ORDER BY turn_id DESC LIMIT 1;";
        ResultSet rs = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, match_id);
            rs = preparedStatement.executeQuery();
            myConn.commit();
            if (!rs.next()) {
                return -1;
            }
            return rs.getInt("player");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }
/*
    public int pollForUnits() { //Midlertidig metode for auto generert units
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String stmt = "SELECT COUNT(*) AS counter FROM Units WHERE match_id = ?;";
        ResultSet rs = null;
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, match_id);
            rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getInt("counter");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(rs);
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
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setDouble(1, currentHealth); //"Arrays begin at 1"
            preparedStatement.setInt(2, pieceID);
            preparedStatement.setInt(3, match_id);
            preparedStatement.setInt(4, opponent_id);

            preparedStatement.executeUpdate();
            myConn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
    }
*/

    /**
     * Sends data for movement done locally in the gameplay phase, to the database.
     *
     * @param movementList list of movements.
     * @return true on success, false on failure.
     */
    public boolean exportMoveList(ArrayList<Move> movementList) {

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
                preparedStatement.setInt(7, movementList.get(i).getEndPosY());

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
    }

    /**
     * Imports your opponents movements.
     *
     * @param enemyTurnIDInput your opponents latest turn id.
     * @param matchIdInput match id.
     * @return List of movements on success, null on failure.
     */
    public ArrayList<Move> importMoveList(int enemyTurnIDInput, int matchIdInput) {

        ArrayList<Move> outputList = new ArrayList<>();

        Connection myConn = connectionPool.getConnection();
        String sqlString = "SELECT turn_id, Movements.piece_id, Movements.match_id, start_pos_x, start_pos_y, end_pos_x, end_pos_y FROM Movements WHERE Movements.match_id = ? AND turn_id = ?;";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = myConn.prepareStatement(sqlString);

            preparedStatement.setInt(1, matchIdInput); //"Arrays begin at 1"
            preparedStatement.setInt(2, enemyTurnIDInput);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Adding to output!!!");
                outputList.add(new Move(resultSet.getInt("turn_id"), resultSet.getInt("piece_id"), resultSet.getInt("match_id"), resultSet.getInt("start_pos_x"), resultSet.getInt("start_pos_y"), resultSet.getInt("end_pos_x"), resultSet.getInt("end_pos_y")));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            Cleaner.closeResSet(resultSet);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }


        System.out.println("Imported move list size: " + outputList.size());

        return outputList;
    }

    /**
     * Sends attacks locally done to the database.
     *
     * @param attackList list of local attacks.
     * @return true on success, false on failure.
     */
    public boolean exportAttackList(ArrayList<Attack> attackList) {

        int turnId = attackList.get(0).getTurnId();                         //TurnId is the same for all entries in the list
        int attackingPlayerId = attackList.get(0).getAttackingPlayerId();   //AttackingPlayerId is the same for all entries in the list
        int matchId = attackList.get(0).getMatchId();                       //MatchId is the same for all entries in the list

        Connection myConn = connectionPool.getConnection();
        String sqlString = "INSERT INTO Attacks VALUES (?,?,?,?,?,?);";

        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = myConn.prepareStatement(sqlString);

            myConn.setAutoCommit(false);

            for (int i = 0; i < attackList.size(); i++) {
                preparedStatement.setInt(1, turnId); //"Arrays begin at 1"
                preparedStatement.setInt(2, attackingPlayerId);
                preparedStatement.setInt(3, attackList.get(i).getAttackerPieceID());
                preparedStatement.setInt(4, attackList.get(i).getReceiverPieceID());
                preparedStatement.setInt(5, matchId);
                preparedStatement.setInt(6, attackList.get(i).getDamage());

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
    }

    /**
     * Imports the latest attacks from your opponent.
     *
     * @param enemyTurnIDInput your opponents latest turn id.
     * @param matchIdInput match id.
     * @param otherPlayerIdInput your opponents player id.
     * @return list of attacks on success, null on failure.
     */
    public ArrayList<Attack> importAttackList(int enemyTurnIDInput, int matchIdInput, int otherPlayerIdInput) {

        ArrayList<Attack> outputList = new ArrayList<>();

        Connection myConn = connectionPool.getConnection();
        String sqlString = "SELECT * FROM Attacks WHERE attacking_player_id = ? AND match_id = ? AND turn_id = ?;";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = myConn.prepareStatement(sqlString);

            preparedStatement.setInt(1, otherPlayerIdInput); //"Arrays begin at 1"
            preparedStatement.setInt(2, matchIdInput);
            preparedStatement.setInt(3, enemyTurnIDInput);

            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                System.out.println("ADDING TO ATTACK LIST!");
                outputList.add(new Attack(resultSet.getInt("turn_id"), resultSet.getInt("match_id"), resultSet.getInt("attacking_player_id"), resultSet.getInt("attacker_piece_id"), resultSet.getInt("receiver_piece_id"), resultSet.getInt("damage_dealt")));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            Cleaner.closeResSet(resultSet);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }

        return outputList;
    }

    /**
     * Sets a match to being surrendered.
     *
     * @return true if game has been surrendered, false if an error occurred.
     */
    public boolean surrenderGame() {
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "update Matches set surrendered=? where match_id=?";
        PreparedStatement preparedStatement = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, match_id);

            int result = preparedStatement.executeUpdate();
            myConn.commit();
            if (result == 1) {
                System.out.println("Game surrendered");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }

    /**
     * Checks if a game has been surrendered.
     *
     * @return returns 1 if game has been surrendered, -1 if not.
     */
    public int checkForSurrender() {
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String stmt = "SELECT surrendered FROM Matches WHERE match_id = ?;";
        ResultSet rs = null;
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, match_id);
            rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return -1;
            }
            return rs.getInt("surrendered");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    /*
     __ _             _
    / _(_) __ _ _ __ (_)_ __
    \ \| |/ _` | '_ \| | '_ \
    _\ \ | (_| | | | | | | | |
    \__/_|\__, |_| |_|_|_| |_|
          |___/
     */

    /**
     * Takes given username and password to log in.
     *
     * @param username username to account
     * @param password password to account
     * @return user id if login is successful, -1 if not.
     */
    public int login(String username, String password) {

        Connection myConn = connectionPool.getConnection();

        //SELECT statement finds hashed password and salt from the entered user.
        String stmt = "SELECT user_id,hashedpassword,passwordsalt,online_status FROM Users WHERE username = ?";
        String stmt2 = "UPDATE Users SET online_status = 1 WHERE user_id = ?";

        PreparedStatement preparedQuery = null;
        PreparedStatement preparedUpdate = null;
        ResultSet rs = null;
        try {
            myConn.setAutoCommit(false);
            preparedQuery = myConn.prepareStatement(stmt);
            preparedQuery.setString(1, username);
            myConn.commit();
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
                myConn.commit();
                return userId;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedQuery);
            Cleaner.closeStatement(preparedUpdate);
            Cleaner.closeResSet(rs);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    /**
     * Logs out a user.
     *
     * @param userId user id of user to log out of.
     * @return true if logged out, false if not.
     */
    public boolean logout(int userId) {
        //Logs out the user. Sets online_status to 0.
        Connection myConn = connectionPool.getConnection();
        String stmt = "UPDATE Users SET online_status = 0 WHERE user_id = ?";
        PreparedStatement preparedQuery = null;
        try {
            myConn.setAutoCommit(false);
            preparedQuery = myConn.prepareStatement(stmt);
            preparedQuery.setInt(1, userId);
            preparedQuery.executeUpdate();
            myConn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedQuery);
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }

    /**
     * Creates a new user using user name, password and e-mail.
     * Password gets a hash using SALT and then stored on the database.
     *
     * @param user new account user name.
     * @param password new account password.
     * @param email new account email.
     * @return 1 for registration success, 0 if account exists, -1 if error.
     */
    public int signUp(String user, String password, String email) {

        Connection con = connectionPool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("SELECT username FROM Users WHERE username = ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            con.commit();

            if (rs.next()) {
                return 0;
            } else {
                //random is used to generate salt by creating a unique set of bytes.
                byte[] salt = new byte[16];
                SecureRandom random = new SecureRandom();
                random.nextBytes(salt);

                byte[] hash = generateHash(password, salt);

                addUserToDatabase(user, email, hash, salt);

                ps = con.prepareStatement("SELECT user_id FROM Users ORDER BY user_id DESC LIMIT 1;");
                rs = ps.executeQuery();
                con.commit();
                rs.next();
                addUserToStatistics(rs.getInt("user_id"));
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(con);
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(ps);
            connectionPool.releaseConnection(con);
        }
        return -1;
    }

    /**
     * Generates a hash,
     *
     * @param password
     * @param salt
     * @return
     */
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

    /**
     * Compares the given password with the one stored on the database .
     *
     * @param password given password.
     * @param hash hash from database.
     * @param salt users' SALT.
     * @return
     */

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

    /**
     * Adds user to database.
     *
     * @param username new users' username.
     * @param email new users' email.
     * @param hash new users' hashed password.
     * @param salt new users' SALT.
     * @return 1 if successful, -1 on failure.
     */
    private int addUserToDatabase(String username, String email, byte[] hash, byte[] salt) {
        String stmt = "INSERT INTO Users (username,hashedpassword,passwordsalt,email,online_status) VALUES (?,?,?,?,?)";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1, username);
            preparedStatement.setBytes(2, hash);
            preparedStatement.setBytes(3, salt);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, 0);
            if (preparedStatement.executeUpdate() > 0) {
                myConn.commit();
                return 1;
            } else return -1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    /*
     __ _        _
    / _\ |_ __ _| |_ ___
    \ \| __/ _` | __/ __|
    _\ \ || (_| | |_\__ \
    \__/\__\__,_|\__|___/
    Stats
     */

    /**
     * Gets a given user's username.
     *
     * @return user's username on success, "error" on failure.
     */
    public String getMyName() {
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String stmt = "SELECT username FROM Users WHERE user_id = ?;";
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            rs = preparedStatement.executeQuery();
            myConn.commit();
            rs.next();
            return rs.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return "error";
    }

    /**
     * Gets a given user's e-mail.
     *
     * @return user's e-mail on success, "error" on failure.
     */
    public String getMyEmail() {
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String stmt = "SELECT email FROM Users WHERE user_id = ?;";
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            rs = preparedStatement.executeQuery();
            myConn.commit();
            rs.next();
            return rs.getString("email");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return "error";
    }

    /**
     * Increments the number of games a user has played.
     *
     * @return true if games incremented, false if error.
     */
    public boolean incrementGamesPlayed() {
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "update Statistics set games_played=games_played + 1 where user_id = ?;";
        PreparedStatement preparedStatement = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, user_id);
            int result = preparedStatement.executeUpdate();
            myConn.commit();
            if (result == 1) {
                System.out.println("Game registered");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }


    /**
     * Increments number of games a player has won.
     *
     * @return true if win registered, false if error.
     */
    public boolean incrementGamesWon() {
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "update Statistics set games_won=games_won + 1 where user_id = ?;";
        PreparedStatement preparedStatement = null;
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1, user_id);
            int result = preparedStatement.executeUpdate();
            myConn.commit();
            if (result == 1) {
                System.out.println("Win registered");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }

    /**
     * Gets # of games a user has played.
     *
     * @return # of games on success as a String, "error" on failure.
     */
    public String getGamesPlayed() {
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String value = "";
        String stmt = "SELECT games_played FROM Statistics WHERE user_id = ?;";
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            rs = preparedStatement.executeQuery();
            myConn.commit();
            rs.next();
            value += rs.getInt("games_played");
            return value;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return "error";
    }

    /**
     * Gets # of games a user has won.
     *
     * @return # of games won as a String on success, "error on failure.
     */
    public String getGamesWon() {
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String value = "";
        String stmt = "SELECT games_won FROM Statistics WHERE user_id = ?;";
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            rs = preparedStatement.executeQuery();
            myConn.commit();
            rs.next();
            value += rs.getInt("games_won");
            return value;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeResSet(rs);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return "error";
    }

    /**
     * Adds a given user to the statistics table.
     *
     * @param user_id user id of player to add in statistics
     * @return 1 on success, -1 on failure.
     */
    public int addUserToStatistics(int user_id) {

        String stmt;
        PreparedStatement preparedStatement = null;
        Connection myConn = connectionPool.getConnection();
        stmt = "INSERT INTO Statistics(user_id, games_won, games_played) VALUES(?,?,?);";
        try {
            myConn.setAutoCommit(false);
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, 0);
            if (preparedStatement.executeUpdate() > 0) {
                myConn.commit();
                return 1;
            } else return -1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.setAutoCommit(myConn);
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
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

    /**
     * Calls the shutdown method from ConnectionPool.java
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        connectionPool.shutdown();
    }
}
