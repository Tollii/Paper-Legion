package menus.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import database.Variables.*;

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


    /**
     * Initialize variables, and is a sort of constructor for the scene setup.
     * @see com.sun.javafx.fxml.builder.JavaFXSceneBuilder
     */
    @FXML
    private void initialize(){
        statsBackButton.setOnAction(event -> {
            changeScene("mainMenu.fxml");
        });
        statsUsernameLabel.setText("Username: " + db.getMyName());
        statsEmailLabel.setText("E-mail: " + db.getMyEmail());
        statsGamesPlayedLabel.setText("Games played: " + db.getGamesPlayed());
        statsGamesWonLabel.setText("Games won: " + db.getGamesWon());


    }


}
