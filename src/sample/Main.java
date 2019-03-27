package sample;

import Database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

import static Database.Variables.db;
import static Database.Variables.user_id;

public class Main extends Application {

    public static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        //Database is a static class that starts when the application starts. All queries from the the database goes through it.
        db = new Database();
        Parent root = FXMLLoader.load(getClass().getResource("/sample/View/login.fxml"));
        primaryStage.setTitle("Binary Warfare");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(650);

    }

    public static void main(String[] args) {
        //Shutdown hook. Closes stuff when program exits.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (user_id > 0) {
                db.logout(user_id);
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
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
