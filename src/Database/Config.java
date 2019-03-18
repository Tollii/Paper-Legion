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
        DB_USER_NAME = "andrtoln";
        DB_PASSWORD = "GVAXiZju";
        DB_URL = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/andrtoln";
        DB_DRIVER = "com.mysql.cj.jdbc.Driver";
        DB_MAX_CONNECTIONS = 10;
    }

    public static Config getInstance(){
        return config;
    }

}
