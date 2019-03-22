package sample.Controller;

import com.jfoenix.controls.JFXButton;
import dragAndDrop.GameLogic;
import dragAndDrop.SetUp;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Main;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static Database.Variables.*;
import static Database.Variables.match_id;

public class mainMenuController extends Controller {
    private static boolean findGameClicked = false;
    public static boolean startGame = false;
    public static Timer timer = new Timer(true);
    public static boolean gameEntered = false;

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
                }

                mainMenuPlayButton.setText("Abort");

            }
        });

    }

    public static void refresh(){
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    service();
                }
            }, 5000, 5000);
    }

    public static void service() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        if(!gameEntered){
                            if(findGameClicked){
                                    while (!startGame) {
                                        if(!gameEntered){
                                            startGame = db.pollGameStarted(match_id);
                                        }
                                    }
                                }
                            }
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(startGame){
                                        enterGame();
                                        gameEntered = true;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    public static void enterGame() throws Exception {
        SetUp setUp = new SetUp();
        setUp.importUnitTypes();
        //GameLogic game = new GameLogic();
        System.out.println("Succsess!!!!");
        //game.start(Main.window);
    }
}
