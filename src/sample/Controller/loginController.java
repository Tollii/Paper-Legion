package sample.Controller;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static sample.Main.db;

public class loginController {
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
            newUserButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            changeScene("/sample/View/signUp.fxml");
        });

        loginEnterButton.setOnAction(event -> {
            //Logs user in and enter main menu. Currently no info about the user is sent along.
            int userId = db.login(usernameInput.getText(),passwordInput.getText());
            if (userId > 0) {
                setUser_id(userId);
                changeScene("/sample/View/mainMenu.fxml");
            } else {
                //If the user is not logged in this error is shown. More speficity to what went wrong, can be implemented.
                alertField.setText("Error occured while logging in");
            }
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

    public void setUser_id(int user_id) {
        mainMenuController.user_id = user_id;
    }
}
