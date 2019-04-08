package database;

import java.sql.*;

/**
 * A simple class that cleans up after SQL statements.
 *
 */
public class Cleaner {

    /**
     * Closes ResultSets, usually this method is called upon
     * in a finally block
     * @param res Takes in a ResultSet.
     * @see ResultSet
     */
    public static void closeResSet(ResultSet res){
        try{
            if(res != null && !res.isClosed()){
                res.close();
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    /**
     * Closes statements, usually this method is called upon
     * in a finally block
     * @param st Takes in a Statement
     * @see Statement
     */
    public static void closeStatement(Statement st){
        try{
            if(st != null && !st.isClosed()){
            st.close();
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    /**
     * Closes Connection, usually this method is called upon
     * in a finally block
     * @param con Takes in a Connection
     * @see Connection
     */
    public static void closeConnection(Connection con){
        try{
            if(con != null && !con.isClosed()){
            con.close();
            }
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    /**
     * Rollback changes if error occurs, this method has to be called upon
     * in a finally block.
     * @param con Takes in a Connection
     * @see Connection
     */
    public static void rollBack(Connection con){
        try{
            if(con != null && !con.getAutoCommit()){
                con.rollback();
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    /**
     * Sets auto commit = true for given connection, this method can to be called upon
     * in a finally block.
     * @param con Takes in a Connection
     * @see Connection
     */
    public static void setAutoCommit(Connection con){
        try{
            if(con != null && !con.getAutoCommit()){
            con.setAutoCommit(true);
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
