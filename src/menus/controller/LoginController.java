package menus.controller;

import javafx.scene.layout.AnchorPane;
import runnables.RunnableInterface;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import static database.Variables.*;


/**
 * controller class for the login scene, it sets up layout and buttons for the scene.
 * It also uses threads for running background database checks to see if user input of
 * password and username is correct. If user information is correct then it will change scene to
 * main menu, if not it will display a alert label.
 * @see Controller Change Scenes
 * @see Thread
 */
public class LoginController extends Controller {

    private boolean loginPressed = false;
    private boolean changeScene = false;
    private Thread loginThread;
    private boolean executed = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton loginEnterButton;

    @FXML
    private JFXButton newUserButton;

    //Forgot password. Does nothing right now.
    @FXML
    private Label forgotPasswordButton;

    @FXML
    private JFXTextField usernameInput;

    @FXML
    private JFXPasswordField passwordInput;

    @FXML
    private Label alertField;

    @FXML
    private ImageView paperLegionLogo;

    @FXML
    private AnchorPane paneforPattern;


    @FXML
    private AnchorPane contentPane;



    /**
     * Initialize variables, and is a sort of constructor for the scene setup.
     * @see com.sun.javafx.fxml.builder.JavaFXSceneBuilder
     */
    @FXML
    void initialize() {


        paneforPattern.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());
        contentPane.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());


        // Thread to log in so program doesn't freeze up when getting data from the database

        RunnableInterface loginRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public synchronized void doStop() {
                this.doStop = true;
            }

            @Override
            public synchronized boolean keepRunning() {
                return !doStop;
            }

            @Override
            public void run() {
                while (keepRunning()) {
                    if (loginPressed) {
                        //Logs user in and enter main menu. Currently no info about the user is sent along.
                        int userId = db.login(usernameInput.getText(), passwordInput.getText());
                        if (userId > 0) {
                            setUser_id(userId);
                            Platform.runLater(() -> changeScene("MainMenu.fxml"));
                            loginPressed = false;
                            this.doStop();
                        } else {
                            //If the user is not logged in this error is shown. More specificity to what went wrong can be implemented.
                            Platform.runLater(() -> {
                                loginPressed = false;
                                alertField.setText("Login failed");
                            });
                        }
                    }
                }
            }
        };

        loginThread = new Thread(loginRunnable);

        newUserButton.setOnAction(event ->
            changeScene("SignUp.fxml"));

        loginEnterButton.setOnAction(event -> {
            loginPressed = true;
            if (!executed) {
                executed = true;
                loginThread.start();
            }
        });

        loginEnterButton.setDefaultButton(true);

    }
    /**
     * Sets a static variable user_id from the user that logged in.
     * @param userIDFromLogin Sets the static variable (int) user_id for the user logged in.
     * @see database.Variables
     */
    private void setUser_id(int userIDFromLogin) {
        user_id = userIDFromLogin;
    }
}


