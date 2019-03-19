package sample.Test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class Controller2 extends Controller {

    @FXML
    private Button scenechanger;

    @FXML
    void initialize() {
        scenechanger.setOnAction( event -> {
            changeSceneI("fxml1.fxml");
        });
    }
}