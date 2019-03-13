package hashAndSalt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class SignUp {

    SecureRandom random = new SecureRandom();

    String user;
    String password;
    byte[] salt = new byte[16];

    public SignUp(String user, String password) {

        this.user = user;
        this.password = password;

        //Check if user is not already registered, or username is taken.

        byte[] hash = generateHash(password);


    }

    private byte[] generateHash(String password) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


}
