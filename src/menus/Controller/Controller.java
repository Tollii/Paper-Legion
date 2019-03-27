package menus.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import menus.Main;

import java.io.IOException;

public class Controller {

    public void changeScene(String fxml) {
        Class currentClass = this.getClass();

        double stageWidth = Main.window.getScene().getWidth();
        double stageHeight = Main.window.getScene().getHeight();
        String fxmlDir = "/menus/View/"+fxml;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(currentClass.getResource(fxmlDir));


        Parent root2 = null;
        try {
            root2 = FXMLLoader.load(getClass().getResource("/menus/View/"+fxml));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.rootScene.setRoot(root2);


    }
}
