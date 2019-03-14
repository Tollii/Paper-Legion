package hashAndSalt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;

public class SignUp {

    private static Connection myConn;
    private SecureRandom random = new SecureRandom();
    private byte[] salt = new byte[16];

    public SignUp() {
        try {
            myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/thomabmo", "thomabmo", "EEo6fscj");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean signUp(String user, String password) {

        //TODO Check if user is not already registered, or username is taken.

        byte[] hash = generateHash(password);

        addUserToDatabase();
    }

    public boolean login(String username, String password) {

        //SELECT statement finds hashed password and salt from the entered user.
        String stmt = "SELECT hashedpassword,passwordsalt FROM Users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1,username);

            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            byte[] hash = rs.getBytes("hashedpassword");
            byte[] salt = rs.getBytes("passwordsalt");

            if (verifyPassword(password,hash,salt)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean verifyPassword(String password,byte[] hash,byte[] salt) {
        //TODO
        return true;
    }

    private void addUserToDatabase() {

        //TODO
    }

    private byte[] generateHash(String password) {
        //random is used to create a unique set of bytes.
        random.nextBytes(salt);
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
}
