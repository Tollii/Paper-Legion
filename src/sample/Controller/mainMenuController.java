package sample.Controller;

import com.jfoenix.controls.JFXButton;
import dragAndDrop.SetUp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLOutput;
import java.util.Timer;
import static Database.Variables.*;
import static Database.Variables.match_id;

public class mainMenuController extends Controller {
    private static boolean findGameClicked = false;
    public static boolean startGame = false;
    public static Timer timer = new Timer(true);
    public static boolean gameEntered = false;
    private Thread thread;
    @FXML
    private JFXButton mainMenuPlayButton;

    @FXML
    private JFXButton mainMenuSettingsButton;

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

        // Logs out the current user.
        mainMenuExitButton.setOnAction(event -> {
            db.logout(user_id);
            changeScene("login.fxml");
        });

        //Displays Stats and tutorial information.
        mainMenuGameInfoButton.setOnAction(e -> {
        });

        mainMenuPlayButton.setOnAction(event -> {
            // If user clicks the button while searching for game the matchmaking thread is shut down.
            if (findGameClicked) {
                mainMenuPlayButton.setText("Play");
                findGameClicked = false;
                db.abortMatch(user_id);
            } else {
                findGameClicked = true;
                match_id = db.matchMaking_search(user_id);
                if(match_id > 0){
                    startGame = true;
                }
                //if none available create own game
                if (match_id < 0) {
                    match_id = db.createGame(user_id);
                    thread = new Thread() {
                        public void run(){
                            try {
                                while(!gameEntered){
                                    Thread.sleep(3000);
                                    gameEntered = db.pollGameStarted(match_id);
                                }
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
                mainMenuPlayButton.setText("Abort");
            }
        });

    }


    public static void enterGame() throws Exception {
        SetUp setUp = new SetUp();
        setUp.importUnitTypes();
        //GameLogic game = new GameLogic();
        System.out.println("Succsess!!!!");
        //game.start(Main.window);
    }
}
