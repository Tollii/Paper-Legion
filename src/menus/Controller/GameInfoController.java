package menus.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

public class GameInfoController extends Controller {


    @FXML
    private JFXButton gameInfoBackButton;


    @FXML
    private void initialize(){

        gameInfoBackButton.setOnAction(event -> {
            changeScene("mainMenu.fxml");
        });


    }

}
