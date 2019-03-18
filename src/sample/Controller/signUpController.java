package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static sample.Controller.loginController.changeScene;

public class signUpController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXPasswordField confirmPasswordInput;

    @FXML
    private JFXTextField usernameInput;

    @FXML
    private JFXButton goBackButton;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private JFXPasswordField passwordInput;

    @FXML
    private JFXTextField emailInput;

    @FXML
    void initialize() {
        goBackButton.setOnAction(event -> {
            goBackButton.getScene().getWindow().hide();
            changeScene("/sample/View/login.fxml");

        });
    }



}
