package menus.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class GameInfoController extends Controller {



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton gameInfoBackButton;


    @FXML
    private ImageView paperLegionLogo;



    @FXML
    private void initialize(){

        gameInfoBackButton.setOnAction(event -> {
            changeScene("mainMenu.fxml");
        });


    }

}
