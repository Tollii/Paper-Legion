package Database;

import java.sql.*;

public class Cleaner {

    public static void closeResSet(ResultSet res){
        try{
            if(res != null && !res.isClosed()){
                res.close();
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public static void closeStatement(Statement st){
        try{
            if(st != null && !st.isClosed()){
            st.close();
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public static void closeConnection(Connection con){
        try{
            if(con != null && !con.isClosed()){
            con.close();
            }
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public static void rollBack(Connection con){
        try{
            if(con != null && !con.getAutoCommit()){
                con.rollback();
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

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
