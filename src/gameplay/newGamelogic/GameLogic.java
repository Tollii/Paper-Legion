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

import Runnables.RunnableInterface;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.SQLException;
import static database.Variables.*;
import java.util.ArrayList;
import java.util.Collections;
import javafx.geometry.Orientation;

public class GameLogic extends Application {
  ////BOARD SIZE CONTROLS////
  private static final int boardSize = 7; //sets the number of tiles en each direction of the grid
  public static final int tileSize = 100; //sets size of each tile on the grid
  private static final int playerSideSize = 2; //Used to set width of the placement area

  ////SCENE ELEMENTS////
  private Grid grid = new Grid(boardSize, boardSize);
  private Pane root;
  private Label description = new Label(); //description label for the selected unit
  private Label turnCounter = new Label("TURN: " + turn); //describe which turn it is

  private Thread waitTurnThread;

  ////WINDOW SIZE////
  private final int windowWidth = 1920;
  private final int windowHeight = 1080;

  ////SIZE VARIABLES////
  private final int buttonWidth = 150;
  private final int buttonHeight = 75;

  ////PANE PADDINGS////
  private final int gridXPadding = 300;
  private final int gridYPadding = 100;
  private final int recruitXPadding = gridXPadding + tileSize*boardSize + 150;
  private final int recruitYPadding = 150;
  private final int placementButtonXPadding = 100;
  private final int placementButtonYPadding = 500;
  private final int sidePanelXPadding = gridXPadding + tileSize*boardSize + 150;
  private final int sidePanelYPadding = 150;
  private final int descriptionXPadding = 0;
  private final int descriptionYPadding = 0;

  ////GAME CONTROL VARIABLES////
  private boolean unitSelected = false;
  private int moveCounter = 0;                                // Counter for movement phase.
  private int attackCount = 0;                                // Counter for attack phase.
  private Unit selectedUnit;
  private ArrayList<Move> movementList = new ArrayList<>();   //Keeps track of the moves made for the current turn.
  private ArrayList<Attack> attackList = new ArrayList<>();
  private boolean movementPhase = true;                       //Controls if the player is in movement or attack phase
  private UnitGenerator unitGenerator = new UnitGenerator();
  ArrayList<PieceSetup> setupPieces;

  ////STYLING////
  private String gameTitle = "PAPER LEGION";
  private String descriptionFont = "-fx-font-family: 'Arial Black'";
  private String buttonBackgroundColor = "-fx-background-color: #000000";
  private String turnCounterFontSize = "-fx-font-size: 32px";
  private Paint selectionOutlineColor = Color.RED;
  private Paint buttonTextColor = Color.WHITE;
  private Paint movementHighlightColor = Color.GREENYELLOW;
  private Paint attackHighlightColor = Color.DARKRED;
  private Paint untargetableTileColor = Color.color(155.0/255.0, 135.0/255.0, 65.0/255.0);

  public void start(Stage window) throws Exception {
    // Sets static variables for players and opponent id.
    db.getPlayers();

    SetUp setUp = new SetUp();
    setUp.importUnitTypes();

    root = new Pane();
    Scene scene = new Scene(root, windowWidth, windowHeight);

    Pane gridPane = createGrid(); //creates the grid

    placementPhaseStart(); //starts the placement phase


    window.setTitle(gameTitle);
    window.setScene(scene);
    window.show();
  }

