package menus.Controller;

import Runnables.RunnableInterface;
import com.jfoenix.controls.JFXButton;
import gameplay.GameLogic;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import menus.Main;

import java.net.URL;
import java.util.ResourceBundle;

import static database.Variables.*;

public class mainMenuController extends Controller {
    private boolean findGameClicked, gameEntered, threadStarted = false;

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
    private JFXButton mainMenuCustomMatchButton;

    @FXML
    private JFXButton mainMenuPlayButton;


    @FXML
    void initialize() {

        searchGameRunnable = new RunnableInterface() {
            private boolean doStop = false;

            //TODO bug. Dosnt change scene after returning from a game.

            //TODO bug. Starting a game and going to any other button will cancel it, but the scene switches quickly back mainmenu without prompt.

            @Override
            public void run() {
                while (keepRunning()) {
                    // If user clicks the button while searching for game the matchmaking thread is shut down.
                    if (findGameClicked) {
                        Platform.runLater(() -> {
                            cancelGame();
                            threadStarted = false;
                            searchGameThread = null;
                            changeScene("mainMenu.fxml");
                            this.doStop();
                        });
                    } else {
                        Platform.runLater(() -> {
                            mainMenuPlayButton.setText("Abort");
                        });
                        match_id = db.quickMatch_search(user_id);
                        findGameClicked = true;
                        if (match_id > 0) {
                            // If you join a game, you are player 2.
                            yourTurn = false;
                            Platform.runLater(
                                    () -> {
                                        enterGame();
                                    });
                            this.doStop();
                        }
                        //if none available create own game
                        if (match_id < 0) {
                            match_id = db.createGame(user_id, "null");
                            // If you create the game, you are player 1.
                            yourTurn = true;
                            try {
                                while (!gameEntered && findGameClicked) {
                                    Thread.sleep(1000);
                                    gameEntered = db.pollGameStarted(match_id);

                                    if (gameEntered) {
                                        Platform.runLater(() -> {
                                            enterGame();
                                        });
                                        this.doStop();
                                    }

                                    if(!findGameClicked){
                                        Platform.runLater(()->{
                                            cancelGame();
                                            threadStarted = false;
                                            searchGameThread = null;
                                            changeScene("mainMenu.fxml");
                                        });
                                            this.doStop();
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public synchronized void doStop() {
                this.doStop = true;
            }

            @Override
            public synchronized boolean keepRunning() {
                return !this.doStop;
            }

        };


        mainMenuLoggedInAsLabel.setText("Logged in as " + db.getMyName(user_id));

        //Button handlers

        mainMenuPlayButton.setOnAction(event -> {
            if (!threadStarted) {
                searchGameThread = new Thread(searchGameRunnable);
                threadStarted = true;
                searchGameThread.start();
            }
            if(findGameClicked)findGameClicked = false;

        });

        mainMenuCustomMatchButton.setOnAction(event -> {
            cancelGame();
            changeScene("MatchSetup.fxml");
        });

        // Logs out the current user.
        mainMenuExitButton.setOnAction(event -> {
            cancelGame();
            db.logout(user_id);
            user_id = -1;
            changeScene("login.fxml");
        });

        //Displays Stats and tutorial information.
        mainMenuGameInfoButton.setOnAction(event -> {
            cancelGame();
            changeScene("gameInfo.fxml");
        });

        mainMenuStatsButton.setOnAction(event -> {
            cancelGame();
            changeScene("stats.fxml");
        });

    }

    private void enterGame() {
        try {
            findGameClicked = false;
            GameLogic game = new GameLogic();
            game.start(Main.window);
            System.out.println("Success!!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelGame() {
        if (findGameClicked) {
            findGameClicked = false;
            mainMenuPlayButton.setText("Play");
            db.abortMatch(user_id);
            System.out.println("Game cancelled");
        }
    }
}
