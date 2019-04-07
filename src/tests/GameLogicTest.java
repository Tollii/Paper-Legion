package tests;

import java.sql.ResultSet;
import database.Cleaner;
import database.ConnectionPool;
import database.Database;
import database.Variables;
import gameplay.*;

import org.junit.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static database.Variables.*;
import static gameplay.GameMain.*;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.Cleaner;

//testUser2 Password: testPassword2 id: 51
//testUser password: testPassword id: 3

public class GameLogicTest {
    GameLogic game;
    UnitGenerator units;
    SetUp setUp;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        tileSize = 50;
        testing = true;
        Variables.screenWidth = 1500;
        Database db = new Database();
        grid = new Grid(boardSize, boardSize);


        user_id = 3;
        Variables.opponent_id = 51;
        db.createGame(user_id, "null");

        ConnectionPool connectionPool;
        connectionPool = ConnectionPool.create();
        Connection myConn = connectionPool.getConnection();
        ResultSet rs = null;
        String stmt = "SELECT match_id FROM Matches WHERE game_started=0 AND player1 = 3; ";
        PreparedStatement preparedQuery = null;
        try {
            preparedQuery = myConn.prepareStatement(stmt);
            rs = preparedQuery.executeQuery();
            rs.next();
            match_id = rs.getInt("match_id");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            Cleaner.closeStatement(preparedQuery);
            connectionPool.releaseConnection(myConn);
        }


        db.joinGame(match_id, opponent_id);






    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
        game = new GameLogic();
        setUp = new SetUp();
        setUp.importUnitTypes();
        units = new UnitGenerator();
        obstacles = null;
        selectedUnit = null;
        selectedPosX = -1;
        selectedPosY = -1;
    }

    @After
    public void tearDown() {

    }
//TODO call Tile.SetUnit(unit) for å lage units til testing

    @Test
    //tests if a unit gets the right available movements
    public void testAvailableMovements() {
        Unit testUnit = units.newFriendlyUnit(2);
        grid.tileList[boardSize-1][boardSize-1].setUnit(testUnit);
        //sets up a unit in the corner posistion
        selectedUnit = grid.tileList[boardSize-1][boardSize-1].getUnit();
        selectedPosX = 6;
        selectedPosY = 6;
        Tile[] compare = new Tile[2];
        //It should only be able to move left and up
        compare[0] = grid.tileList[boardSize-2][boardSize-1];
        compare[1] = grid.tileList[boardSize-1][boardSize-2];
        ArrayList<Tile> result = game.getMovementPossibleTiles();
        Tile[] arrayResult = new Tile[result.size()];
        for(int i = 0; i<result.size(); i++){
            arrayResult[i] =  result.get(i);
        }
        assertArrayEquals(compare, arrayResult);
    }

    @Test
    public void CreateObstacle(){
        int lowerBound = 3;
        int upperBound = 7;
        ArrayList<Obstacle> obstacles;
        obstacles = game.createObstacles();
        boolean noObstaclesOutOfBounds = true;
        //Goes through the arraylist and checks if all obstacles are out of the deployable zone
        for(int i = 0; i<obstacles.size(); i++){
            if (obstacles.get(i).getPosY()<1 && obstacles.get(i).getPosY()>boardSize-2){
                noObstaclesOutOfBounds = false;
            }
        }
        assertTrue(lowerBound<=obstacles.size() && upperBound>=obstacles.size() && noObstaclesOutOfBounds);
    }

    @Test
    public void testAvailableAttacksShortRange(){
        //checks the knight, if he can attack diagonally
        //creates units, one in range, one out of it
        Unit testUnit = units.newFriendlyUnit(1);
        Unit testEnemy1 = units.newEnemyUnit(2,0);
        Unit testEnemy2 = units.newEnemyUnit(2,0);
        grid.tileList[boardSize-1][boardSize-1].setUnit(testUnit);
        grid.tileList[boardSize-2][boardSize-2].setUnit(testEnemy1);
        grid.tileList[boardSize-1][boardSize-3].setUnit(testEnemy2);
        selectedUnit = testUnit;
        selectedPosX = 6;
        selectedPosY = 6;
        Tile[] compare = new Tile[1];
        compare[0] = grid.tileList[boardSize-2][boardSize-2];
        ArrayList<Tile> result = game.getAttackableTiles();
        Tile[] resultArray = new Tile[result.size()];
        for(int i = 0; i < result.size(); i++){
          resultArray[i] = result.get(i);
        }
        assertArrayEquals(compare, resultArray);
    }

    @Test
    public void testAvailableAttacksLongRange(){
        //checks the knight, if he can attack diagonally
        //creates units, one in range, one out of it
        Unit testUnit = units.newFriendlyUnit(2);
        Unit testEnemy1 = units.newEnemyUnit(2,0);
        Unit testEnemy2 = units.newEnemyUnit(2,0);
        grid.tileList[boardSize-1][boardSize-1].setUnit(testUnit);
        grid.tileList[boardSize-2][boardSize-2].setUnit(testEnemy1);
        grid.tileList[boardSize-1][boardSize-3].setUnit(testEnemy2);
        selectedUnit = testUnit;
        selectedPosX = 6;
        selectedPosY = 6;
        Tile[] compare = new Tile[1];
        compare[0] = grid.tileList[boardSize-2][boardSize-2];
        ArrayList<Tile> result = game.getAttackableTiles();
        Tile[] resultArray = new Tile[result.size()];
        for(int i = 0; i < result.size(); i++){
            resultArray[i] = result.get(i);
        }
        assertArrayEquals(compare, resultArray);
    }
}
