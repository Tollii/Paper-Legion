package tests;

import gameplay.GameLogic;
import org.junit.*;

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
