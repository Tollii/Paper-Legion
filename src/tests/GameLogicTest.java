package tests;

import java.sql.ResultSet;
import database.Cleaner;
import database.ConnectionPool;
import database.Database;
import database.Variables;
import database.Variables.*;
import gameplay.GameLogic;
import gameplay.SetUp;
import menus.Main;
import org.junit.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static database.Variables.*;
import static org.junit.Assert.*;

//testUser2 Password: testPassword2 id: 51
//testUser password: testPassword id: 3

public class GameLogicTest {

    @BeforeClass
    public static void setUpClass() throws SQLException {

        Database db = new Database();

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

        GameLogic game = new GameLogic();

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void test() {

    }
}
