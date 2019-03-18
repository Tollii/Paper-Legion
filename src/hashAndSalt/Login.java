package hashAndSalt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;
import Database.BasicConnectionPool;

public class Login {

    public static BasicConnectionPool pool;
    public static Connection myConn;

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
        String stmt = "SELECT hashedpassword,passwordsalt FROM Users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            byte[] hash = rs.getBytes("hashedpassword");
            byte[] salt = rs.getBytes("passwordsalt");
            if (verifyPassword(password, hash, salt)) {
                //TODO SET ONLINE TO 1 IN DB.
                return true;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
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

    private void release() {

    }
}