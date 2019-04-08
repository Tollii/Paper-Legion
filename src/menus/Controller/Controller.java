package menus.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import menus.Main;

import java.io.IOException;

/**
 * Controller to hold a shared method changeScene.
 * It's function is to avoid duplicate code, and have a method
 * that can be called from other classes.
 */
public class Controller {

    /**
     * Changes the scene for the main Stage of the application.
     * @param fxml Takes a String with the filename of the fxml file to the new scene.
     * @see javafx.stage.Stage
     * @see javafx.fxml.FXML
     * @see FXMLLoader
     */
    void changeScene(String fxml) {
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
