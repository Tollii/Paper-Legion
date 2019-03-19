package Database;

import dragAndDrop.ProtoUnitType;
import dragAndDrop.UnitType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    //Class variables

    static BasicConnectionPool connectionPool = null;
    static private Cleaner cleaner = new Cleaner();

    public Database(){

        try{
            System.out.println("Creating pool");
            connectionPool = BasicConnectionPool.create();
        }catch (SQLException e){

            //Prints out the error
            e.printStackTrace();
        }

    }

    public static ProtoUnitType importUnitType(String unitNameInput){

        String sqlString = "SELECT * FROM Unit_types WHERE unit_name =" + "'"+unitNameInput+"'";
        Connection myConn = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ////UNIT INFO////
        String type;
        double hp;
        int attack;
        int abilityCooldown;
        double defenceMultiplier;
        int minAttackRange;
        int maxAttackRange;
        int movementRange;

        try{
            preparedStatement = myConn.prepareStatement(sqlString);

            System.out.println("Executing statement");
            resultSet = preparedStatement.executeQuery();
            System.out.println("Statement executed");

            resultSet.next();
            type = resultSet.getString("unit_name");
            System.out.println(type);
            hp = (double)resultSet.getFloat("max_health");
            System.out.println(hp);
            attack = resultSet.getInt("attack");
            System.out.println(attack);
            abilityCooldown = resultSet.getInt("ability_cooldown");
            System.out.println(abilityCooldown);
            defenceMultiplier = resultSet.getDouble("defence_multiplier");
            System.out.println(defenceMultiplier);
            minAttackRange = resultSet.getInt("min_attack_range");
            System.out.println(minAttackRange);
            maxAttackRange = resultSet.getInt("max_attack_range");
            System.out.println(maxAttackRange);
            movementRange = resultSet.getInt("movement_range");
            System.out.println(movementRange);

            cleaner.closeResSet(resultSet);
            connectionPool.releaseConnection(myConn);

        }catch (SQLException e){

            e.printStackTrace();
            connectionPool.releaseConnection(myConn);

            return null;
        }

        return new ProtoUnitType(type, hp,attack,abilityCooldown,defenceMultiplier,minAttackRange,maxAttackRange, movementRange, "", "" );
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

        database.importUnitType("swordsman");

        database.close();
    }


}
