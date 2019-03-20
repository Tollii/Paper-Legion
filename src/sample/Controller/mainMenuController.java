package sample.Controller;

import com.jfoenix.controls.JFXButton;
import dragAndDrop.Matchmaking;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import static Database.Variables.db;
import static Database.Variables.user_id;




public class mainMenuController extends Controller{
    private boolean findGameClicked=false;
    Thread thread = new Matchmaking();
    public static boolean shutDownThread = false;




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

        mainMenuPlayButton.setOnAction(event -> {
            if(findGameClicked){
                mainMenuPlayButton.setText("Find game");
                findGameClicked=false;
                shutDownThread = true;
                db.abortMatch(user_id);


            } else{
                thread.start();
                mainMenuPlayButton.setText("Abort");
                findGameClicked = true;
            }


        });
    }

}
