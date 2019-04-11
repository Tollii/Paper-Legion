package menus.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

import static database.Variables.*;

/**
 * controller class for Sign Up scene. Uses methods changeScene() and db.signUp().
 * Sets up variables, and layout for the scene.
 * @see database.Database
 * @see Controller
 */
public class SignUpController extends Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField usernameInput;

    @FXML
    private JFXPasswordField passwordInput;

    @FXML
    private JFXPasswordField confirmPasswordInput;

    @FXML
    private JFXTextField emailInput;

    @FXML
    private JFXButton goBackButton;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private Label alertField;

    @FXML
    private ImageView paperLegionLogo;

    @FXML
    private AnchorPane paneforPattern;


    @FXML
    private AnchorPane contentPane;


    /**
     * Initialize variables, and is a sort of constructor for the scene setup.
     * @see com.sun.javafx.fxml.builder.JavaFXSceneBuilder
     */
    @FXML
    void initialize() {

        paneforPattern.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());
        contentPane.getStylesheets().add(getClass().getResource("/menus/controller/MenuCSS.css").toExternalForm());


        goBackButton.setOnAction(event -> changeScene("Login.fxml"));

        signUpButton.setOnAction(event -> {
            //Checks if both password fields are the same.

            boolean usernameAtLeast3chars = usernameInput.getText().length() >= 3;
            boolean passwordsAtLeast3chars = usernameInput.getText().length() >= 3;
            boolean passwordsMatch = passwordInput.getText().equals(confirmPasswordInput.getText());

            if (usernameAtLeast3chars && passwordsAtLeast3chars && passwordsMatch) {
                // Tries to register user in database.
                int signup = db.signUp(usernameInput.getText(), passwordInput.getText(), emailInput.getText());
                if (signup > 0) {
                    changeScene("Login.fxml");
                }
                else if(signup == 0){
                    alertField.setText("User already exists");
                }
            }
            String errorMsg = "";
            if (!usernameAtLeast3chars) { errorMsg += "Username has to have at least 3 characters!\n";}
            if (!passwordsAtLeast3chars) { errorMsg += "Password has to have at least 3 characters!\n";}
            if (!passwordsMatch) { errorMsg += "Passwords do not match!";}
            alertField.setText(errorMsg);
        });
    }

}
