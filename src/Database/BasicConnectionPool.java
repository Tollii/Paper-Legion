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


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool {

    private String url;
    private String user;
    private String password;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 10;
    private static int MAX_POOL_SIZE = 20;


    // Initializes an ArrayList, then fills it with connections. This is the connectionPool.
    public static BasicConnectionPool create(String url, String user, String password) throws SQLException{



        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for(int i  = 0; i < INITIAL_POOL_SIZE; i++){
            pool.add(createConnection(url, user, password));
        }
        return new BasicConnectionPool(url, user, password, pool);
    }


    public BasicConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = connectionPool;

    }

    // Fetches a connection from the connection pool
    // If connection pool is empty, it will create a new connection
    @Override
    public Connection getConnection(){
/*
        try{

            if(connectionPool.isEmpty()){
                if(usedConnections.size() < MAX_POOL_SIZE){
                    connectionPool.add(createConnection(url, user, password));
                }
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
*/
        Connection con = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(con);
        return con;
    }

    // Will put the connection back into the connection pool and remove it from the usedConnections list
    @Override
    public boolean releaseConnection(Connection con){
        connectionPool.add(con);
        return usedConnections.remove(con);
    }

    private static Connection createConnection(String url, String user, String password) throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }

    // Gets total amount of connections (used and unused)
    public int getSize(){
        return connectionPool.size() + usedConnections.size();
    }


    /**
     *
     * Does not work at the moment. Throws an exception when it calls releaseConnection()
     *
     */

    public void shutdown() throws SQLException{
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool){
            c.close();
        }
        connectionPool.clear();
    }



    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
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

    public static void main(String[]args){

        String adr = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/andrtoln";
        String user = "andrtoln";
        String pw = "GVAXiZju";
        String driver1 = "org.apache.derby.jdbc.ClientDriver";
        String driver2 = "com.mysql.cj.jdbc.Driver";
        BasicConnectionPool pool;


        try{
            Class.forName(driver2);

            pool = create(adr, user, pw);

            Connection con = pool.getConnection();

            if(con.isValid(1)){
                System.out.println("Success");
            } else{
                System.out.println("Failure");
            }


            Statement st = con.createStatement();
            String query = "SELECT isbn FROM boktittel";


            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                System.out.println(rs.getString("isbn"));
            }
            rs.close();
            con.close();
            pool.releaseConnection(con);





            //  pool.shutdown();
            System.out.println("Shutdown");

        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }


    }




}
