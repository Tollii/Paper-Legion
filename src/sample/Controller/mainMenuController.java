package sample.Controller;

import com.jfoenix.controls.JFXButton;
import dragAndDrop.GameLogic;
import dragAndDrop.Matchmaking;
import dragAndDrop.SetUp;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Main;
import java.util.concurrent.CountDownLatch;

import static Database.Variables.db;
import static Database.Variables.user_id;

public class mainMenuController extends Controller {
    private boolean findGameClicked = false;
    Thread thread;
    public static boolean shutDownThread = false;
    public static boolean startGame = false;

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
                shutDownThread = true;
                db.abortMatch(user_id);
            } else {
                // User clicks play button and the application starts Matchmaking and then starts game once players are found.
                thread = new Matchmaking();
                thread.start();
                mainMenuPlayButton.setText("Abort");
                findGameClicked = true;
                shutDownThread = false;

                //Start game and go to game screen would go here.
            }
        });

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //FX Stuff done here
                                    System.out.println("test");
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

    public void refresh() {

    }

    public static void enterGame() throws Exception {
        SetUp setUp = new SetUp();
        setUp.importUnitTypes();
        GameLogic game = new GameLogic();
        game.start(Main.window);
    }
}
