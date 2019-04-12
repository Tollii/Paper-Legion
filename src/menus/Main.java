package menus;

import database.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import runnables.RunnableInterface;

import java.sql.SQLException;

import static database.Variables.*;
/**
 * The main class of the program, which calls the main method which then again calls the
 * start method. This class sets up the stage and scene. This class extends to Application
 * which inherits automatically a lot of variables and methods to make the GUI.
 */
public class Main extends Application {

    public static Stage window;
    public static Scene rootScene;

    /**
     * Start method is called by main method on program launch, and sets up the database connection,
     * the stage, and the scene.
     * @param primaryStage Takes in a stage from Application.
     * @throws Exception throws out execeptions.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        
        //database is a static class that starts when the application starts. All queries from the the database goes through it.
        db = new Database();
        Parent root = FXMLLoader.load(getClass().getResource("/menus/fxml/Login.fxml"));
        rootScene = new Scene(root, 850, 650);

        window.setTitle("Paper Legion");
        window.setScene(rootScene);
        window.setMinWidth(1024);
        window.setMinHeight(768);

        //Finds screen size and sizes window to it.
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
        window.setWidth(screenWidth);
        window.setHeight(screenHeight);
        databaseNoTimeout();
        window.show();
        databaseNoTimeoutThread = new Thread(databaseNoTimeoutRunnable);
        databaseNoTimeoutThread.start();
    }

    /**
     * The main method of the program, launches method start() and a thread to check if user quits the program, which
     * will close and log out users from the database
     * @param args Takes in arguments for the main method.
     */
    public static void main(String[] args) {

        //Shutdown hook. Closes stuff when program exits.
        Runtime.getRuntime().addShutdownHook(new Thread(Main::closeAndLogout));
        launch(args);
    }

    /**
     * Runs in the background while the program is open, and executes an select statement after a set amount of time to keep the database from timing out.
     *
     */
    private void databaseNoTimeout() {
        databaseNoTimeoutRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while (keepRunning()) {
                    try {
                        Thread.sleep(150000);
                        db.keepTheConnectionAlive();

                        Platform.runLater(() -> {

                        });
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void doStop() {
                this.doStop = true;
            }

            @Override
            public boolean keepRunning() {
                return !this.doStop;
            }
        };
    }

    /**
     * Stops the thread  that logs out users from database, which was created from main method.
     */
    @Override
    public void stop() {
        // Executed when the application shuts down. User is logged out and database connection is closed.
        if(user_id>0 && match_id>0){
            game.actualSurrender();
        }
        closeAndLogout();

    }

    /**
     * Closes all connections and logs out the users from the database on program exit.
     */
    public static void closeAndLogout() {
        if (user_id > 0) {
            db.logout(user_id);
            System.out.println("Logged out");
        }
        //Stops threads.
        if (waitTurnThread != null) {
            waitTurnRunnable.doStop();
            waitTurnThread.interrupt();
        }
        if (waitPlacementThread != null) {
            waitPlacementRunnable.doStop();
            waitPlacementThread.interrupt();
        }
        if (searchGameThread != null) {
            searchGameRunnable.doStop();
            searchGameThread.interrupt();
        }
        if(databaseNoTimeoutThread != null){
            databaseNoTimeoutRunnable.doStop();
            databaseNoTimeoutThread.interrupt();
        }
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
