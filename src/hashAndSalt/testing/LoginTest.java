package hashAndSalt.testing;

import Database.Database;
import org.junit.*;

import static org.junit.Assert.*;

public class LoginTest {
    private Database instance;

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
        //lager nytt objekt for hver test
        instance = new Database();
    }

    @After
    public void tearDown() {
        instance.logout(3);
    }

    @Test
    public void testLogin(){
        System.out.println("testLogin()");
        int result = instance.login("testUser","testPassword");
        int expResult = 3;
        assertEquals(expResult,result);
    }

    @Test
    public void testLoginFailedPassword(){
        System.out.println("testLogin()");
        int result = instance.login("testUser","admong");
        int expResult = -1;
        assertEquals(expResult,result);
    }

    @Test
    public void testLoginWrongUsername(){
        System.out.println("testLogin()");
        int result = instance.login("testUser2","admin");
        int expResult = -1;
        assertEquals(expResult,result);
    }

    @Test
    public void testLogout(){
        System.out.println("testLogout()");
        instance.login("testUser","testPassword");
        boolean expResult = true;
        boolean result = instance.logout(3);
        assertEquals(expResult, result);
    }
}

