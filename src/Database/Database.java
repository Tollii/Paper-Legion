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
