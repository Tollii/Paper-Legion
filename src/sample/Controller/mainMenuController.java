package sample.Controller;

import com.jfoenix.controls.JFXButton;
import hashAndSalt.Login;
import javafx.fxml.FXML;

public class mainMenuController {

    @FXML
    private JFXButton logoutButton;

    @FXML
    void initialize() {
        Login log = new Login();
        logoutButton.setOnAction(event -> {
            log.logout();
        });
    }

}
