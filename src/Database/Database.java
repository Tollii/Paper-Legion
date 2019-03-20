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
import java.util.Arrays;

public class Database {

    //Class variables

    static BasicConnectionPool connectionPool = null;
    static private Cleaner cleaner = new Cleaner();
    private String username;

    public Database() {

        try {
            System.out.println("Creating pool");
            connectionPool = BasicConnectionPool.create();
        } catch (SQLException e) {
            //Prints out the error
            e.printStackTrace();
        }

    }

    public static ProtoUnitType importUnitType(String unitNameInput){

        String sqlString = "SELECT * FROM Unit_types WHERE unit_name =" + "'"+unitNameInput+"'";
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

        try{
            preparedStatement = myConn.prepareStatement(sqlString);

            System.out.println("Executing statement");
            resultSet = preparedStatement.executeQuery();
            System.out.println("Statement executed");

            resultSet.next();
            type = resultSet.getString("unit_name");
            System.out.println(type);
            hp = (double)resultSet.getFloat("max_health");
            System.out.println(hp);
            attack = resultSet.getInt("attack");
            System.out.println(attack);
            abilityCooldown = resultSet.getInt("ability_cooldown");
            System.out.println(abilityCooldown);
            defenceMultiplier = resultSet.getDouble("defence_multiplier");
            System.out.println(defenceMultiplier);
            minAttackRange = resultSet.getInt("min_attack_range");
            System.out.println(minAttackRange);
            maxAttackRange = resultSet.getInt("max_attack_range");
            System.out.println(maxAttackRange);
            movementRange = resultSet.getInt("movement_range");
            System.out.println(movementRange);

            cleaner.closeResSet(resultSet);
            connectionPool.releaseConnection(myConn);

        }catch (SQLException e){

            e.printStackTrace();
            connectionPool.releaseConnection(myConn);

            return null;
        }

        return new ProtoUnitType(type, hp,attack,abilityCooldown,defenceMultiplier,minAttackRange,maxAttackRange, movementRange, "", "" );
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
                this.username = username; //La inn denne for test metoden, kan hende den ikke skal være her
                return userId;
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
        try {
            ps =con.prepareStatement("SELECT username FROM Users WHERE username = ?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

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
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Cleaner.closeStatement(ps);
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
            if(preparedStatement.executeUpdate() > 0){
                return 1;
            }
            else {
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
