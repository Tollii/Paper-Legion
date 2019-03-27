package tests;

import database.ConnectionPool;
import database.Database;
import org.junit.*;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.Cleaner;

//@FixMethodOrder(MethodSorters.DEFAULT)
public class SignUpLoginTest {
    private Database instance;
    ConnectionPool connectionPool = null;
    int userId = 0;
    @BeforeClass
    public static void setUpClass() throws SQLException {
        ConnectionPool connectionPool;
        connectionPool = ConnectionPool.create();

        Connection myConn = connectionPool.getConnection();
        String stmt = "DELETE FROM Users WHERE username = 'testUserReg'; ";
        PreparedStatement preparedQuery = null;
        try {
            preparedQuery = myConn.prepareStatement(stmt);
            preparedQuery.executeUpdate();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            Cleaner.closeStatement(preparedQuery);
            connectionPool.releaseConnection(myConn);
        }
    }

    @AfterClass
    public static void tearDownClass()  {
    }

    @Before
    public void setUp() {
        //lager nytt objekt for hver test
        instance = new Database();
    }

    @After
    public void tearDown() {
        instance.logout(userId);
        instance.logout(3);
    }

    @Test
    public void testSignUp(){
        System.out.println("testSignUp()");
        int result = instance.signUp("testUserReg", "testPassword", "Email");
        int expResult = 1;
        assertEquals(expResult, result);
    }

    @Test
    public void testSignUpUserExists(){
        System.out.println("testSignUp()");
        int result = instance.signUp("testUser", "testPassword", "Email");
        int expResult = 0;
        assertEquals(expResult, result);
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

    /*
    @Test
    public void testLoginCreatedUser(){
        System.out.println("testLoginCreatedUSer()");
        int result = instance.login("testUserReg","testPassword");
        int minExpResult = 0;
        this.userId = result;
        assertTrue(result>minExpResult);
        }
        */
    //Var med teoretisk hvis man ville sjekke om klassen man lagde funker,
    // men man kan ikke vite hvilken rekkefølge test klassene skjer i, så det vil oftest ikke funke

// TODO catch sql unique hentes exception, test at det som lages kan logges inn i med login. Lag en User.
}


