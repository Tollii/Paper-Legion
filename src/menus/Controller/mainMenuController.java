package menus.Controller;

import Runnables.RunnableInterface;
import com.jfoenix.controls.JFXButton;
import gameplay.GameLogic;
import gameplay.SetUp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import menus.Main;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import static database.Variables.*;

public class mainMenuController extends Controller {
    private static boolean findGameClicked = false;
    public static boolean startGame = false;
    public static Timer timer = new Timer(true);
    public static boolean gameEntered = false;
    private Thread searchGameThread;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label mainMenuLoggedInAsLabel;

    @FXML
    private JFXButton mainMenuGameInfoButton;

    @FXML
    private JFXButton mainMenuExitButton;

    @FXML
    private JFXButton mainMenuStatsButton;

    @FXML
    private Label gameTitleLabel;

    @FXML
    private JFXButton mainMenuSettingsButton;

    @FXML
    private JFXButton mainMenuPlayButton;




    @FXML
    void initialize() {

        RunnableInterface searchGameRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while(keepRunning()){
                    // If user clicks the button while searching for game the matchmaking thread is shut down.
                    if (findGameClicked) {
                        Platform.runLater(() -> {
                            findGameClicked = false;
                            mainMenuPlayButton.setText("Play");
                            db.abortMatch(user_id);
                        });

                        this.doStop();
                    } else {
                        Platform.runLater(() -> {
                            mainMenuPlayButton.setText("Abort");
                        });
                        match_id = db.matchMaking_search(user_id);
                        findGameClicked = true;
                        if(match_id > 0) {
                            // If you join a game, you are player 2.
                            yourTurn = false;
                            startGame = true;
                            Platform.runLater(
                                    () ->{
                                        enterGame();
                                    });
                            this.doStop();
                        }
                        //if none available create own game
                        if (match_id < 0) {
                            match_id = db.createGame(user_id);
                            // If you create the game, you are player 1.
                            yourTurn = true;
                            try {
                                while(!gameEntered){
                                    Thread.sleep(3000);
                                    gameEntered = db.pollGameStarted(match_id);
                                    if(gameEntered){
                                        Platform.runLater(()->{
                                            enterGame();
                                        });

                                        this.doStop();
                                    }

                                }
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public synchronized void doStop(){
                this.doStop = true;
            }

            @Override
            public synchronized boolean keepRunning(){
                return !this.doStop;
            }

        };



        mainMenuLoggedInAsLabel.setText("Logged in as " + db.getMyName(user_id));

        // Logs out the current user.
        mainMenuExitButton.setOnAction(event -> {
            db.logout(user_id);
            changeScene("login.fxml");
        });

        //Displays Stats and tutorial information.
        mainMenuGameInfoButton.setOnAction(e -> {
        });

        mainMenuPlayButton.setOnAction(event -> {
            searchGameThread = new Thread(searchGameRunnable);
            searchGameThread.start();

        });

        mainMenuGameInfoButton.setOnAction( event -> {
            changeScene("gameInfo.fxml");

        });

        mainMenuStatsButton.setOnAction(event -> {
            changeScene("stats.fxml");
        });

    }


    public static void enterGame(){
        try{
            SetUp setUp = new SetUp();
            setUp.importUnitTypes();
            GameLogic game = new GameLogic();
            game.start(Main.window);
            System.out.println("Success!!!!");
        } catch(Exception e){
            e.printStackTrace();
        }
    }





}
