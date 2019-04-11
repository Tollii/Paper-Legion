package menus.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import runnables.RunnableInterface;
import com.jfoenix.controls.JFXButton;
import database.Variables;
import gameplay.GameMain;
import gameplay.gameboard.Grid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import menus.Main;

import java.net.URL;
import java.util.ResourceBundle;

import static database.Variables.*;

/**
 * controller class for the main menu scene.
 * Sets up the scene layout, buttons, paddings etc.
 * this class Extends to controller, in able to gain access to ChangeScene method.
 * @see Controller
 */
public class MainMenuController extends Controller {
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
    private ImageView paperLegionLogo;

    @FXML
    private AnchorPane paneforPattern;

    @FXML
    private Pane imageMainMenu;


    @FXML
    private AnchorPane contentPane;


    /**
     * Initialize variables, and is a sort of constructor for the scene setup.
     * @see com.sun.javafx.fxml.builder.JavaFXSceneBuilder
     */
    @FXML
    void initialize() {

        paneforPattern.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());
        imageMainMenu.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());
        contentPane.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());

        //Reset from a previous game.
        game = null;
        opponentReady = false;
        findGameClicked = false;
        gameEntered = false;
        threadStarted = false;
        turn = 1;

        searchGameRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while (keepRunning()) {
                    // If user clicks the button while searching for game the matchmaking thread is shut down.
                    if (findGameClicked) {
                        Platform.runLater(() -> {
                            cancelGame();
                            threadStarted = false;
                            searchGameThread = null;
                            changeScene("MainMenu.fxml");
                            this.doStop();
                        });
                    } else {
                        Platform.runLater(() -> mainMenuPlayButton.setText("Abort Match"));
                        match_id = db.quickMatch_search(user_id);
                        findGameClicked = true;
                        if (match_id > 0) {
                            // If you join a game, you are player 2.
                            Platform.runLater(
                                    () -> {
                                        yourTurn = false;
                                        enterGame();
                                    });
                            this.doStop();
                        }
                        //if none available create own game
                        if (match_id < 0) {
                            match_id = db.createGame(user_id, "null");
                            // If you create the game, you are player 1.
                            try {
                                while (!gameEntered && findGameClicked) {
                                    Thread.sleep(1000);
                                    gameEntered = db.pollGameStarted(match_id);
                                    if (gameEntered) {
                                        Platform.runLater(() -> {
                                            yourTurn = true;
                                            enterGame();
                                        });
                                        this.doStop();
                                    }

                                    if(!findGameClicked){
                                        Platform.runLater(()->{
                                            cancelGame();
                                            threadStarted = false;
                                            searchGameThread = null;
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


        mainMenuLoggedInAsLabel.setText("Logged in as " + db.getMyName());

        //Button handlers

        mainMenuPlayButton.setOnAction(event -> {
            if(threadStarted){
                db.abortMatch(user_id);
            }

            if (!threadStarted) {
                searchGameThread = new Thread(searchGameRunnable);
                threadStarted = true;
                searchGameThread.start();
            }
            if(findGameClicked){
                findGameClicked = false;
                mainMenuPlayButton.setText("Quick Match");
            } else{
                mainMenuPlayButton.setText("Abort Match");
                findGameClicked = true; //added this
            }

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
            changeScene("Login.fxml");
        });

        //Displays Stats and tutorial information.
        mainMenuGameInfoButton.setOnAction(event -> {
            cancelGame();
            changeScene("GameInfo.fxml");
        });

        mainMenuStatsButton.setOnAction(event -> {
            cancelGame();
            changeScene("Stats.fxml");
        });
    }


    /**
     * Changes the scene to GameMain when two players has joined the same game.
     * @see GameMain
     */
    private void enterGame() {
        try {
            findGameClicked = false;
            Variables.tileSize = (int)(screenWidth * 0.07);
            grid = new Grid(boardSize, boardSize);
            game = new GameMain();
            game.start(Main.window);
            System.out.println("Success!!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Aborts the game and resets the button text.
     */
    private void cancelGame() {
        if (findGameClicked) {
            findGameClicked = false;
            mainMenuPlayButton.setText("Play");
            db.abortMatch(user_id);
            System.out.println("Game cancelled");
        }
    }
}
