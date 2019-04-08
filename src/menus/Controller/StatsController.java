package menus.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import static database.Variables.*;
/**
 * Controller class for Statistics scene in main menu. Uses methods db.getMyName(),
 * db.getMyEmail(), db.getGamesPlayed(), db.getGamesWon().
 * This class is used to show the information of a player's previous games.
 * @see database.Database
 */
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
    private ImageView paperLegionLogo;


    @FXML
    /**
     * Initialize variables, and is a sort of constructor for the scene setup.
     * @see com.sun.javafx.fxml.builder.JavaFXSceneBuilder
     */
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
