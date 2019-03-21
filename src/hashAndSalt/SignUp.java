package hashAndSalt;

import Database.BasicConnectionPool;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;

public class SignUp {

    private Connection myConn = Login.pool.getConnection();
    private SecureRandom random = new SecureRandom();
    //JDBC connection

    public boolean signUp(String user, String password,String email) {


        //random is used to generate salt by creating a unique set of bytes.
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        byte[] hash = generateHash(password,salt);

        return addUserToDatabase(user,email,hash,salt);
    }

    private byte[] generateHash(String password,byte[] salt) {
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

    private boolean addUserToDatabase(String username, String email, byte[] hash, byte[] salt) {
        String stmt = "INSERT INTO Users (username,hashedpassword,passwordsalt,email,online_status) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1, username);
            preparedStatement.setBytes(2, hash);
            preparedStatement.setBytes(3, salt);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, 0);

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
