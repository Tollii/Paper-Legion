package tests;

import database.Cleaner;
import database.ConnectionPool;
import database.Database;
import gameplay.GameLogic;
import javafx.stage.Stage;
import org.junit.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class GameLogicTests {
        private GameLogic instance = new GameLogic();
        int userId = 0;

        @BeforeClass
        public static void setUpClass() throws SQLException {

        }

        @AfterClass
        public static void tearDownClass()  {
        }

        @Before
        public void setUp() throws Exception {
            //lager nytt objekt for hver test



        }

        @After
        public void tearDown() {

        }

        @Test
        public void getPosXFromEventTest(){

        }


}
