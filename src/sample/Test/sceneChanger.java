package sample.Test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class sceneChanger {

    public static void changeScene(String fxml)  {
        Parent root = null;
        try {
            root = FXMLLoader.load(sceneChanger.class.getResource(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //MainControlTest.window.setScene(new Scene(root));
    }
}
