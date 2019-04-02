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

//testUser2 Password: testPassword2 51
//testUser password: testPassword   3

public class GameLogicTest {

    @BeforeClass
    public static void setUpClass() throws SQLException {

        SetUp setUp = new SetUp();
        setUp.importUnitTypes();

        GameLogic game = new GameLogic();

        Variables.user_id = 3;
        Variables.opponent_id = 51;
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
