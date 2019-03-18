package hashAndSalt.testing;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;

public class HashingTest {

    private static Connection myConn;

    public static void main(String[] args) {
        //This one is just a mess of things we used to test things in the beginning.
        try {
            myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/thomabmo", "thomabmo", "EEo6fscj");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SecureRandom random = new SecureRandom();

        byte[] hash = new byte[0];
        byte[] wrongHash = new byte[0];
        byte[] correctHash = new byte[0];
        byte[] salt = new byte[16];
        byte[] fromDB = new byte[0];

        random.nextBytes(salt);

        String password = "rudeboi69";
        String wrongPwd = "wrongboi68";
        String correctPwd = "rudeboi69";

        password = "admin";

        String stmt = "SELECT hashedpassword,passwordsalt FROM Users WHERE username = 'TestUser'";
        try {
            PreparedStatement preparedStatement = myConn.prepareStatement(stmt);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                fromDB = rs.getBytes("hashedpassword");
                salt = rs.getBytes("passwordsalt");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            hash = factory.generateSecret(spec).getEncoded();

            KeySpec specWrong = new PBEKeySpec(wrongPwd.toCharArray(), salt, 65536, 128);
            wrongHash = factory.generateSecret(specWrong).getEncoded();

            KeySpec specCorrect = new PBEKeySpec(correctPwd.toCharArray(), salt, 65536, 128);
            correctHash = factory.generateSecret(specCorrect).getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        //addUser(hash,salt);

        //System.out.println(addUser(hash,salt));

        System.out.println(Arrays.toString(hash));
        System.out.println(Arrays.toString(wrongHash));
        System.out.println(Arrays.toString(correctHash));
        System.out.println(Arrays.toString(fromDB));

        if (!(Arrays.toString(hash).equals(Arrays.toString(wrongHash)))) {
            System.out.println("Wrong pass did not log in");
        }

        if (Arrays.toString(hash).equals(Arrays.toString(correctHash))) {
            System.out.println("Logged in for svarte helvete");
        }

        if (Arrays.equals(hash,fromDB)) {
            System.out.println("Logged in for svarte helvete");
        }
        
    }

    public static int addUser(byte[] hash, byte[] salt) {
        String stmt = "INSERT INTO Users (username,hashedpassword,passwordsalt,email,online_status) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = myConn.prepareStatement(stmt);
            preparedStatement.setString(1,"admin");
            preparedStatement.setBytes(2,hash);
            preparedStatement.setBytes(3,salt);
            preparedStatement.setString(4,"N/A");
            preparedStatement.setInt(5,0);

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
