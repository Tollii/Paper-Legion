package hashAndSalt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;
import Database.BasicConnectionPool;
import Database.Cleaner;

public class Login {

    //Planen atm er å flytte hel login inn i database og aksessere sql statements

    public static BasicConnectionPool pool;
    public static Connection myConn;
    private String username;

    public Login() {
        try {
            pool = BasicConnectionPool.create();
            myConn = pool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Should probably return more than a boolean so we can identify who is logged in.
    public boolean login(String username, String password) {

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
                this.username = username;
                Cleaner.closeStatement(preparedStatement);
                Cleaner.closeStatement(preparedStatement2);
                return true;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        } finally {
            Cleaner.closeStatement(preparedStatement);
            Cleaner.closeStatement(preparedStatement2);
            Cleaner.closeResSet(rs);
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

    public boolean logout() {
        //Logs out the user. Sets online_status to 0.
        String stmt = "UPDATE Users SET online_status = 0 WHERE username = ?";
        try {
            PreparedStatement preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1, username);

            preparedStatement.executeUpdate();
            Cleaner.closeStatement(preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

}