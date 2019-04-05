package menus.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import menus.Main;

import java.io.IOException;

public class Controller {

    public void changeScene(String fxml) {
        Class currentClass = this.getClass();

        String fxmlDir = "/menus/View/"+fxml;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(currentClass.getResource(fxmlDir));

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlDir));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.rootScene.setRoot(root);

    }

}
