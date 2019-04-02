package tests;

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

import static org.junit.Assert.*;

//testUser2 Password: testPassword2
//testUser password: testPassword

public class GameLogicTest {

    @BeforeClass
    public static void setUpClass() throws SQLException {

        Database db = new Database();

        SetUp setUp = new SetUp();
        setUp.importUnitTypes();
        GameLogic game = new GameLogic();

        database.Variables.user_id = 3;
        Variables.opponent_id = 2;
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
