package sample.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class controllerHelper {

    public static void changeScene(String fxmlDir){
        FXMLLoader loader = new FXMLLoader();

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Class currentClass = new Object() { }.getClass().getEnclosingClass();

        loader.setLocation(currentClass.getResource(fxmlDir));
        try {
            loader.load();
        } catch(IOException e){
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

//    public static void changeScene(JFXButton btn, String fxmlDir) {
//        Parent root = null;
//        try {
//            root = FXMLLoader.load(btn.class.getResource(fxmlDir));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Main.window.setScene(new Scene(root));
//    }

//    public static void changeScene(String fxmlDir) {
//        FXMLLoader loader = new FXMLLoader();
//        Class currentClass = new Object() {}.getClass().getEnclosingClass();
//        loader.setLocation(currentClass.getResource(fxmlDir));
//        try {
//            loader.load();
//        } catch (IOException e ) {
//            e.printStackTrace();
//        }
//        Parent root = loader.getRoot();
//        Main.window.setScene(new Scene(root));
//    }

}
