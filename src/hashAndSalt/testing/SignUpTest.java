package hashAndSalt.testing;

import hashAndSalt.SignUp;
import hashAndSalt.Login;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;



public class LoginTest {
    private SignUp instance;
    private SignUp loginInstance;


    public LoginTest() {

    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
        //TODO legg inn at man sletter det som ble laget i testen.
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

// TODO catch sql unique hentes exception, test at det som lages kan logges inn i med login. Lag en User.
}


