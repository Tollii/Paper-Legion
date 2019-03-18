package dragAndDrop;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;

public class PlacementPhaseTest extends Application {
  final int boardSize = 7;
  final Grid gp = new Grid(boardSize, boardSize);
  Piece[][] pieces = new Piece[boardSize][boardSize];

  @Override
  public void start(Stage stage) {
    FlowPane root = new FlowPane();
    Scene scene = new Scene(root, 1280, 720);

    Pane grid = new Pane();
    grid.getChildren().add(gp.gp);
    root.getChildren().add(grid);

    stage.setTitle("Placement Phase Test Application");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
