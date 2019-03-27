package temp;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        JFXButton endgame = new JFXButton("Return to menu");

        // maxHeight="30.0" maxWidth="90.0" minHeight="30.0" minWidth="90.0" prefHeight="30.0" prefWidth="90.0" style="-fx-background-color: #e3e4e5#e3e4e5;" text="Play"

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
