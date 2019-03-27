package Menus.Controller;

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
        statsGamesPlayedLabel.setText("Games played: " + db.getGamesPlayed(user_id));
        statsGamesWonLabel.setText("Games won: " + db.getGamesWon(user_id));


    }


}
