package sample.Controller;

import com.jfoenix.controls.JFXButton;
import dragAndDrop.GameLogic;
import dragAndDrop.Matchmaking;
import dragAndDrop.SetUp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.Main;

import static Database.Variables.db;
import static Database.Variables.user_id;




public class mainMenuController extends Controller{
    private boolean findGameClicked=false;
    Thread thread;
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

        mainMenuGameInfoButton.setOnAction(e ->{
        });


        mainMenuPlayButton.setOnAction(event -> {
            if(findGameClicked){
                mainMenuPlayButton.setText("Play");
                findGameClicked=false;
                shutDownThread = true;
                db.abortMatch(user_id);


            } else{
                thread = new Matchmaking();
                thread.start();
                mainMenuPlayButton.setText("Abort");
                findGameClicked = true;
                shutDownThread=false;
            }


        });
    }

    public static void enterGame() throws Exception{
        SetUp setUp = new SetUp();
        setUp.importUnitTypes();
        GameLogic game = new GameLogic();
        game.start(Main.window);
    }
}
