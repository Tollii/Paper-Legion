package dragAndDrop;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import java.util.ArrayList;
import javafx.geometry.Orientation;

public class PlacementPhaseTest extends Application {
  final int boardSize = 7;
  final Grid gp = new Grid(boardSize, boardSize);
  Unit[][] pieces = new Unit[boardSize][boardSize];

  @Override
  public void start(Stage stage) {
    Pane root = new Pane();
    Scene scene = new Scene(root, 1920, 1080);

    //lager de ulike ui-komponentene, og returnerer panes som kan styles
    Pane grid = createGrid(root);
    Pane unitPane = createUnitPane(root);

    stage.setTitle("Placement Phase Test Application");
    stage.setScene(scene);
    stage.show();
  }

  public Pane createGrid(Pane root) { //lager spillbrettet
    Pane grid = new Pane();

    grid.getChildren().add(gp.gp);

    grid.setLayoutX(300);
    grid.setLayoutY(100);
    root.getChildren().add(grid);

    return grid;
  }

  public Pane createUnitPane(Pane root) { //lager unitselectoren
    Pane unitPane = new Pane();
    FlowPane units = new FlowPane(Orientation.HORIZONTAL, 5, 5);

    units.setMinWidth(520);

    for (int i = 0; i < 7; i++) {
      Tile tile = new Tile(100, 100);
      units.getChildren().add(tile);
    }
    unitPane.getChildren().add(units);

    unitPane.setLayoutX(1150);
    unitPane.setLayoutY(150);
    root.getChildren().add(unitPane);

    return unitPane;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
