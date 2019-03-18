package hashAndSalt.testing;

import hashAndSalt.Login;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;



public class LoginTest {
    private Login instance;


    public LoginTest() {

    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
        //lager nytt objekt for hver test
        instance = new Login();
    }

    @After
    public void tearDown() {
        instance.logout();
    }


    @Test
    public void testLogin(){
        System.out.println("testLogin()");
        boolean result = instance.login("admin","admin");
        boolean expResult = true;
        assertEquals(expResult,result);
    }

    @Test
    public void testLoginFail(){
        System.out.println("testLogin()");
        boolean result = instance.login("admin","admong");
        boolean expResult = false;
        assertEquals(expResult,result);
    }

    @Test
    public void testLogout(){
        System.out.println("testLogout()");
        instance.login("admin","admin");
        boolean expResult = true;
        boolean result = instance.logout();
        assertEquals(expResult, result);
    }





    public static void main(String args[]) {
        //org.junit.runner.JUnitCore.main(LoginTest.class.getName());
    }

}

