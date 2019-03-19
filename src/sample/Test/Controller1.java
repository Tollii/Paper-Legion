package sample.Test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller1 extends Controller {

    @FXML
    private Button scenechanger;

    @FXML
    void initialize() {
        scenechanger.setOnAction( event -> {
            changeSceneI("fxml2.fxml");
        });
    }

}
