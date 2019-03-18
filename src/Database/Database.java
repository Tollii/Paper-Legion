package Database;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Database {

    //Class variables

    BasicConnectionPool connectionPool = null;

    public Database(){

        try{
            System.out.println("Creating pool");
            connectionPool = BasicConnectionPool.create();
        }catch (SQLException e){
            //Prints out the error
            e.printStackTrace();
        }
    }

    public void test(){

        System.out.println("Test begun");
        String sqlString = "SELECT username FROM Users";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = myConn.prepareStatement(sqlString);

            System.out.println("Executing statement");
            resultSet = preparedStatement.executeQuery();
            System.out.println("Statement executed");

            while (resultSet.next()){

                System.out.println(resultSet.getString("username"));
            }

            connectionPool.releaseConnection(myConn);
        }catch (SQLException e){

            e.printStackTrace();
            connectionPool.releaseConnection(myConn);
        }
    }

    //Should probably return more than a boolean so we can identify who is logged in.
    public boolean login (String username, String password) {

        Connection myConn = connectionPool.getConnection();

        //SELECT statement finds hashed password and salt from the entered user.
        String stmt = "SELECT hashedpassword,passwordsalt,online_status FROM Users WHERE username = ?";
        String stmt2 = "UPDATE Users SET online_status = 1 WHERE username = ?";

        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet rs = null;
        try {
            preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1, username);

            rs = preparedStatement.executeQuery();
            rs.next();
            byte[] hash = rs.getBytes("hashedpassword");
            byte[] salt = rs.getBytes("passwordsalt");
            int loginStatus = rs.getInt("online_status");

            //Checks if the user is already logged in. If not the user is logged in.
            if (verifyPassword(password, hash, salt) && loginStatus == 0) {
                preparedStatement2 = myConn.prepareStatement(stmt2);
                preparedStatement2.setString(1, username);
                preparedStatement2.executeUpdate();
                connectionPool.releaseConnection(myConn);
                return true;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            connectionPool.releaseConnection(myConn);
        }
        return false;
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

    public boolean logout(String username) {
        //Logs out the user. Sets online_status to 0.
        Connection myConn = connectionPool.getConnection();
        String stmt = "UPDATE Users SET online_status = 0 WHERE username = ?";
        try {
            PreparedStatement preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1, username);

            preparedStatement.executeUpdate();
            connectionPool.releaseConnection(myConn);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.releaseConnection(myConn);
        return false;
    }

    public void close() throws SQLException{
        connectionPool.shutdown();

    }

    public static void main(String[] args) throws SQLException{

        Database database = new Database();

        database.test();

        database.close();
    }


}
