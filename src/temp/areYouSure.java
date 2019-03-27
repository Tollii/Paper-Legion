package temp;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import menus.Main;

import java.io.IOException;

public class areYouSure extends Application {

    /*
    This is a temporary file used to preview the confirmation pop-up.
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        Stage confirm_alert = primaryStage;

        confirm_alert = new Stage();
        confirm_alert.initModality(Modality.APPLICATION_MODAL);
        confirm_alert.setTitle("Game over!");

        Text surrender_text = new Text();
        surrender_text.setText("Are you sure?");
        surrender_text.setStyle("-fx-font-size:32px;");

        JFXButton surrender_yes = new JFXButton("Yes");
        JFXButton surrender_no = new JFXButton("No");

        surrender_yes.setOnAction(event -> {
            //TODO
        });

        surrender_no.setOnAction(event -> {
            //TODO
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(surrender_yes,surrender_no);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(20);

        content.getChildren().addAll(surrender_text,buttons);
        Scene scene = new Scene(content, 250, 150);
        confirm_alert.initStyle(StageStyle.UNDECORATED);
        confirm_alert.setScene(scene);
        confirm_alert.showAndWait();
    }
}
