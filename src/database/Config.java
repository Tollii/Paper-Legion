package database;

/**
 * Configuration file that contains information needed to connect to the MYSQL database
 * Configuration is set up on program launch
 */

public class Config {

    private String DB_USER_NAME;

    private String DB_PASSWORD;

    private String DB_URL;

    private String DB_DRIVER;

    private int DB_MAX_CONNECTIONS;

    private static Config config = new Config();

    private Config() {
        init();
    }

    public static Config getConfig() {
        return config;
    }
    //jdbc:MySQL://MySQL.stud.iie.ntnu.no:3306/thomabmo/confluence?autoReconnect=true

    public static Config getInstance() {
        return config;
    }

    /**
     * Sets up the MYSQL adress to be readily used
     */

    private void init() {
        DB_USER_NAME = "thomabmo";
        DB_PASSWORD = "EEo6fscj";
        DB_URL = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/thomabmo?autoReconnect=true&useUnicode=yes";
        DB_DRIVER = "com.mysql.cj.jdbc.Driver";
        DB_MAX_CONNECTIONS = 10;
    }

    public  String getDB_USER_NAME() {
        return DB_USER_NAME;
    }

    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public String getDB_DRIVER() {
        return DB_DRIVER;
    }

    public int getDB_MAX_CONNECTIONS() {
        return DB_MAX_CONNECTIONS;
    }
}
