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


public class Main extends Application {

    public static Stage window;
    public static Scene rootScene;

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

    public static void main(String[] args) {
//        //Sets the Dock icon for mac
//        try {
//            URL iconURL = Main.class.getResource("/images/dockIcon.png");
//            Image image = new ImageIcon(iconURL).getImage();
//            com.apple.eawt.Application.getApplication().setDockIconImage(image);
//        } catch (Exception e) {
//            // Won't work on Windows or Linux.
//        }

        //Shutdown hook. Closes stuff when program exits.
        Runtime.getRuntime().addShutdownHook(new Thread(Main::closeAndLogout));
        launch(args);
    }

    private void databaseNoTimeout() {
        // Runnable lambda implementation for turn waiting with it's own thread

        databaseNoTimeoutRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while (keepRunning()) {
                    try {
                        System.out.println("HALLOOOO from the thread");
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
            public synchronized void doStop() {
                this.doStop = true;
            }

            @Override
            public synchronized boolean keepRunning() {
                return !this.doStop;
            }
        };
    }

    @Override
    public void stop() {
        // Executed when the application shuts down. User is logged out and database connection is closed.
        if(user_id>0 && match_id>0){
            game.actualSurrender();
        }
        closeAndLogout();

    }

    public static void closeAndLogout() {
        if (user_id > 0) {
            db.logout(user_id);
            System.out.println("You were logged out");
        }
        //Stops threads.
        if (waitTurnThread != null) {
            waitTurnRunnable.doStop();
            waitTurnThread.stop();
        }
        if (waitPlacementThread != null) {
            waitPlacementRunnable.doStop();
            waitPlacementThread.stop();
        }
        if (searchGameThread != null) {
            searchGameRunnable.doStop();
            searchGameThread.stop();
        }
        if(databaseNoTimeoutThread != null){
            databaseNoTimeoutRunnable.doStop();
            searchGameThread.stop();
        }
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
