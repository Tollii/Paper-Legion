package sample.Test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller1 {

    @FXML
    private Button scenechanger;

    @FXML
    void initialize() {
        scenechanger.setOnAction( event -> {
            sceneChanger.changeScene("");
        });

    }
}
