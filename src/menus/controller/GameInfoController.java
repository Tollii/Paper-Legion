package menus.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * controller class for Game Info scene.
 * Sets up the scene layout, buttons, paddings etc.
 * Uses images and texts to show information about how to play the game,
 * as well as show images whose function serves as a game tutorial.
 * @see javafx.scene.image.Image
 */
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
    private AnchorPane paneforPattern;


    @FXML
    private AnchorPane contentPane;


    /**
     * Initialize variables, and is a sort of constructor for the scene setup.
     * @see com.sun.javafx.fxml.builder.JavaFXSceneBuilder
     */
    @FXML
    private void initialize(){
        paneforPattern.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());
        contentPane.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());


        backgroundImagePane.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());
        attackbackgroundImagePane.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());
        placementbackgroundImagePane.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());


        gameInfoBackButton.setOnAction(event -> changeScene("MainMenu.fxml"));


    }

}
