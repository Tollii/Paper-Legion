package menus.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static database.Variables.*;


    public class MatchSetupController {

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private JFXButton joinGameButton;

        @FXML
        private TableColumn<Match, Boolean> passwordTable;

        @FXML
        private JFXButton createGameButton;

        @FXML
        private Label gameTitleLabel;

        @FXML
        private TableColumn<Match, String> player_table;

        @FXML
        private TableColumn<Match, Integer> matchID_Table;

        @FXML
        private TableView<Match> table;

        @FXML
        void initialize() {

            //Sets up variables for connecting Match class with Table Columns
            player_table.setCellValueFactory(new PropertyValueFactory<>("player"));
            matchID_Table.setCellValueFactory(new PropertyValueFactory<>("match_id"));
            passwordTable.setCellValueFactory(new PropertyValueFactory<>("password"));
            table.setItems(getMatches());


            joinGameButton.setOnAction(event -> {
                //table.getSelectionModel().getSelectedCells()
            });


            createGameButton.setOnAction(event -> {
                //TODO: CREATE INPUT FOR PASSWORD
                StackPane stackpane = new StackPane();
                VBox vBox = new VBox();
                Label dialog = new Label("Do you want to password protect this game?");
                JFXButton yes_button = new JFXButton("Yes");
                JFXButton no_button = new JFXButton("No");
                HBox hBox = new HBox();
                JFXButton abort = new JFXButton("Abort");


                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(yes_button,no_button, abort);
                vBox.getChildren().addAll(dialog,hBox);
                vBox.setAlignment(Pos.CENTER);
                vBox.setSpacing(20);
                stackpane.getChildren().add(vBox);
                JFXTextField inputPassword = new JFXTextField();
                inputPassword.setPromptText("Enter password");
                JFXButton submitPassword = new JFXButton("Submit");
                inputPassword.setPrefWidth(150);
                inputPassword.setMaxWidth(150);



                Stage window = new Stage();
                window.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(stackpane, 300,150);
                window.setScene(scene);
                window.show();

                yes_button.setOnAction(event1 -> {
                    dialog.setText("Input password for game:");
                    hBox.getChildren().removeAll(yes_button,no_button, abort);
                    vBox.getChildren().removeAll(hBox);
                    hBox.getChildren().addAll(submitPassword, abort);
                    vBox.getChildren().addAll(inputPassword, hBox);
                });

                submitPassword.setOnAction(event1 -> {
                    String passwordInserted = inputPassword.getText().trim().toString();
                    System.out.println(passwordInserted);
                });

                no_button.setOnAction(event1 -> {
                    //db.createGame(20);
                    window.close();
                });


                abort.setOnAction(event1 -> {
                    window.close();
                });
                table.setItems(getMatches()); //Refreshes tables.

            });

        }

    public ObservableList<Match> getMatches(){

        ArrayList<Match> matches =  db.findGamesAvailable();
        ObservableList<Match> match_ids = FXCollections.observableArrayList();

        for (Match i : matches) {
            match_ids.add(i);
        }




//        match_ids.add(new Match(1,"Eric", true));
//        match_ids.add(new Match(2,"Thomas", false));

        return match_ids;
    }




}
