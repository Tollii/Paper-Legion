package sample.Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainControlTest extends Application {

        public static Stage window;

        //Just a test folder I used to try out some scene switching. -Joakim

        @Override
        public void start(Stage primaryStage) throws Exception {

            window = primaryStage;

            primaryStage.setTitle("Binary Warfare");
            Parent root = FXMLLoader.load(getClass().getResource("/sample/Test/fxml1.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
}

