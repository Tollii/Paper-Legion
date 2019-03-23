package Database;

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

import static Database.Variables.match_id;

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

    public static ProtoUnitType importUnitType(String unitNameInput) {
        String sqlString = "SELECT * FROM Unit_types WHERE unit_name = ?";
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
            preparedStatement.setString(1, unitNameInput);

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

    public ArrayList<String> fetchUnitTypeList() {

        ArrayList<String> outputList = new ArrayList<>();

        String sqlString = "SELECT unit_name FROM Unit_types";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = myConn.prepareStatement(sqlString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                outputList.add(resultSet.getString("unit_name"));
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

    //TODO
    public boolean exportMoveList() {
        return false;
    }

    //TODO
    public boolean exportAttackList() {
        return false;
    }

    //TODO
    public boolean importMoveList() {
        return false;
    }

    //TODO
    public boolean importAttackList() {
        return false;
    }


    public void test() {

        System.out.println("Test begun");
        String sqlString = "SELECT username FROM Users";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedQuery = null;
        ResultSet resultSet = null;

        try {
            preparedQuery = myConn.prepareStatement(sqlString);

            System.out.println("Executing statement");
            resultSet = preparedQuery.executeQuery();
            System.out.println("Statement executed");

            while (resultSet.next()) {

                System.out.println(resultSet.getString("username"));
            }

            connectionPool.releaseConnection(myConn);
        } catch (SQLException e) {

            e.printStackTrace();
            connectionPool.releaseConnection(myConn);
        }
    }

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

        //TODO Check if user is not already registered, or username is taken.

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
            if (preparedStatement.executeUpdate() > 0) {
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    public int sendTurn() {
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String stmt = "PUT IT HERE JON";
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1,match_id);
            if (preparedStatement.executeUpdate() > 0) {
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return -1;
    }

    public boolean waitForTurn() {
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String stmt = "PUT IT HERE JON";
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setInt(1,match_id);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedStatement);
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }

    public void close() throws SQLException {
        connectionPool.shutdown();
    }

    public static void main(String[] args) throws SQLException {

        Database database = new Database();

        database.test();

        database.importUnitType("swordsman");

        database.close();
    }
}
