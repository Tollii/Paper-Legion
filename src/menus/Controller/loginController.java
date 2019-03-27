package menus.Controller;

import Runnables.RunnableInterface;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import static Database.Variables.*;

public class loginController extends Controller {

    private boolean changeScene = false;
    private Thread loginThread;

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
    private Label gameTitleLabel;

    @FXML
    void initialize() {

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
                while(keepRunning()){
                    //Logs user in and enter main menu. Currently no info about the user is sent along.
                    int userId = db.login(usernameInput.getText(),passwordInput.getText());
                    if (userId > 0) {
                        setUser_id(userId);
                        Platform.runLater( () ->{
                            changeScene("mainMenu.fxml");
                                });
                        this.doStop();
                    } else {
                        //If the user is not logged in this error is shown. More specificity to what went wrong can be implemented.
                        Platform.runLater(() ->{
                            gameTitleLabel.setText("Login Failed");
                        });
                        this.doStop();
                    }
                }
            }
        };


        newUserButton.setOnAction(event -> {
            //newUserButton.getScene().getWindow().hide();
            changeScene("signUp.fxml");
        });

        loginEnterButton.setOnAction(event -> {
            loginThread = new Thread(loginRunnable);
            loginThread.start();

        });

        loginEnterButton.setDefaultButton(true);
    }
    private void setUser_id(int userIDFromLogin) {
        user_id = userIDFromLogin;
    }
}


