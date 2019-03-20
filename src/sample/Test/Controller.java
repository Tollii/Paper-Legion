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
//        Class currentClass = new Object() {
//        }.getClass().getEnclosingClass();

        Class currentClass = this.getClass();

        try {
            root = FXMLLoader.load(currentClass.getResource(fxmlDir));

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root == null) {
            System.out.println("root is null");
        } else {
            MainControlTest.window.setScene(new Scene(root));
        }
    }
}
