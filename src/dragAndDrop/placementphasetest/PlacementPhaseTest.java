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

    //lager de ulike ui-komponentene, og returnerer panes som kan styles
    Pane grid = createGrid(root);
    Pane unitPane = createUnitPane(root);

    stage.setTitle("Placement Phase Test Application");
    stage.setScene(scene);
    stage.show();
  }

  public Pane createGrid(FlowPane root) { //lager spillbrettet
    Pane grid = new Pane();

    grid.getChildren().add(gp.gp);
    root.getChildren().add(grid);

    return grid;
  }

  public Pane createUnitPane(FlowPane root) { //lager unitselectoren
    Pane unitPane = new Pane();
    HBox units = new HBox(5);

    Tile swordman = new Tile(100, 100);
    Tile bowman = new Tile(100, 100);

    units.getChildren().addAll(swordman, bowman);
    unitPane.getChildren().add(units);
    root.getChildren().add(unitPane);

    return unitPane;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