  private void placementPhaseStart() {
    Pane recruitPane = createRecruitPane();

    JFXButton finishedPlacingButton = new JFXButton("Finished placing units"); //creates a button for ending the placementphase
    finishedPlacingButton.setMinSize(buttonWidth, buttonHeight);
    finishedPlacingButton.setTextFill(buttonTextColor);
    finishedPlacingButton.setStyle(buttonBackgroundColor);
    finishedPlacingButton.setLayoutX(placementButtonXPadding);
    finishedPlacingButton.setLayoutY(placementButtonYPadding);

    recruitPane.getChildren().add(finishedPlacingButton);

    int playerSideTop, playerSideBottom; //sets paddings depending on player side (to the coloring of the boardtiles as well as untargetability)
    if (user_id == player1) {
      playerSideTop = playerSideSize;
      playerSideBottom = 0;
    } else {
      playerSideTop = 0;
      playerSideBottom = playerSideSize;
    }

    for (int i = playerSideTop; i < boardSize - playerSideBottom; i++) { //colors and makes tiles untargetable
      for (int j = 0; j < boardSize; j++) {
        grid.tileList[i][j].setFill(untargetableTileColor);
        grid.tileList[i][j].setUntargetable();
      }
    }

    finishedPlacingButton.setOnAction(event -> { //when button pressed, finishes the placementphase
      placementPhaseFinished(recruitPane);
    });
  }

  private void placementPhaseFinished(Pane recruitPane) {
    root.getChildren().remove(recruitPane); //removes recruitmentpane with all necessities tied to placementphase

    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        grid.tileList[i][j].setFill(Color.WHITE); //sets the grid all white again, and sets all tiles untargetable for dragboard as a safety measure
        grid.tileList[i][j].setUntargetable();
      }
    }

    ArrayList<Unit> exportUnitList = new ArrayList<>();
    ArrayList<Integer> exportPositionXList = new ArrayList<>();
    ArrayList<Integer> exportPositionYList = new ArrayList<>();

    for (int i = 0; i < grid.tileList.length; i++) {
      for (int j = 0; j < grid.tileList[i].length; j++) {

        if(grid.tileList[i][j].getUnit() != null){
          exportUnitList.add(grid.tileList[i][j].getUnit());
          exportPositionXList.add(j);
          exportPositionYList.add(i);
        }
      }
    }

    if (exportUnitList != null){
      db.exportPlacementUnits(exportUnitList, exportPositionXList, exportPositionYList);
    }

  }

  private int getPosXFromEvent(MouseEvent event) {
    return (int)Math.ceil((event.getX() - gridXPadding) / tileSize);
  }

  private int getPosYFromEvent(MouseEvent event) {
    return (int)Math.ceil((event.getY() - gridYPadding) / tileSize);
  }

  private Pane createGrid() { //adds grid and styles it
    Pane gridPane = new Pane();

    gridPane.getChildren().add(grid);

    gridPane.setLayoutX(gridXPadding);
    gridPane.setLayoutY(gridYPadding);
    root.getChildren().add(gridPane);

    return gridPane;
  }

  private Pane createRecruitPane() { //adds unit selector/recruiter and styles it
    Pane unitPane = new Pane();
    FlowPane units = new FlowPane(Orientation.HORIZONTAL, 5, 5);

    units.setMinWidth(520);

    for (int i = 0; i < SetUp.unitTypeList.size(); i++) {
      RecruitTile tile = new RecruitTile(tileSize, tileSize, unitGenerator.newRecruit(SetUp.unitTypeList.get(i)));
      units.getChildren().add(tile);
    }
    unitPane.getChildren().add(units);

    unitPane.setLayoutX(recruitXPadding);
    unitPane.setLayoutY(recruitYPadding);
    root.getChildren().add(unitPane);

    return unitPane;
  }

  private Pane createSidePanel() { //creates the side panel for movement/attack phase
    Pane sidePanel = new Pane();

    turnCounter.setStyle(turnCounterFontSize);

    description.setStyle(descriptionFont);
    description.setLayoutX(descriptionXPadding);
    description.setLayoutY(descriptionYPadding);
    description.setVisible(false);

    sidePanel.getChildren().addAll(description, turnCounter);

    sidePanel.setLayoutX(sidePanelXPadding);
    sidePanel.setLayoutY(sidePanelYPadding);
    root.getChildren().add(sidePanel);

    return sidePanel;
  }
}
