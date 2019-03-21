package hashAndSalt.testing;

import Database.Database;
import org.junit.*;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Database.BasicConnectionPool;
import Database.Cleaner;
import org.junit.runners.MethodSorters;

//@FixMethodOrder(MethodSorters.DEFAULT)
public class SignUpTest {
    private Database instance;
    BasicConnectionPool connectionPool = null;
    int userId = 0;
    @BeforeClass
    public static void setUpClass() throws SQLException {
        BasicConnectionPool connectionPool;
        connectionPool = BasicConnectionPool.create();

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


