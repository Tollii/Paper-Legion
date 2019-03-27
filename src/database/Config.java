package Database;

public class Config {

    public String DB_USER_NAME;

    public String DB_PASSWORD;

    public String DB_URL;

    public String DB_DRIVER;

    public int DB_MAX_CONNECTIONS;

    private static Config config = new Config();

    public Config(){
        init();
    }

    private void init(){
        DB_USER_NAME = "thomabmo";
        DB_PASSWORD = "EEo6fscj";
        DB_URL = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/thomabmo";
        DB_DRIVER = "com.mysql.cj.jdbc.Driver";
        DB_MAX_CONNECTIONS = 10;
    }
    //jdbc:MySQL://MySQL.stud.iie.ntnu.no:3306/thomabmo/confluence?autoReconnect=true

    public static Config getInstance(){
        return config;
    }

}
