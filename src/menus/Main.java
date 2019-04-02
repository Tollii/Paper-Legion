package menus;

import database.Database;
import database.Variables;
import gameplay.GameLogic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import menus.Controller.mainMenuController;

import java.sql.SQLException;

import static database.Variables.db;
import static database.Variables.user_id;

public class Main extends Application {

    public static Stage window;
    public static Scene rootScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        //database is a static class that starts when the application starts. All queries from the the database goes through it.
        db = new Database();
        Parent root = FXMLLoader.load(getClass().getResource("/menus/View/login.fxml"));
        rootScene = new Scene(root, 850,650);

        primaryStage.setTitle("Paper Legion");
        primaryStage.setScene(rootScene);
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(650);
        primaryStage.show();

    }

    public static void main(String[] args) {
        //Shutdown hook. Closes stuff when program exits.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (user_id > 0) {
                db.logout(user_id);
            }
            if (Variables.searchGameThread.isAlive()) {
                Variables.searchGameThread.stop();
            }
            try {
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
        launch(args);
    }

    @Override
    public void stop() {
        // Executed when the application shuts down. User is logged out and database connection is closed.
        if (user_id > 0) {
            db.logout(user_id);
        }
        if (Variables.searchGameThread.isAlive()) {
            Variables.searchGameThread.stop();
        }
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
