package menus.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GameInfoController extends Controller {


    @FXML
    private JFXButton gameInfoBackButton;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;



    @FXML
    private Label gameTitleLabel;



    @FXML
    private void initialize(){

        gameInfoBackButton.setOnAction(event -> {
            changeScene("mainMenu.fxml");
        });


    }

}
