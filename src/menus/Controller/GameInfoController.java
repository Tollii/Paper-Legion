package menus.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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
    private ImageView movementImage;

    @FXML
    private HBox movementHbox;


    @FXML
    private Pane movementPane;

    @FXML
    private JFXTextArea movementText;


    @FXML
    private Pane backgroundImagePane;


    @FXML
    private JFXTextArea attackText;

    @FXML
    private JFXTextArea placementText;

    @FXML
    private Pane attackbackgroundImagePane;

    @FXML
    private Pane placementbackgroundImagePane;


    @FXML
    private void initialize(){

        gameInfoBackButton.setOnAction(event -> {
            changeScene("mainMenu.fxml");
        });

        backgroundImagePane.setStyle("-fx-background-image: url(/menus/menuAssets/movementInfo.png);" +
                "-fx-background-size: cover;" +
                "-fx-background-position: top left;");

        attackbackgroundImagePane.setStyle("-fx-background-image: url(/menus/menuAssets/attackInfo.png);" +
                "-fx-background-size: cover;" +
                "-fx-background-position: top left;");

        placementbackgroundImagePane.setStyle("-fx-background-image: url(/menus/menuAssets/placement.png);" +
                "-fx-background-size: cover;" +
                "-fx-background-position: top left;");





        //IMAGES USE 13:9 RATIO

    }

}
