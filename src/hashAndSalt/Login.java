package hashAndSalt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;

public class Login {

    private static Connection myConn;

    public Login() {
        try {
            myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/thomabmo", "thomabmo", "EEo6fscj");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                //SET ONLINE TO 1 IN DB.
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
}