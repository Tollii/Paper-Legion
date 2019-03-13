package hashAndSalt;

import java.security.SecureRandom;

public class Hashing {

    SecureRandom random = new SecureRandom();

    String password;
    byte[] salt = new byte[16];

    public Hashing(String password) {
        this.password = password;



    }
}
