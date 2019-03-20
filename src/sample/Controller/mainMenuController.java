package sample.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import static Database.Variables.db;

public class mainMenuController extends Controller {

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

        mainMenuExitButton.setOnAction(event -> {
            db.logout(user_id);
            changeScene("/sample/View/login.fxml");
        });
    }
}
