package sample.Test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    Stage window = new Stage();

    public void changeSceneI(String fxmlDir) {
        Parent root = null;
        try {
            root = FXMLLoader.load(sceneChanger.class.getResource(fxmlDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        window.setScene(new Scene(root));
    }
}
