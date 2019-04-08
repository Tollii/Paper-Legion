package database;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple connection pool that is made to be initialized at the start of the program to minimize resource usage
 * during the programs run-time. Starts off with a size of 5 connections.
 * Adds new connections when needed until it reaches 10 connections.
 */


public class ConnectionPool {
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;


    private ConnectionPool(List<Connection> pool){
        this.connectionPool = pool;
    }

    /**
     * Initializes the connection pool. Creates # of connections as INITIAL_POOL_SIZE indicates
     * Gets driver information from the local config object class.
     *
     * @return null on error, or new ConnectionPool on success
     * @throws SQLException
     */
    public static ConnectionPool create() throws SQLException{
        try{
            Config config = Config.getInstance();
            Class.forName(config.getDB_USER_NAME());
            List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
            for(int i  = 0; i < INITIAL_POOL_SIZE; i++){
                pool.add(createConnection());
            }
            return new ConnectionPool(pool);
        } catch (ClassNotFoundException clne){
            clne.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new connection. This is used during initialization or
     * when INITIAL_POOL_SIZE < # of connections < MAX_POOL_SIZE
     *
     * @return a newly created connection
     * @throws SQLException
     */
    private static Connection createConnection() throws SQLException{
        Config config = Config.getInstance();
        return DriverManager.getConnection(config.getDB_URL(), config.getDB_USER_NAME(), config.getDB_PASSWORD());
    }

    /**
     * Fetches a connection from the connection pool
     * If connection pool is empty, it will create a new connection
     * Puts the used connection in the usedConnections list, and removes it from the connectionPool list
     *
     * @return a connection on success, null on failure
     */
    public Connection getConnection(){

        try{
            if(connectionPool.isEmpty()){
                if(usedConnections.size() < MAX_POOL_SIZE){
                    connectionPool.add(createConnection());
                } else{
                    System.out.println("Max pool size reached");
                    return null;
                }
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

        Connection con = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(con);
        //System.out.println("Connection opened");
        return con;
    }

    /**
     * Puts the connection back into the connectionPool after usage.
     *
     * @param con A connection
     * @return boolean value of whether usedConnections.remove was successfull
     */
    public boolean releaseConnection(Connection con){
        connectionPool.add(con);
        //System.out.println("Connection released");
        return usedConnections.remove(con);
    }

    /**
     * A simple getter that returns # of initialized connections.
     * @return # of connections
     */
    public int getSize(){
        return connectionPool.size() + usedConnections.size();
    }


    /**
     * Shuts down all connections.
     * Adds all used connections back into the connection pool, then proceeds to close all of them.
     * Clears the lists to make sure they are all empty.
     * Used during the shutdown of the program
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
}
