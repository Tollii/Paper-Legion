package Menus.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

import static Database.Variables.db;

public class signUpController extends Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField usernameInput;

    @FXML
    private JFXPasswordField passwordInput;

    @FXML
    private JFXPasswordField confirmPasswordInput;

    @FXML
    private JFXTextField emailInput;

    @FXML
    private JFXButton goBackButton;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private Label alertField;

    @FXML
    void initialize() {
        goBackButton.setOnAction(event -> {
            changeScene("login.fxml");
        });

        signUpButton.setOnAction(event -> {
            //Checks if both password fields are the same.
            if (passwordInput.getText().equals(confirmPasswordInput.getText())) {
                // Tries to register user in database.
                int signup = db.signUp(usernameInput.getText(), passwordInput.getText(), emailInput.getText());
                if (signup > 0) {
                    changeScene("login.fxml");
                }
                else if(signup == 0){
                    alertField.setText("User already exists");
                }

            }
            alertField.setText("Error while signing up.");

        });
    }



}
