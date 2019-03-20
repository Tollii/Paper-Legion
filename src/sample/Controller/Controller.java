package sample.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sample.Main;

import java.io.IOException;

public class Controller {

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
        if (root != null) {
            Main.window.setScene(new Scene(root));
        }
    }
}
