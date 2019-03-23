package Database;

/**
 *
 *
 * We can also use a connection pool framework, so we don't have to make everything from scratch.
 * It's a good idea to make it from scratch to learn, but we can use the frameworks as a backup if it gets too complicated,
 * or for testing purposes
 * Not all methods are tested yet
 *
 * Import mysql-connector
 *
 */

import Database.Config;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool {
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 5;
    private static int MAX_POOL_SIZE = 10;


    // Initializes an ArrayList, then fills it with connections. This is the connectionPool.
    public static BasicConnectionPool create() throws SQLException{
        try{
            Config config = Config.getInstance();
            Class.forName(config.DB_DRIVER);
            List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
            for(int i  = 0; i < INITIAL_POOL_SIZE; i++){
                pool.add(createConnection());
            }
            return new BasicConnectionPool(pool);
        } catch (ClassNotFoundException clne){
            clne.printStackTrace();
        }
        return null;
    }

    public BasicConnectionPool(List<Connection> pool){
        this.connectionPool = pool;
    }

    // Fetches a connection from the connection pool
    // If connection pool is empty, it will create a new connection
    public Connection getConnection(){

        try{
            if(connectionPool.isEmpty()){
                if(usedConnections.size() < MAX_POOL_SIZE){
                    connectionPool.add(createConnection());
                } else{
                    System.out.println("Max pool size reached");
                }
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

        Connection con = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(con);
        System.out.println("Connection opened");
        return con;
    }

    // Will put the connection back into the connection pool and remove it from the usedConnections list
    public boolean releaseConnection(Connection con){
        connectionPool.add(con);
        System.out.println("Connection released");
        return usedConnections.remove(con);
    }

    // Creates a connection
    private static Connection createConnection() throws SQLException{
        Config config = Config.getInstance();
        return DriverManager.getConnection(config.DB_URL, config.DB_USER_NAME, config.DB_PASSWORD);
    }

    // Gets total amount of connections (used and unused)
    public int getSize(){
        return connectionPool.size() + usedConnections.size();
    }


    /**
     *
     * Possible to find a better solution
     *
     */

    public void shutdown() throws SQLException{
        connectionPool.addAll(usedConnections);
        int i = 1;
        for(Connection con : connectionPool){
            con.close();
            System.out.println("Connection " + i + " closed");
            i++;
        }
        System.out.println("All connections closed");
        connectionPool.clear();
        usedConnections = new ArrayList<>();

    }


    public List<Connection> getConnectionPool() {
        return connectionPool;
    }

    public List<Connection> getUsedConnections() {
        return usedConnections;
    }

    public static int getInitialPoolSize() {
        return INITIAL_POOL_SIZE;
    }

    public static int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }

    // Small test that finds ISBN and forfatter from one of the programming exercises and then shuts down the connection pools
    public static void main(String[]args){
/*
        // Does not work with Thomas' database

        BasicConnectionPool pool;

        try{
            pool = create();

            Connection con = pool.getConnection();

            if(con.isValid(1)){
                System.out.println("Success connection 1");
            } else{
                System.out.println("Failure connection 1");
            }


            Statement st = con.createStatement();
            String query = "SELECT username FROM Users";


            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                System.out.println(rs.getString("username"));
            }
            rs.close();

//            Connection con2 = pool.getConnection();
//            Statement st2 = con2.createStatement();
//            String query2 = "SELECT forfatter FROM boktittel";
//            ResultSet rs2 = st2.executeQuery(query2);
//
//            while(rs2.next()){
//                System.out.println(rs2.getString("forfatter"));
//            }
//            rs2.close();


            pool.shutdown();
            System.out.println("Shutdown");

        }   catch (SQLException sqle){
            sqle.printStackTrace();
        }


*/
    }



}
