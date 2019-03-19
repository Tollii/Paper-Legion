package sample.Controller;

import Database.Database;
import com.jfoenix.controls.JFXButton;
import hashAndSalt.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

import static sample.Main.db;

public class mainMenuController {

    public static int user_id;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private Label identifyUser;

    @FXML
    void initialize() {
        identifyUser.setText("User ID = " + user_id);
        Login log = new Login();

        logoutButton.setOnAction(event -> {
            db.logout(1);
            changeScene("/sample/View/login.fxml");
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
