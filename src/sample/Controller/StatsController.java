package sample.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import static Database.Variables.*;

public class StatsController extends Controller{


    @FXML
    private Label statsUsernameLabel;

    @FXML
    private Label statsEmailLabel;

    @FXML
    private Label statsGamesPlayedLabel;

    @FXML
    private Label statsGamesWonLabel;

    @FXML
    private JFXButton statsBackButton;


    @FXML
    private void initialize(){
        statsBackButton.setOnAction(event -> {
            changeScene("mainMenu.fxml");
        });
        statsUsernameLabel.setText("Username: " + db.getMyName(user_id));
        statsEmailLabel.setText("E-mail: " + db.getMyEmail(user_id));
        statsGamesPlayedLabel.setText("Games played: " + "");
        statsGamesWonLabel.setText("Games won: " + "");


    }


}
