package hashAndSalt.testing;

import hashAndSalt.Login;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class Main extends Application {

    private Scene scene1,scene2;

    @Override
    public void start(Stage primaryStage) {

        Stage window = primaryStage;

        VBox layout = new VBox();
        Label error = new Label("");
        TextField username = new TextField("admin");
        TextField password = new TextField("admin");
        Button enter = new Button("Enter");
        layout.getChildren().addAll(error,username,password,enter);
        layout.setPadding(new Insets(20, 20, 20, 20));

        enter.setOnAction(event -> {
            Login log = new Login();
            if (log.login(username.toString(),password.toString())) {
                window.setScene(scene2);
            } else {
                error.setText("Error logging in");
            }
        });

        VBox layout2 = new VBox();
        Label text = new Label("You are logged in");
        Button logout = new Button("Log out");
        layout2.getChildren().addAll(text,logout);

        logout.setOnAction(event -> window.setScene(scene1));

        layout.setAlignment(Pos.CENTER);
        layout2.setAlignment(Pos.CENTER);

        scene1 = new Scene(layout,200,200);
        scene2 = new Scene(layout2,200,200);

        window.setScene(scene1);
        window.setTitle("Login");
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
