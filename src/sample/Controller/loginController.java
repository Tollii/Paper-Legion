package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import hashAndSalt.Login;
import hashAndSalt.SignUp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton loginEnterButton;

    @FXML
    private JFXButton newUserButton;

    @FXML
    private Label forgotPasswordButton;

    @FXML
    private JFXTextField usernameInput;

    @FXML
    private JFXPasswordField passwordInput;

    @FXML
    void initialize() {
        newUserButton.setOnAction(event -> {
            newUserButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            changeScene("/sample/View/signUp.fxml");
        });

        loginEnterButton.setOnAction(event -> {
            System.out.println("ENter button");
        });
    }

    public static void changeScene(String fxmldir){
        FXMLLoader loader = new FXMLLoader();
        Class currentClass = new Object() { }.getClass().getEnclosingClass();
        loader.setLocation(currentClass.getResource(fxmldir));
        try{
            loader.load();
        } catch(IOException e){
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
