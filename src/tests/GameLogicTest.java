package tests;

import java.sql.ResultSet;
import database.Cleaner;
import database.ConnectionPool;
import database.Database;
import database.Variables;
import gameplay.*;
import org.junit.*;
import org.junit.Test;
import org.junit.Assert;

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
    GameLogic game = new GameLogic();;
    UnitGenerator units = new UnitGenerator();;
    GameMain gameMain = new GameMain();
    @BeforeClass
    public static void setUpClass() throws SQLException {

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

        SetUp setUp = new SetUp();
        setUp.importUnitTypes();



    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
        GameLogic game = new GameLogic();

    }

    @After
    public void tearDown() {

    }
//TODO call Tile.SetUnit(unit) for å lage units til testing

    @Test
    public void test() {
        Unit testUnit = units.newFriendlyUnit(2);
        grid.tileList[boardSize-1][boardSize-1].setUnit(testUnit);
        selectedUnit = grid.tileList[boardSize-1][boardSize-1].getUnit();
        Tile[] compare = new Tile[2];
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
        //asstert arraylist of obstacles to be greater in length than 0 or whatever
    }
}
