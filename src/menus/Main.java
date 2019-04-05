package menus;

import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
        Parent root = FXMLLoader.load(getClass().getResource("/menus/View/login.fxml"));
        rootScene = new Scene(root, 850, 650);

        window.setTitle("Paper Legion");
        window.setScene(rootScene);
        window.setMinWidth(850);
        window.setMinHeight(650);

        //Finds screen size and sizes window to it.
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
        window.setWidth(screenWidth);
        window.setHeight(screenHeight);
        window.show();
    }

    public static void main(String[] args) {
        //Shutdown hook. Closes stuff when program exits.
        Runtime.getRuntime().addShutdownHook(new Thread(Main::closeAndLogout));
        launch(args);
    }

    @Override
    public void stop() {
        // Executed when the application shuts down. User is logged out and database connection is closed.
        closeAndLogout();
    }

    public static void closeAndLogout() {
        if (user_id > 0) {
            db.logout(user_id);
            System.out.println("You were logged out");
        }
        //Stops runnables.
        if (searchGameThread != null) {
            searchGameThread.stop();
        }
        if (waitPlacementThread != null) {
            waitPlacementThread.stop();
        }
        if (waitTurnThread != null) {
            waitTurnThread.stop();
        }
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
