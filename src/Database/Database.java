package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    //Class variables

    BasicConnectionPool connectionPool = null;

    public Database(){

        try{
            System.out.println("Creating pool");
            connectionPool = BasicConnectionPool.create();
        }catch (SQLException e){

            //Prints out the error
            e.printStackTrace();

        }
    }

    public int matchMaking_search(int player_id){
        Connection myConn = connectionPool.getConnection();
        String sqlString = "SELECT * FROM Matches where game_started=0";
        ResultSet results;

        try {
            PreparedStatement ps  = myConn.prepareStatement(sqlString);
            results = ps.executeQuery();
            int match_id=-1;
            while(results.next()){
                match_id = results.getInt(1);
            }

            if(match_id>0){
                System.out.println("Match Found: " + match_id);
                joinGame(match_id,player_id);
                connectionPool.releaseConnection(myConn);
                return match_id;
            } else{
                return -1;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            connectionPool.releaseConnection(myConn);
            return -1;
        }
    }

    public boolean joinGame(int match_id, int player2){
        Connection myConn = connectionPool.getConnection();
        String sqlSetning = "update Matches set player2=?, game_started=1 where match_id=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1,player2);
            preparedStatement.setInt(2,match_id);
            int resultSet = preparedStatement.executeUpdate();
            if(resultSet == 1){
                System.out.println("Joined game");
                connectionPool.releaseConnection(myConn);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connectionPool.releaseConnection(myConn);
        }
        return false;
    }

    public int createGame(int player_id){
        Connection myConn = connectionPool.getConnection();
        String sqlSetning= "insert into Matches(match_id, player1, player2, game_started) values (default,?,null,0);";
        try {
            PreparedStatement preparedStatement = myConn.prepareStatement(sqlSetning);
            preparedStatement.setInt(1,player_id);
            int result = preparedStatement.executeUpdate();
            if(result>0) {
                System.out.println("Created Game");
            }
            connectionPool.releaseConnection(myConn);
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            connectionPool.releaseConnection(myConn);
            return -1;
        }

    }

    public void test(){

        System.out.println("Test begun");
        String sqlString = "SELECT username FROM Users";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = myConn.prepareStatement(sqlString);

            System.out.println("Executing statement");
            resultSet = preparedStatement.executeQuery();
            System.out.println("Statement executed");

            while (resultSet.next()){

                System.out.println(resultSet.getString("username"));
            }

            connectionPool.releaseConnection(myConn);
        }catch (SQLException e){

            e.printStackTrace();
            connectionPool.releaseConnection(myConn);
        }
    }

    public void close() throws SQLException{
        connectionPool.shutdown();

    }

    public static void main(String[] args) throws SQLException{

        Database database = new Database();

        database.test();

        database.close();
    }


}
