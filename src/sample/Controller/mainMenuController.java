package sample.Controller;

import com.jfoenix.controls.JFXButton;
import hashAndSalt.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

import static sample.Controller.controllerHelper.*;
import static sample.Main.db;

public class mainMenuController {

    public static int user_id;

    @FXML
    private JFXButton mainMenuPlayButton;

    @FXML
    private JFXButton mainMenuSettingsButtin;

    @FXML
    private JFXButton mainMenuStatsButton;

    @FXML
    private JFXButton mainMenuGameInfoButton;

    @FXML
    private JFXButton mainMenuExitButton;

    @FXML
    private Label mainMenuLoggedInAsLabel;

    @FXML
    void initialize() {
        mainMenuLoggedInAsLabel.setText("Logged in as " + user_id);
        Login log = new Login();

        mainMenuExitButton.setOnAction(event -> {
            db.logout(user_id);
            changeScene("/sample/View/login.fxml");
        });
    }
}
