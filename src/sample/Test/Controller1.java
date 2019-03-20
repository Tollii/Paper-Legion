package sample.Test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller1 extends Controller {

    @FXML
    private Button btn_change;

    @FXML
    void initialize() {
        btn_change.setOnAction( event -> {
            changeSceneI("fxml2.fxml");
        });
    }

}
