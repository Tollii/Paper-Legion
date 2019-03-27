package Menus.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import Menus.Main;

import java.io.IOException;

public class Controller {

    public void changeScene(String fxml) {
        Class currentClass = this.getClass();

        String fxmlDir = "/Menus/View/" +fxml;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(currentClass.getResource(fxmlDir));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Main.window.setScene(new Scene(root));
    }
}
