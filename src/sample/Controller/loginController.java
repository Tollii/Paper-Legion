package sample.Controller;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController extends Controller {

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
    void initialize() {

        newUserButton.setOnAction(event -> {
            //newUserButton.getScene().getWindow().hide();
            changeScene("/sample/View/signUp.fxml");
        });

        loginEnterButton.setOnAction(event -> {
            //Logs user in and enter main menu. Currently no info about the user is sent along.
            //int userId = db.login(usernameInput.getText(),passwordInput.getText());
            //TODO CHANGE THIS AFTER TESTING.
            int userId = 1;
            if (userId > 0) {
                setUser_id(userId);
                changeScene("/sample/View/mainMenu.fxml");
                System.out.println("Change scene");
            } else {
                //If the user is not logged in this error is shown. More specificity to what went wrong can be implemented.
                alertField.setText("Login Failed");
            }
        });
    }
    private void setUser_id(int user_id) {
        mainMenuController.user_id = user_id;
    }
}
