package sample.Test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class Controller2 extends Controller {

    @FXML
    private Button btn_change;

    @FXML
    void initialize() {
        btn_change.setOnAction( event -> {
            changeSceneI("fxml1.fxml");
        });
    }
}