package temp;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import menus.Main;

import java.io.IOException;

import static database.Variables.*;
import static database.Variables.db;

public class winner extends Application {


    /*
    This is a temporary file used to preview the checkForGameOver pop-up.
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        String win_loseText;
        String gameSummary = "";
        int loser = -1;
        turn = 5;
        user_id = 2;
        opponent_id = 3;

        if (true) {
            gameSummary = "The game ended after a player's unit were all eliminated after " + turn + " turns\n";
            loser = 3;
        } else if (db.checkForSurrender() != -1) {
            gameSummary = "The game ended after a player surrendered the match after " + turn + " turns\n";
            loser = 3;
        }

        if (loser != -1) {
            //Game is won or lost.
            //Open alert window.
            Stage winner_alert = new Stage();
            winner_alert.initModality(Modality.APPLICATION_MODAL);
            winner_alert.setTitle("Game over!");

            Text winnerTextHeader = new Text();
            Text winnerText = new Text();
            winnerTextHeader.setStyle("-fx-font-size:32px;");
            winnerTextHeader.setBoundsType(TextBoundsType.VISUAL);
            //db.incrementGamesPlayed();

            if (loser == user_id) {
                win_loseText = "You Lose!\n";
            } else {
                win_loseText = "You Win!\n";
            }

            winnerTextHeader.setText(win_loseText);
            winnerText.setText(gameSummary);

            JFXButton endGameBtn = new JFXButton("Return to menu");

            endGameBtn.setOnAction(event -> {
                String fxmlDir = "/menus/View/mainMenu.fxml";
                Parent root = null;
                try {
                    root = FXMLLoader.load(this.getClass().getResource(fxmlDir));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("load failed");
                }
                winner_alert.setScene(new Scene(root));
            });

            VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            content.setSpacing(20);
            content.getChildren().addAll(winnerTextHeader,winnerText,endGameBtn);
            Scene scene = new Scene(content,450,200);
            winner_alert.initStyle(StageStyle.UNDECORATED);
            winner_alert.setScene(scene);
            winner_alert.showAndWait();
        }
    }
}
