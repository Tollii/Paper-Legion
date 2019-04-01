//  ██████╗ ██╗███╗   ██╗ █████╗ ██████╗ ██╗   ██╗    ██╗    ██╗ █████╗ ██████╗ ███████╗ █████╗ ██████╗ ███████╗  //
//  ██╔══██╗██║████╗  ██║██╔══██╗██╔══██╗╚██╗ ██╔╝    ██║    ██║██╔══██╗██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝  //
//  ██████╔╝██║██╔██╗ ██║███████║██████╔╝ ╚████╔╝     ██║ █╗ ██║███████║██████╔╝█████╗  ███████║██████╔╝█████╗    //
//  ██╔══██╗██║██║╚██╗██║██╔══██║██╔══██╗  ╚██╔╝      ██║███╗██║██╔══██║██╔══██╗██╔══╝  ██╔══██║██╔══██╗██╔══╝    //
//  ██████╔╝██║██║ ╚████║██║  ██║██║  ██║   ██║       ╚███╔███╔╝██║  ██║██║  ██║██║     ██║  ██║██║  ██║███████╗  //
//  ╚═════╝ ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝        ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝  //
//                                                                                                                //

//   ######## ########    ###    ##     ##       ##   ########     //
//      ##    ##         ## ##   ###   ###     ####   ##    ##     //
//      ##    ##        ##   ##  #### ####       ##       ##       //
//      ##    ######   ##     ## ## ### ##       ##      ##        //
//      ##    ##       ######### ##     ##       ##     ##         //
//      ##    ##       ##     ## ##     ##       ##     ##         //
//      ##    ######## ##     ## ##     ##     ######   ##         //


package gameplay;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.input.MouseButton;
import javafx.geometry.Orientation;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class GameLogic extends Application {
  static ArrayList<String> unitTypeList = new ArrayList<String>();
  private static final int boardSize = 7; //sets the number of tiles en each direction of the grid
  public static final int tileSize = 100; //sets size of each tile on the grid

  private Grid grid = new Grid(boardSize, boardSize);
  private Pane root;

  ////WINDOW SIZE////
  private final int windowWidth = 1920;
  private final int windowHeight = 1080;

  ////PANE PADDINGS////
  private final int gridXPadding = 300;
  private final int gridYPadding = 100;
  private final int recruitXPadding = 150;
  private final int recruitYPadding = 150;
  private final int placementButtonXPadding = 100;
  private final int placementButtonYPadding = 500;

  private boolean unitSelected = false;
  private final boolean player = true;

  ////COLORS////
  private Paint movementHighlightColor = Color.GREENYELLOW;
  private Paint attackHighlightColor = Color.DARKRED;
  private Paint untargetableTileColor = Color.color(155.0/255.0, 135.0/255.0, 65.0/255.0);

  public void start(Stage window) throws Exception {
    root = new Pane();
    Scene scene = new Scene(root, windowWidth, windowHeight);

    Pane gridPane = createGrid();

    placementPhaseStart();

    //TODO: might need the other phases as methods as well

    //TODO: think we might use phaseStart() and phaseFinish() methods, and to all the event handling
    //inside start(), and then call the the start methods when a phase starts, and call the finish
    //method when the phase finishes

    window.setTitle("");
    window.setScene(scene);
    window.show();
  }

  public void placementPhaseStart() {
    Pane recruitPane = createRecruitPane();

    JFXButton finishedPlacing = new JFXButton("Finished placing units"); //creates a button for ending the placementphase
    finishedPlacing.setMinSize(200.0, 70.0);
    finishedPlacing.setLayoutX(placementButtonXPadding);
    finishedPlacing.setLayoutY(placementButtonYPadding);

    recruitPane.getChildren().add(finishedPlacing);

    int playerSideTop, playerSideBottom; //sets paddings depending on player side
    if (player == true) {
      playerSideTop = 2;
      playerSideBottom = 0;
    } else {
      playerSideTop = 0;
      playerSideBottom = 2;
    }

    for (int i = 0 + playerSideTop; i < boardSize - playerSideBottom; i++) { //colors and makes tiles untargetable
      for (int j = 0; j < boardSize; j++) {
        grid.tileList[i][j].setFill(untargetableTileColor);
        grid.tileList[i][j].setUntargetable();
      }
    }

    finishedPlacing.setOnAction(event -> { //when button pressed, finishes the placementphase
      placementPhaseFinished(recruitPane);
    });
  }

  public void placementPhaseFinished(Pane recruitPane) {
    root.getChildren().remove(recruitPane); //removes recruitmentpane with all necessities tied to placementphase

    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        grid.tileList[i][j].setFill(Color.WHITE); //sets the grid all white again, and sets all tiles untargetable for dragboard as a safety measure
        grid.tileList[i][j].setUntargetable();
      }
    }
  }

  //TODO: need to finish the selection method, it takes three arguments and should let us mark a unit
  //and either get the tiles it can move to, og the tiles with units that it can attack.
  //Description box is currently missing, so add method to add it, similarly to the other
  //create pane methods, use old styler for it, just change it into using the new standard for creating panes
  //focus on movementphase, as i've got some ideas for the attack phase
  private void select(int posX, int posY, boolean movementPhase) {
    int row = posX - 1;
    int column = posY - 1;
    if (grid.tileList[column][row].getUnit() != null) {
      if (movementPhase) {

      } else {

      }
    }
  }

  //TODO: finish deselector, should sort of mirror the select method, while not beeing too diffrent from
  // the old deselector
  private void deselect() {

  }

  private int getPosXFromEvent(MouseEvent event) {
    return (int)Math.ceil((event.getX() - gridXPadding) / 100);
  }

  private int getPosYFromEvent(MouseEvent event) {
    return (int)Math.ceil((event.getY() - gridYPadding) / 100);
  }

  public Pane createGrid() { //adds grid and styles it
    Pane gridPane = new Pane();

    gridPane.getChildren().add(grid);

    gridPane.setLayoutX(gridXPadding);
    gridPane.setLayoutY(gridYPadding);
    root.getChildren().add(gridPane);

    return gridPane;
  }

  public Pane createRecruitPane() { //adds unit selector/recruiter and styles it
    Pane unitPane = new Pane();
    FlowPane units = new FlowPane(Orientation.HORIZONTAL, 5, 5);

    units.setMinWidth(520);

    for (int i = 0; i < unitTypeList.size(); i++) {
      RecruitTile tile = new RecruitTile(100, 100, new Recruit(UnitGenerator.newUnit(unitTypeList.get(i))));
      units.getChildren().add(tile);
    }
    unitPane.getChildren().add(units);

    unitPane.setLayoutX(1150);
    unitPane.setLayoutY(150);
    root.getChildren().add(unitPane);

    return unitPane;
  }

  public static void main(String[] args) {
    unitTypeList.add("Archer");
    unitTypeList.add("Swordsman");

    launch(args);
  }
}
