package database;

import java.io.*;
import java.util.Properties;

/**
 * Configuration file that contains information needed to connect to the MYSQL database
 * Configuration is set up on program launch
 *
 * @see Database
 * @see ConnectionPool
 */
public class Config {

    private String DB_USER_NAME;

    private String DB_PASSWORD;

    private String DB_URL;

    private final static Config config = new Config();

    /**
     * Starts the init() method on object creation.
     */
    private Config() {
        init();
    }

    /**
     * Sets the variables for the database pool connection.
     *
     * @see Database
     */
    private void init() {
        DB_Login();
    }

    /**
     * Returns a Config object
     *
     * @return Config
     * @see Database
     */
    public static Config getInstance() {
        return config;
    }

    /**
     * Returns username as a String variable.
     *
     * @return String
     * @see Database
     */
    public String getDB_USER_NAME() {
        return DB_USER_NAME;
    }

    /**
     * Returns database password as a String
     *
     * @return String
     * @see Database
     */
    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    /**
     * Returns database URL as a String
     *
     * @return String
     * @see Database
     */
    public String getDB_URL() {
        return DB_URL;
    }

    /**
     * Reads a property file that contains the database username and password. Anyone that wants to use this code as is, needs to add this file.
     *
     * @see Database
     */
    private void DB_Login() {
        try {
            FileInputStream fis = new FileInputStream("DatabaseLogin.properties");
            Properties prop = new Properties();
            prop.load(fis);

            DB_USER_NAME = prop.getProperty("username");
            DB_PASSWORD = prop.getProperty("password");
            DB_URL = prop.getProperty("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
