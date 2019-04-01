package temp;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import menus.Main;

import java.io.IOException;

public class winner extends Application {


    /*
    This is a temporary file used to preview the winner pop-up.
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        Stage winner_alert = primaryStage;

        //Game is won.
        winner_alert = new Stage();
        //winner_alert.initModality(Modality.APPLICATION_MODAL);
        winner_alert.setTitle("Game over!");

        Text winner = new Text();
        winner.setStyle("-fx-font-size:32px;");
        //Button endgame = new Button("Return to menu");

        //winner_alert.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 2, 1.0, 0, 0);");

        JFXButton endgame = new JFXButton("Return to menu");

        endgame.setOnAction(event -> {
            String fxmlDir = "/menus/View/mainMenu.fxml";
            FXMLLoader loader = new FXMLLoader();
            Parent root2 =null;
            try {
               root2 =  FXMLLoader.load(this.getClass().getResource(fxmlDir));
               // loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("load failed");
            }

            Main.window.setScene(new Scene(root2));
        });

        winner.setText("You win!");

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(20);
        content.getChildren().addAll(winner,endgame);
        Scene scene = new Scene(content,250,150);
        winner_alert.initStyle(StageStyle.UNDECORATED);
        winner_alert.setScene(scene);
        winner_alert.show();

    }
}
