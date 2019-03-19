package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

import static sample.Controller.controllerHelper.*;
import static sample.Main.db;

public class signUpController {

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
            goBackButton.getScene().getWindow().hide();
            changeScene("/sample/View/login.fxml");
        });

        signUpButton.setOnAction(event -> {
            //Checks if both password fields are the same.
            if (passwordInput.getText().equals(confirmPasswordInput.getText())) {
                // Tries to register user in database.
                int signup = db.signUp(usernameInput.getText(), passwordInput.getText(), emailInput.getText());
                if (signup > 0) {
                    changeScene("/sample/View/login.fxml");
                }
            } else {
                alertField.setText("Error while signing up.");
            }
        });
    }



}
