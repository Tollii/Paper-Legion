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

import gameplay.gameboard.*;
import gameplay.logic.Attack;
import gameplay.logic.GameLogic;
import gameplay.logic.Move;
import gameplay.units.Unit;
import gameplay.units.UnitGenerator;
import runnables.RunnableInterface;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;

import java.io.IOException;

import static database.Variables.*;

import java.util.ArrayList;

import javafx.geometry.Orientation;
import menus.Main;

/**
 * This class handles the game part of the application, it sets up the the game
 * with variables, panes, buttons, layout and communication methods/threads for
 * communicating with other players. All of the visual stuff is done here, and also
 * some methods for handling the the visuals.
 * When generated a object of this class, the object should call: "instancename".start(Stage).
 * When calling this method, this Main method takes control of the Stage.
 * @see database.Database
 * @see GameLogic
 */
public class GameMain extends Application {

    ////BOARD SIZE CONTROLS////
    private static final int playerSideSize = 2; //Used to set width of the placement area

    ////SCENE ELEMENTS////
    private static Pane root;
    private static final Label description = new Label();                //description label for the selected unit
    private static final Label descriptionHead = new Label();            //description head label for selected unit
    private static final Label placeUnitLabel = new Label("Drag units onto the grid"); //User hint to place units on the board
    private static final Label resourceLabel = new Label();    //Static so that Tile can update the label
    private final Label  turnCounter = new Label("TURN: " + turn); //describe which turn it is
    private final Label phaseLabel = new Label();
    private JFXButton endTurnButton = new JFXButton();                                //button for ending turn
    private static FlowPane recruitUnits;
    public Pane phaseLabelPane;
    private static Pane placeUnitPane;
    private static Path arrow;

    ////WINDOW SIZE////
    private final double windowWidth = screenWidth;
    private final double windowHeight = screenHeight;

    ////GAME OVER WINDWOW SIZE////
    private final int gameOverHeight = 200;
    private final int gameOverWidth = 450;

    ////SURRENDER WINDOW SIZE////
    private final int surrenderHeight = 180;
    private final int surrenderWidth = 250;

    ////SIZE VARIABLES////

    //GAME OVER WINDOW//
    private final int gameOverContentSpacing = 20;

    //SURRENDER WINDOW//
    private final int surrenderButtonSpacing = 50;
    private final int surrenderContentSpacing = 20;

    //BUTTONS SIZE//
    private final int buttonWidth = 175;
    private final int buttonHeight = 75;

    //LABELS SIZES//
    private final int phaseLabelHeight = 50;
    private final int descriptionHeadHeigth = 20;
    private final int placeUnitLabelWidth = 500;
    private final int placeUnitLabelHeight = 40;

    //RECRUIT PANE UNIT TILES WIDTH//
    private static final int unitPadding = 5;
    private static final int unitTilesWidth = tileSize * 5 + unitPadding * 4;

    ////PANE PADDINGS////
    private final int buttonYPadding = (int)(windowHeight * 0.65);
    private final int buttonSpacing = 10;

    //GRID//
    private final int gridXPadding = (int)(windowWidth * 0.08);
    private final int gridYPadding = (int)(windowHeight * 0.07);

    //PLACEMENT PHASE SIDE PANEL//
    private final int recruitXPadding = gridXPadding + tileSize * boardSize + (int)(screenWidth * 0.05);
    private final int recruitYPadding = (int)(windowHeight * 0.12);
    private final int unitTilesYPadding = 60;
    private final int placementDescriptionYPadding = 200;
    private final int placementButtonXPadding = 225;
    private final int recruitContentXPadding = 75;

    //ARROW//
    private final int arrowWidth = 5;
    private final int arrowHeadSize = 30;

    //MOVEMENT AND ATTACK PHASE SIDE PANEL//
    private final int sidePanelXPadding = gridXPadding + tileSize * boardSize + (int)(screenWidth * 0.05);
    private final int sidePanelYPadding = (int)(windowHeight * 0.12);
    private final int turnCounterXPadding = 5;
    private final int turnCounterYPadding = 0;
    private final int sidePanelContentXPadding = 0;
    private final int descriptionYPadding = 100;
    private final int descriptionXPadding = 0;


    //PHASELABEL PANE//
    private final int phaseLabelYPadding = ((gridYPadding - phaseLabelHeight) / 2);

    ////GAME CONTROL VARIABLES////
    private boolean unitSelected = false;
    private ArrayList<Move> movementList = new ArrayList<>();           //Keeps track of the moves made for the current turn.
    private ArrayList<Attack> attackList = new ArrayList<>();           //Keeps track of the attacks made for the current turn.
    private boolean movementPhase = true;                               //Controls if the player is in movement or attack phase
    private boolean isPlacementPhase;
    private boolean surrendered = false;
    private boolean gameFinished = false;
    public static boolean hasPlacedAUnit = false;

    ////THREAD TIMER////
    private final int threadTimer = 1000;

    ////UNIT GENERATOR////
    private UnitGenerator unitGenerator = new UnitGenerator();

    private final String arialFont = "-fx-font-family: 'Arial Black';";
    private final String buttonBackgroundColor = "-fx-background-color: #000000;";
    private final String fontSize32 = "-fx-font-size:32px;";
    private final String descriptionHeadFontSize = "-fx-font-size: 20px;";
    private final Paint buttonTextColor = Color.WHITE;
    private final Paint movementHighlightColor = Color.GREENYELLOW;
    private final Paint attackHighlightColor = Color.DARKRED;
    private final Paint untargetableTileColor = Color.rgb(155, 135, 65);
    private final String placeUnitLabelBackgroundColor = "-fx-background-color: rgba(255, 255, 255, 0.4);";
    private final Paint arrowColor = Color.rgb(170, 170, 192);

    private GameLogic game;

    /**
     * Start is called upon whenever the application changes scenes.
     * Sets up variables, layouts, panes, buttons, etc. This method calls upon
     * the addObstacles() method and the placementPhaseStart() method.
     * the game in placementPhase
     * @see GameLogic
     * @see Application
     */
    public void start(Stage window) {
        // Sets static variables for players and opponent id.
        game = new GameLogic();

        db.getPlayers();

        root = new Pane();
        root.setId("root");
        root.getStylesheets().add(getClass().getResource("/gameplay/assets/css/GameMainCSS.css").toExternalForm());

        Scene scene = new Scene(root, windowWidth, windowHeight);

        Pane gridPane = createGrid(); //creates the grid
        gridPane.setId("grid");
        gridPane.getStylesheets().add(getClass().getResource("/gameplay/assets/css/GameMainCSS.css").toExternalForm());

        addObstacles();

        placementPhaseStart(); //starts the placement phase

        ////STYLING////

        String gameTitle = "PAPER LEGION";
        window.setTitle(gameTitle);
        window.setScene(scene);
        window.show();

    }

    /**
     * Player 2 adds obstacles when he joins the game, this method calls upon the
     * method  (GameLogic) game.createObstacles() which creates obstacles and give them a random placement
     * by using a Random number generator.Player 1 continuously wait and polls to check if obstacles have been added
     * before resuming the game flow of the program.
     * @see GameLogic
     * @see database.Database
     */
    private void addObstacles() {
        // Also this code can put obstacles in the same spot at the moment.
        if (!yourTurn) {
            obstacles = game.createObstacles();
        } else {
            //Player 1 continually checks if all the obstacles have been added to the match. Then he imports from the database.
            while (!db.obstaclesHaveBeenAdded()) {
                try {
                    Thread.sleep(threadTimer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            obstacles = db.importObstacles();
        }

        //Adds obstacles to the board.
        for (int i = 0; i < obstacles.size(); i++) {
            grid.tileList[obstacles.get(i).getPosY()][obstacles.get(i).getPosX()].setObstacle(obstacles.get(i));
        }
    }

    /**
     * Starts the placement phase by setting variables currentResources = startingResources, calling upon
     * the method createRecruitPane() which enables Dragboard to work with recruit, recruit tiles,
     * and sets up a sidebar for display information like finishedPlacingButton, description, and sets up
     * the grid to be divided against the players, so that player 1 can place their units/recruits on the top
     * and player can place their units on the bottom. Clicking the finishedPlacingButton calls upon the method
     * placementPhaseFinished().
     * @see GameLogic
     * @see Recruit
     * @see Unit
     * @see database.Variables
     */
    private void placementPhaseStart() {

        currentResources = startingResources; //Starts current resources to starting resources;
        isPlacementPhase = true;

        Pane recruitPane = createRecruitPane();
        recruitPane.setId("recruitPane");
        recruitPane.getStylesheets().add(getClass().getResource("/gameplay/assets/css/GameMainCSS.css").toExternalForm());
        showPlaceUnitLabel();

        JFXButton finishedPlacingButton = new JFXButton("Finished placing units"); //creates a button for ending the placementphase
        finishedPlacingButton.setMinSize(buttonWidth, buttonHeight);
        finishedPlacingButton.setTextFill(buttonTextColor);
        finishedPlacingButton.setStyle(buttonBackgroundColor);
        finishedPlacingButton.setLayoutX(placementButtonXPadding);
        finishedPlacingButton.setLayoutY(buttonYPadding);
        description.setTranslateX(descriptionXPadding);
        descriptionHead.setTranslateX(descriptionXPadding);
        resourceLabel.setTranslateX(recruitContentXPadding);
        recruitUnits.setTranslateX(recruitContentXPadding);

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

        grid.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
          if (event.getButton() == MouseButton.SECONDARY && isPlacementPhase) {
            int posX = getPosXFromEvent(event);
            int posY = getPosYFromEvent(event);
            if (grid.tileList[posY][posX].getUnit() != null) {
              currentResources += grid.tileList[posY][posX].getUnit().getCost();
              updateResourceLabel();
              grid.tileList[posY][posX].removeUnit();
              grid.tileList[posY][posX].setTargetable();
            }
          }
        });

        finishedPlacingButton.setOnAction(event -> { //when button pressed, finishes the placementphase
            if (hasPlacedAUnit) {
                placementPhaseFinished(recruitPane);
            }
        });
    }

    /**
     * It is a static method that updates the resource label, when called upon.
     * It sets the text of the resourceLabel to a static variable :"currentResources".
     * @see database.Variables
     */
    public static void updateResourceLabel() {
        resourceLabel.setText("Resources: $" + currentResources);
    }

    /**
     * Deselects the other recruit tiles in the sidebar when selecting
     * another recruit tile. This method unstrokes the recruitTiles which is
     * not selected.
     * @see Recruit
     * @see RecruitTile
     */
    public static void deselectRecruitTiles() {
        RecruitTile[] a = new RecruitTile[recruitUnits.getChildren().size()];
        for (RecruitTile tile:recruitUnits.getChildren().toArray(a)) {
            tile.setStrokeWidth(standardStrokeWidth);
            tile.setStroke(standardStrokeColor);
        }
    }

    /**
     * When placement phase is finished, this method is called upon to remove the panes and
     * calls upon the method exportPlaceMentUnits() to export the position of the units placed in
     * the placement phase, and setReady() to indicate to the other player that opponent has finished placement
     * phase and is ready to start the game in Movement phase.
     * Finally this method calls upon the method waitForOpponentReady() which polls
     * to check if opponent is read.
     * @param recruitPane Takes in a Pane
     * @see database.Database
     * @see Recruit
     * @see RecruitTile
     */
    private void placementPhaseFinished(Pane recruitPane) {
        root.getChildren().remove(recruitPane); //removes recruitmentpane with all necessities tied to placementphase
        isPlacementPhase = false;
        phaseLabelPane = createPhaseLabelPane();
        phaseLabelPane.setId("phaseLabelPane");
        phaseLabelPane.getStylesheets().add(getClass().getResource("/gameplay/assets/css/GameMainCSS.css").toExternalForm());
        phaseLabel.setText("WAITING FOR OTHER PLAYER");

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid.tileList[i][j].setFill(standardTileColor); //sets the grid all white again, and sets all tiles untargetable for dragboard as a safety measure
                grid.tileList[i][j].setUntargetable();
            }
        }

        ArrayList<Unit> exportUnitList = new ArrayList<>();
        ArrayList<Integer> exportPositionXList = new ArrayList<>();
        ArrayList<Integer> exportPositionYList = new ArrayList<>();

        for (int i = 0; i < grid.tileList.length; i++) {
            for (int j = 0; j < grid.tileList[i].length; j++) {

                if (grid.tileList[i][j].getUnit() != null) {
                    exportUnitList.add(grid.tileList[i][j].getUnit());
                    exportPositionXList.add(j);
                    exportPositionYList.add(i);
                }
            }
        }

        db.exportPlacementUnits(exportUnitList, exportPositionXList, exportPositionYList);
        db.setReady(true);
        waitForOpponentReady();
    }

    /**
     * Method for polling if opponent is finished with placement phase. It uses the
     * checkIfOpponentReady() from Database method to do this.
     * This methods calls upon the methods importPlacementUnits() and movementPhaseStart()
     * when opponent is finished with the placement phase.
     * @see GameLogic
     * @see database.Database
     */
    private void waitForOpponentReady() {
        // Runnable lambda implementation for turn waiting with it's own thread

        waitPlacementRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while (keepRunning()) {
                    try {
                        Thread.sleep(threadTimer);
                        while (!opponentReady) {
                            Thread.sleep(threadTimer);
                            //When player in database matches your own user_id it is your turn again.

                            if (db.checkIfOpponentReady()) {
                                opponentReady = true;

                                doStop();
                                waitPlacementThread = null;
                            }
                        }

                        //What will happen when your opponent is ready.

                        Platform.runLater(() -> {

                            importOpponentPlacementUnits();
                            movementPhaseStart();

                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public synchronized void doStop() {
                this.doStop = true;
            }

            @Override
            public synchronized boolean keepRunning() {
                return !this.doStop;
            }
        };

        waitPlacementThread = new Thread(waitPlacementRunnable);
        waitPlacementThread.start();
    }

    /**
     * Uses db.importPlacementUnits() method to import opponent's pieces onto board.
     * Uses UnitGenerator to add the new enemy units.
     * @see PiecePlacer
     * @see Grid
     * @see UnitGenerator
     * @see database.Database
     */
    private void importOpponentPlacementUnits() {
        ArrayList<PiecePlacer> importList = db.importPlacementUnits();

        for (PiecePlacer piece : importList) {
            grid.tileList[piece.getPositionY()][piece.getPositionX()].setUnit(unitGenerator.newEnemyUnit(piece.getUnit_type_id(), piece.getPieceId()));
        }
    }

    /**
     * Starts the movement phase, starts up creating panes for game with the methods
     * createSidePanel() and createPhaseLabelPane() to set up layouts for the game in
     * the movement phase. This method also set's the headline label to Movement phase.
     * And if waiting on the other player it sets the text to Opponent's turn. And calls the
     * waitForTurn() method to poll database to see if the turn has changed.
     * It contains several optional methods depending on user input. User can hit
     * the end turn button and be transported into attackphase, or it can shift the turn to the
     * opponent depending on which phase player is in. This method has all of the MouseEvent's
     * for the game, and depending on which mouse button clicked, or if double click will launch
     * different methods. The methods used in the MouseEvent is select(), deselect(), attack(), move()
     * @see GameLogic
     *
     */
    private void movementPhaseStart() {
        Pane sidePanel = createSidePanel();


        descriptionHead.setTranslateX(75);
        description.setTranslateX(75);

        sidePanel.setId("sidePanel");
        sidePanel.getStylesheets().add(getClass().getResource("/gameplay/assets/css/GameMainCSS.css").toExternalForm());

        endTurnButton.setText("End Turn");
        endTurnButton.setMinSize(buttonWidth, buttonHeight);
        endTurnButton.setTextFill(buttonTextColor);
        endTurnButton.setStyle(buttonBackgroundColor);
        endTurnButton.setTranslateX(135);
        endTurnButton.setTranslateY(-25);

        //button for surrendering
        JFXButton surrenderButton = new JFXButton("Surrender");
        surrenderButton.setTranslateX(135);
        surrenderButton.setTranslateY(-25);
        surrenderButton.setMinSize(buttonWidth, buttonHeight);
        surrenderButton.setTextFill(buttonTextColor);
        surrenderButton.setStyle(buttonBackgroundColor);

        HBox hboxSidePanelButtons = new HBox();
        hboxSidePanelButtons.setSpacing(buttonSpacing);
        hboxSidePanelButtons.getChildren().addAll(endTurnButton, surrenderButton);
        sidePanel.getChildren().addAll(hboxSidePanelButtons);
        hboxSidePanelButtons.setLayoutY(buttonYPadding);

        movementPhase = true;
        phaseLabel.setText("MOVEMENT PHASE");

        //If you are player 2. Start polling the database for next turn.
            turnCounter.setTranslateX(225);
            turnCounter.setTranslateY(25);
        if (!yourTurn) {
            phaseLabel.setText("OPPONENT'S TURN");
            endTurnButton.setText("Waiting for other player");
            waitForTurn();
        } else {
            //Enters turn 1 into database.
            db.sendTurn(turn);
        }

        ////BUTTON EvenT HANDLERS////
        endTurnButton.setOnAction(event -> {
            endTurn();
            phaseLabel.setText("OPPONENT'S TURN");
        });

        surrenderButton.setOnAction(event -> surrender());

        ////EVENT HANDLER FOR SELECTING TILES, MOVEMENT AND ATTACK///
        grid.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            //selects unit if there is none selected
            if (!unitSelected) {
                select(event);
            }

            //deselects unit if selected and right button is pressed
            if (unitSelected && event.getButton() == MouseButton.SECONDARY) {
                deselect();
            }

            //moves unit if left button is pressed once and it is the movement phase
            if (event.getClickCount() == 1) {
                if (unitSelected && movementPhase && event.getButton() == MouseButton.PRIMARY) {
                    move(event);
                }
            }

            //attacks if left button is pressed twice and the unit selected hasn't attacked
            if (event.getClickCount() == 2) {
                if (unitSelected && !movementPhase && selectedUnit.getHasAttackedThisTurn() && event.getButton() == MouseButton.PRIMARY) {
                    attack(event);
                }
            }
        });
    }

    /**
     * If it is player's turn it gets mouseclick position from MouseEvent and uses
     * methods getPosYFromEvent() and getPosYFromEvent() to translate into x and y
     * coordinates corresponding with the grid/board. It then uses the method getMovementPossibleTiles()
     * to get all the tiles the unit is able to move to, and checks if the move is legal with the method
     * checkForLegalMove(). If the move is legal then the unit changes position and adds itself to a movement list
     * to later be exported to Database. Finally, this method sets the phase label to Attack Phase, and remove all
     * of the movement highlighting of the tiles with the method clearHighlight().
     * @param event Takes in a MouseEvent.MouseClicked event.
     * @see GameLogic
     */
    private void move(MouseEvent event) {
        if(yourTurn) {
            int newPosX = getPosXFromEvent(event);
            int newPosY = getPosYFromEvent(event); //position of click

            ArrayList<Tile> movementTargets = game.getMovementPossibleTiles();

            if (game.checkForLegalMove(newPosY, newPosX, movementTargets)) {
                grid.tileList[selectedPosY][selectedPosX].removeUnit();
                grid.tileList[newPosY][newPosX].setUnit(selectedUnit);

                //De-colours previous tile
                grid.tileList[selectedPosY][selectedPosX].setStroke(standardStrokeColor);
                grid.tileList[selectedPosY][selectedPosX].setStrokeType(standardStrokePlacement);
                grid.tileList[selectedPosY][selectedPosX].setStrokeWidth(standardStrokeWidth);

                //adds the move to movementlist
                movementList.add(new Move(turn, selectedUnit.getPieceId(), match_id, selectedPosX, selectedPosY, newPosX, newPosY));

                movementPhase = false; //sets phase to attack
                phaseLabel.setText("ATTACK PHASE");

                clearHighLight();
                select(event);
            }
        }
    }

    /**
     * Checks to see if in movementphase and if it's players turn, if it is then this method uses
     * getPosXFromEvent and getPosYFromEvent to get the MouseEvent.MouseClick transformed into
     * grid coordinates and uses the method getAttackableTiles() to get all pieces on tiles within attack
     * movement and uses checkForLegalAttack to see whether the piece player wants to attack is in the array of
     * attackable Tiles. It then adds the attack to attackList which can be sent to database and then executes attack
     * with executeAttack() method. Finally it deselects the unit with deselect() method.
     * @param event Takes in a MouseEvent.MouseClick
     * @see GameMain
     * @see Grid
     * @see Unit
     * @see GameLogic
     */
    private void attack(MouseEvent event) {
        if (!movementPhase && yourTurn) { //checks if attack phase
            int attackPosX = getPosXFromEvent(event);
            int attackPosY = getPosYFromEvent(event); //sets position attacked
            ArrayList<Tile> attackTargets = game.getAttackableTiles();

            if (game.checkForLegalAttack(attackPosY,attackPosX,attackTargets)) {

                //adds the attack to attacklist
                attackList.add(new Attack(turn, match_id, user_id, selectedUnit.getPieceId(), grid.tileList[attackPosY][attackPosX].getUnit().getPieceId(), selectedUnit.getAttack()));

                game.executeAttack(attackPosY,attackPosX);

                //play audio clip.
                selectedUnit.getAudio().play(); //plays the audio clip associated with the unit type

                deselect();
            }
        }
    }

    /**
     * Highlights all possible tiles that a unit can move, by filling
     * tile with with movement color. This method calls upon the method getMovementPossibleTiles()
     * to find which tiles should be filled.
     * @see GameLogic
     */
    private void highlightPossibleMoves() {
        if (unitSelected) { //checks if there is a unit selected
            ArrayList<Tile> movementTargets = game.getMovementPossibleTiles();

            //colors all tiles that are possible movement targets green
            //colors all tiles that are possible movement targets green
            for(Tile t : movementTargets){
                t.setFill(movementHighlightColor);
            }
        }
    }

    /**
     * Highlights all possible tiles that a unit can attack, by filling
     * tile with attack color. This method calls upon the method getAttackableTiles()
     * to find which tiles should be filled.
     * @see GameLogic
     */
    private void highlightPossibleAttacks() {
        if (unitSelected) { //checks if there is a unit selected
            ArrayList<Tile> attackTargets = game.getAttackableTiles();

            //colors all attackable tiles red

            for(Tile t : attackTargets){
                t.setFill(attackHighlightColor);
            }
        }
    }

    /**
     * Clear all fill from tiles in Grid.list. and set it so the default
     * tile color.
     */
    private void clearHighLight() {
        for (int i = 0; i < grid.tileList.length; i++) {
            for (int j = 0; j < grid.tileList[i].length; j++) {
                grid.tileList[i][j].setFill(standardTileColor);
            }
        }
    }

    /**
     * Is a static method which sets the description used in the sidebar.
     * It is used in Recruit, but can also be called from other Classes.
     * @param newDescription Takes a String with the description that should show in the sidebar.
     * @param newDescriptionHead Takes a String for the description head.
     * @see Recruit
     */
    public static void changeDescriptionLabel(String newDescription, String newDescriptionHead){
        descriptionHead.setText(newDescriptionHead);
        description.setText(newDescription);
    }

    /**
     * Is a static method which controls if the description used in the sidebar is visible.
     * It is used in Recruit, but can also be called from other Classes.
     * @param visible Takes a boolean to control whether the description and description head is visible or not.
     */
    public static void descriptionVisible(boolean visible){
        descriptionHead.setVisible(visible);
        description.setVisible(visible);
    }

    /**
     * Selects a unit, and depending on whether it is player's turn, or player unit, it does different actions.
     * This method uses getPosXFromEvent and getPosYFromEvent to get coordinates relative to Grid/Board, and uses those
     * coordinates to target the tile.
     * If unit is friendly, it sets a stroke around the tile, sets description for the tile in the sidebar,
     * if player is in attack phase, it will call upon highlightPossibleAttacks, or if player is in movement phase
     * it will call upon the method highlightPossibleMoves().
     * @param  event Takes in a MouseEvent to use with getPosXFromEvent and getPosYFromEvent.
     */
    private void select(MouseEvent event) {

        selectedPosX = getPosXFromEvent(event);
        selectedPosY = getPosYFromEvent(event);

        //checks if clicked tile has unit
        if (grid.tileList[selectedPosY][selectedPosX].getUnit() != null && !grid.tileList[selectedPosY][selectedPosX].getUnit().getEnemy()) {
            //selects unit and unitposition
            unitSelected = true;
            selectedUnit = grid.tileList[selectedPosY][selectedPosX].getUnit();

            System.out.println("Is the selected unit an enemy? " + selectedUnit.getEnemy());

            //colors selected tile
            grid.tileList[selectedPosY][selectedPosX].setStroke(selectionOutlineColor);
            grid.tileList[selectedPosY][selectedPosX].setStrokeType(standardStrokePlacement);
            grid.tileList[selectedPosY][selectedPosX].setStrokeWidth(selectedStrokeWidth);

            //displays possible moves or attacks
            if (yourTurn) {
                if (movementPhase) {
                    highlightPossibleMoves();
                } else if (selectedUnit.getHasAttackedThisTurn()) {
                    highlightPossibleAttacks();
                }
            }
            //displays the description of the unit
            description.setText(selectedUnit.getDescription());
            descriptionHead.setText(selectedUnit.getDescriptionHead());
            description.setVisible(true);
            descriptionHead.setVisible(true);
        } else {
            selectedPosX = -1;
            selectedPosY = -1;
        }
    }

    /**
     * Deselects the unit by resetting variables and change styling of tiles to go back to
     * default settings. Styles the tiles (Rectangle) in the Grid.list. It also removes description and clears onClick
     * items from sidebar. Uses static variables: selectedUnit, selectedPosX, selectedPosY, description, grid.
     * This method also calls on clearHighlight() method which sets the fill on every tile back to default settings.
     * @see Grid
     * @see Tile
     * @see javafx.scene.shape.Rectangle
     */
    private void deselect() {
        if (unitSelected) {
            //removes selection of unit tile
            grid.tileList[selectedPosY][selectedPosX].setStroke(standardStrokeColor);
            grid.tileList[selectedPosY][selectedPosX].setStrokeType(standardStrokePlacement);
            grid.tileList[selectedPosY][selectedPosX].setStrokeWidth(standardStrokeWidth);

            //removes unit selection and position
            selectedUnit = null;
            unitSelected = false;
            selectedPosX = -1;
            selectedPosY = -1;

            //removes unit description
            description.setText("");
            descriptionHead.setText("");
            description.setVisible(false);
            descriptionHead.setVisible(false);

            clearHighLight();
        }
    }

    /**
     * Returns X-coordinates which corresponds with the Grid Class by using
     * MouseEvent.MouseClicked as argument and turns the pixel coordinates
     * to grid coordinates by rounding up to nearest tile size.
     *
     * @param  event  Gets the coordinates from MouseEvent.MouseClicked.
     * @return returns an X position integer which corresponds with the grid.
     * @see         Grid
     */
    private int getPosXFromEvent(MouseEvent event) {
        return (int) Math.ceil((event.getX()) / tileSize) - 1;
    }

    /**
     * Returns Y-coordinates which corresponds with the Grid Class by using
     * MouseEvent.MouseClicked as argument and turns the pixel coordinates
     * to grid coordinates by rounding up to nearest tile size.
     *
     * @param  event  Gets the coordinates from MouseEvent.MouseClicked.
     * @return returns an Y position integer which corresponds with the grid.
     * @see         Grid
     */
    private int getPosYFromEvent(MouseEvent event) {
        return (int) Math.ceil((event.getY()) / tileSize) - 1;
    }

    /**
     * If this method is called when it is players turn, then the turn integer increments,
     * and it changes the text labels to Waiting for other players. It then exports the movement
     * and attacks done within players round to database using methods exportMoveList() and
     * exportAttackList() from Database class. After player is done with round, and the export is complete
     * it calls upon the sendTurn() method to change turns. It then deselects the unit using the deselect() method.
     * Finally this methods calls upon the metods checkForGameOver to see if player won, and waitForTurn() which only
     * triggers if player has not surrendered.
     * @see database.Database
     * @see GameLogic
     */
    private void endTurn() {
        if (yourTurn) {

            //Increments turn. Opponents Turn.
            turn++;
            yourTurn = false;
            turnCounter.setText("TURN: " + turn);
            endTurnButton.setText("Waiting for other player");


            ////SEND MOVEMENT////
            if (movementList.size() != 0) {
                db.exportMoveList(movementList); //when we use movement table use this

                movementList = new ArrayList<>(); //Resets the movementList for the next turn.
            }

            /////SEND ATTACKS////
            if (attackList.size() != 0) {
                db.exportAttackList(attackList);
                attackList = new ArrayList<>(); //Resets the attackList for the next turn.
            }

            //Add the next turn into the database.
            db.sendTurn(turn);

            //de-selects the currently selected unit
            deselect();

            //Resets hasAttackedThisTurn for all units
            for (int i = 0; i < grid.tileList.length; i++) {
                for (int j = 0; j < grid.tileList[i].length; j++) {
                    if (grid.tileList[i][j].getUnit() != null) {
                        grid.tileList[i][j].getUnit().setHasAttackedThisTurn(false);
                    }

                }
            }

            //Check if you have won
            checkForGameOver();

            //Wait for you next turn. Does not trigger if you have surrendered.
            if (!surrendered && !gameFinished) {
                waitForTurn();
            }
        }
    }

    /**
     * This method creates a new Thread which checks whose turn it is with the method
     * getTurnPlayer from Database Class and changes the static variable yourTurn=true
     * if method returns true.
     */
    private void waitForTurn() {
        // Runnable lambda implementation for turn waiting with it's own thread
        waitTurnRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while (!doStop) {
                    try {
                        while (!waitTurnThread.isInterrupted() && !yourTurn) {
                            if (gameFinished) {
                                this.doStop();
                                waitTurnThread = null;
                            }
                            Thread.sleep(threadTimer);
                            //When player in database matches your own user_id it is your turn again.
                            int getTurnPlayerResult = db.getTurnPlayer();
                            // If its your turn or you have left the game.
                            System.out.println("Whose turn is it? " + db.getTurnPlayer());
                            if (getTurnPlayerResult == user_id) {
                                yourTurn = true;
                                doStop = true;
                                waitTurnThread.interrupt();
                            }
                        }
                        //What will happen when it is your turn again.

                        //Increments turn. Back to your turn.
                        Platform.runLater(() -> setUpNewTurn());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public synchronized void doStop() {
                this.doStop = true;
            }

            @Override
            public synchronized boolean keepRunning() {
                return !this.doStop;
            }
        };

        waitTurnThread = new Thread(waitTurnRunnable);
        waitTurnThread.start();
    }

    /**
     * Sets up the next turn by deselecting Units, incrementing turn counter, and changes the the text of
     * the button to "End Turn" instead of "Waiting for other player" and phase label to "Movement phase" instead of
     * "Waiting for other player". Then it imports the movement and attack of the oppponent and executes the attacks and moves.
     * Finally the method calls for checkForGameOver() to see if player has been defeated.
     * @see database.Database
     */
    private void setUpNewTurn() {
        deselect();
        selectedUnit = null;
        turn++;
        movementPhase = true;
        turnCounter.setText("TURN: " + turn);
        endTurnButton.setText("End turn");
        phaseLabel.setText("MOVEMENT PHASE");

        //Keeps track of the moves made during the opponents turn
        ArrayList<Move> importedMovementList = db.importMoveList(turn - 1, match_id);
        //Keeps track of the attacks made during the opponents turn
        ArrayList<Attack> importedAttackList = db.importAttackList(turn - 1, match_id, opponent_id);


        ////EXECUTES MOVES FROM OPPONENTS TURN////
        for(Move m : importedMovementList){
            //gets the unit from the tile and removes it from the same tile
            Unit movingUnit = grid.tileList[m.getStartPosY()][m.getStartPosX()].getUnit();
            grid.tileList[m.getStartPosY()][m.getStartPosX()].removeUnit();
            //adds the unit to new tile
            grid.tileList[m.getEndPosY()][m.getEndPosX()].setUnit(movingUnit);
        }

        for(Attack a : importedAttackList){
            for (int j = 0; j < grid.tileList.length; j++) {
                for (int k = 0; k < grid.tileList[j].length; k++) {

                    if (grid.tileList[j][k].getUnit() != null && !grid.tileList[j][k].getUnit().getEnemy() && grid.tileList[j][k].getUnit().getPieceId() == a.getReceiverPieceID()) {
                        grid.tileList[j][k].getUnit().takeDamage(a.getDamage());

                        //If units health is zero. Remove it from the board.
                        if (grid.tileList[j][k].getUnit().getHp() <= 0) {
                            grid.tileList[j][k].removeUnit();
                        }
                    }
                }
            }
        }
        checkForGameOver();
    }

    /**
     * This method is called when a player hits the Surrender button.
     * This method sets up and opens a confirm dialog window to check if player is sure, if no go back
     * to game, if the player selects yes, the method closes the dialog box and calls upon the method
     * actualsurrender which saves the data to a database server and takes the rest of the dialog.
     */
    private void surrender() {
        Stage confirm_alert = new Stage();
        confirm_alert.initModality(Modality.APPLICATION_MODAL);
        confirm_alert.setTitle("Game over!");

        Text surrender_text = new Text();
        surrender_text.setText("Are you sure?");
        surrender_text.setStyle(fontSize32);

        JFXButton surrender_yes = new JFXButton("Yes");
        JFXButton surrender_no = new JFXButton("No");

        surrender_yes.setOnAction(event -> {
            actualSurrender();
            confirm_alert.close();
        });

        surrender_no.setOnAction(event -> confirm_alert.close());

        HBox buttons = new HBox();
        buttons.getChildren().addAll(surrender_yes, surrender_no);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(surrenderButtonSpacing);

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(surrenderContentSpacing);

        content.getChildren().addAll(surrender_text, buttons);
        Scene scene = new Scene(content, surrenderWidth, surrenderHeight);
        confirm_alert.initStyle(StageStyle.UNDECORATED);
        confirm_alert.setScene(scene);
        confirm_alert.showAndWait();
    }

    /**
     * This method is called upon by surrender() method, and this method
     * uses the method surrenderGame() from the Database Class to send information about
     * which player won, and increment games won for that player. It sets a variable
     * surrendered=true, which is checked by another method after the end of each turn.
     * This method ends the turn after setting the variable, and if it is not players turn
     * then the method checks for game over using the  checkForGameOver() method.
     * @see database.Database
     */
    public void actualSurrender(){
        db.surrenderGame();
        surrendered = true;

        if (yourTurn) {
            endTurn();
        } else {
            checkForGameOver();
        }
    }

    /**
     * Uses checkForEliminationVictory() method to see if opponent or player has pieces left on board, also checks the database
     * with the method db.checkForSurrencder() to check if opponent has surrendered.
     * If game ended either by defeat or by surrendering  It creates a popup window to show the
     * end game status to the players. If a player surrenders it will say you lost for that player, and you win for the other.
     * When the game ends, the methods db.incrementGames() is called to increment number of games player for both players. It will
     * also call upon the method db.incrementGamesWon() for the winning player. The message dialog has a button which will return the player
     * to the main menu. When user hits that button, this method uses the method gameCleanup() to reset variables if player wants to play again,
     * and send players to the mainmenu.
     * @see database.Database
     * @see GameLogic
     */
    private void checkForGameOver() {
        String win_loseText;
        String gameSummary = "";
        int loser = -1;
        int eliminationResult = game.checkForEliminationVictory();
        int surrenderResult = db.checkForSurrender();

        if (eliminationResult != -1) {
            gameFinished = true;
            gameSummary = "The game ended after a player's unit were all eliminated after " + turn + " turns\n";
            loser = eliminationResult;
        } else if (surrenderResult == user_id || surrenderResult == opponent_id) {
            gameFinished = true;
            gameSummary = "The game ended after a player surrendered the match after " + turn + " turns\n";
            loser = surrenderResult;
        }

        if (loser != -1) {
            //Game is won or lost.
            //Open alert window.
            Stage winner_alert = new Stage();
            winner_alert.initModality(Modality.APPLICATION_MODAL);
            winner_alert.setTitle("Game over!");

            Text winnerTextHeader = new Text();
            Text winnerText = new Text();
            winnerTextHeader.setStyle(fontSize32);
            winnerTextHeader.setBoundsType(TextBoundsType.VISUAL);
            db.incrementGamesPlayed();

            if (loser == user_id || eliminationResult == 1) {
                win_loseText = "You Lose!\n";
            } else if (loser == opponent_id || eliminationResult == 0) {
                win_loseText = "You Win!\n";
                db.incrementGamesWon();
            } else {
                win_loseText = "Something went wrong\n";
            }

            winnerTextHeader.setText(win_loseText);
            winnerText.setText(gameSummary);

            JFXButton endGameBtn = new JFXButton("Return to menu");

            endGameBtn.setOnAction(event -> {
                String fxmlDir = "/menus/fxml/MainMenu.fxml";
                Parent root = null;
                try {
                    root = FXMLLoader.load(this.getClass().getResource(fxmlDir));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                winner_alert.close();
                gameCleanUp();
                Main.rootScene.setRoot(root);
                Main.window.setScene(Main.rootScene);
            });

            VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            content.setSpacing(gameOverContentSpacing);
            content.getChildren().addAll(winnerTextHeader, winnerText, endGameBtn);
            Scene scene = new Scene(content, gameOverWidth, gameOverHeight);
            winner_alert.initStyle(StageStyle.UNDECORATED);
            winner_alert.setScene(scene);
            winner_alert.showAndWait();
        }
    }

    /**
     * Resets all of the game variables so that if player wants to play another game, all the
     * data will be loaded on a new game.
     */
    private void gameCleanUp() {
        //Stuff that need to be closed or reset. Might not warrant its own method.
        if (waitTurnThread != null) {
            waitTurnRunnable.doStop();
            waitTurnThread.interrupt();
        }
        if (waitPlacementThread != null) {
            waitPlacementRunnable.doStop();
            waitPlacementThread.interrupt();
        }

        //Resets variables to default.
        turn = 1;
        obstacles = null;
        grid = null;
        player1 = -1;
        player2 = -1;
        match_id = -1;
        opponent_id = -1;
        unitGenerator = null;
        selectedUnit = null;
        selectedPosX = -1;
        selectedPosY = -1;
        //endTurnButton = null;
    }

    /**
     * Method for setting up different panes containing the UI elements.
     * Sets up Grid and styles it.
     * @see Grid
     * @return Pane, returns the pane that contains the grid.
     */
    private Pane createGrid() {
        Pane gridPane = new Pane();

        gridPane.getChildren().add(grid);

        gridPane.setLayoutX(gridXPadding);
        gridPane.setLayoutY(gridYPadding);
        root.getChildren().add(gridPane);

        return gridPane;
    }

    /**
     * Creates a sidebar panel for placement phase, and adds the buttons, and Recruits to it.
     * This method also stylizes and sets up resourcelabel, descriptionHead, and adds description
     * to the pane. This method returns the pane with all the labels and buttons in it.
     * @return Pane
     * @see Recruit
     * @see RecruitTile
     */
    private Pane createRecruitPane() { //adds unit selector/recruiter and styles it
        Pane unitPane = new Pane();
        unitPane.setPrefWidth(725);

        resourceLabel.setStyle(fontSize32 + arialFont);
        resourceLabel.setText("Resources: $" + currentResources);

        HBox resourceLabelBox = new HBox();
        if (unitTypeList.size() < 5) {
            resourceLabelBox.setMinWidth((unitTypeList.size() * tileSize) + ((unitTypeList.size() - 1) * unitPadding));
        } else {
            resourceLabelBox.setMinWidth(unitTilesWidth);
        }
        resourceLabelBox.setAlignment(Pos.CENTER);
        resourceLabelBox.getChildren().add(resourceLabel);

        recruitUnits = new FlowPane(Orientation.HORIZONTAL, unitPadding, unitPadding);
        recruitUnits.setMinWidth(unitTilesWidth);

        for (int i = 0; i < unitTypeList.size(); i++) {
            RecruitTile tile = new RecruitTile(tileSize, tileSize, unitGenerator.newRecruit(unitTypeList.get(i)));
            recruitUnits.getChildren().add(tile);
        }
        recruitUnits.setLayoutY(unitTilesYPadding);

        descriptionHead.setStyle(arialFont + descriptionHeadFontSize);
        descriptionHead.setLayoutX(recruitContentXPadding);
        descriptionHead.setLayoutY(placementDescriptionYPadding);
        descriptionHead.setVisible(false);

        description.setStyle(arialFont);
        description.setLayoutX(recruitContentXPadding);
        description.setLayoutY(placementDescriptionYPadding + 2 * descriptionHeadHeigth);
        description.setVisible(false);

        unitPane.getChildren().addAll(resourceLabelBox, recruitUnits, descriptionHead, description);

        unitPane.setLayoutX(recruitXPadding);
        unitPane.setLayoutY(recruitYPadding);
        root.getChildren().add(unitPane);

        return unitPane;
    }

    /**
    * Adds a label to the board to communicate to the player to add units to the board.
    * It also adds an arrow from the recruitTiles to the middle of the playable area of
    * the board depending on the players side of the board.
    */
    private void showPlaceUnitLabel() {
      placeUnitPane = new Pane();

      placeUnitLabel.setStyle(placeUnitLabelBackgroundColor + fontSize32 + arialFont);
      placeUnitLabel.setAlignment(Pos.CENTER);
      placeUnitLabel.setMinWidth(placeUnitLabelWidth);
      placeUnitLabel.setMinHeight(placeUnitLabelHeight);

      if (user_id == player1) {
        arrow = drawArrow(recruitXPadding, recruitYPadding + unitTilesYPadding + (tileSize / 2), gridXPadding + ((tileSize*boardSize) / 2), gridYPadding + tileSize);
      } else {
        arrow = drawArrow(recruitXPadding, recruitYPadding + unitTilesYPadding + (tileSize / 2), gridXPadding + ((tileSize*boardSize) / 2), gridYPadding + tileSize*(boardSize - 1));
      }

      placeUnitPane.getChildren().add(placeUnitLabel);
      placeUnitPane.setLayoutX(gridXPadding + ((tileSize*boardSize - placeUnitLabelWidth) / 2));
      placeUnitPane.setLayoutY(gridYPadding + ((tileSize*boardSize - placeUnitLabelHeight) / 2));

      root.getChildren().addAll(placeUnitPane, arrow);
    }

    /**
    * Method for removing placeUnitLabel and arrow after first unit is added to the board
    * @see Tile
    */
    public static void removePlaceUnitLabel() {
      root.getChildren().remove(placeUnitPane);
      root.getChildren().remove(arrow);
    }

    /**
    * Creates an arrow path to be added by showPlaceUnitLabel() to illustrate how to add
    * units to the board. The mathod takes the start and end x and y positions
    * @param startX double value for start x position of arrow
    * @param startY double value for start y position of arrow
    * @param endX double value for end x position of arrow
    * @param endY double value for end y position of arrow
    * @return Path
    */
    private Path drawArrow(double startX, double startY, double endX, double endY) {
      Path arrow = new Path();

      arrow.setStrokeWidth(arrowWidth);
      arrow.setStroke(arrowColor);

      //Line
      arrow.getElements().add(new MoveTo(startX, startY));
      arrow.getElements().add(new LineTo(endX, endY));

      //ArrowHead
      double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
      double sin = Math.sin(angle);
      double cos = Math.cos(angle);
      //point1
      double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
      double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
      //point2
      double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
      double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;

      arrow.getElements().add(new LineTo(x1, y1));
      arrow.getElements().add(new MoveTo(endX, endY));
      arrow.getElements().add(new LineTo(x2, y2));

      return arrow;
    }

    /**
     * Creates the sidepanel for the placement/movement/attack phase, and serves useful
     * for showing descriptions and buttons like Surrender and End Turn.
     * Returns the sidepanelPane.
     * @return Pane
     */
    private Pane createSidePanel() { //creates the side panel for movement/attack phase
        Pane sidePanel = new Pane();
        sidePanel.setPrefWidth(625);

        turnCounter.setStyle(fontSize32 + arialFont);
        turnCounter.setLayoutX(turnCounterXPadding);
        turnCounter.setLayoutY(turnCounterYPadding);

        descriptionHead.setStyle(arialFont + descriptionHeadFontSize);
        descriptionHead.setLayoutX(sidePanelContentXPadding);
        descriptionHead.setLayoutY(descriptionYPadding);
        descriptionHead.setVisible(false);

        description.setStyle(arialFont);
        description.setLayoutX(sidePanelContentXPadding);
        description.setLayoutY(descriptionYPadding + descriptionHeadHeigth);
        description.setVisible(false);

        sidePanel.getChildren().addAll(turnCounter, descriptionHead, description);

        sidePanel.setLayoutX(sidePanelXPadding);
        sidePanel.setLayoutY(sidePanelYPadding);
        root.getChildren().add(sidePanel);

        return sidePanel;
    }

    /**
     * Sets up a pane on the top of the game with a label to show status for turns,
     * phase info, etc. For example: Placement phase to indicate that player is in that phase,
     * or waiting for opponent to show that it is not players turn.
     * Returns the phaseLabelPane.
     * @return Pane
     */
    private Pane createPhaseLabelPane() {
        Pane phaseLabelPane = new Pane();

        phaseLabel.setStyle(fontSize32 + arialFont);
        phaseLabel.setMinHeight(phaseLabelHeight);

        HBox phaseLabelBox = new HBox();
        phaseLabelBox.setMinWidth(tileSize*boardSize - 2 * standardStrokeWidth);
        phaseLabelBox.setAlignment(Pos.CENTER);
        phaseLabelBox.getChildren().add(phaseLabel);

        phaseLabelPane.getChildren().add(phaseLabelBox);

        phaseLabelPane.setLayoutX(gridXPadding);
        phaseLabelPane.setLayoutY(phaseLabelYPadding);
        root.getChildren().add(phaseLabelPane);

        phaseLabelPane.toBack();

        return phaseLabelPane;
    }

    /**
     * Executes when application shuts down. User is logged out and database connection is closed.
     * Calls on surrender method to show a message dialog to user that game ended before user is logged out.
     */
    @Override
    public void stop() {
        // Executed when the application shuts down. User is logged out and database connection is closed.
        actualSurrender();
        Main.closeAndLogout();
    }

    /**
     * Calls for shutdown methods if program is closed. It is used to log out user
     * and close connections.
     * @param args Takes in arguemnts for the main method.
     */
    public void main(String[] args) {
        System.out.println("SHUTDOWN HOOK CALLED");
        Runtime.getRuntime().addShutdownHook(new Thread(Main::closeAndLogout));
        launch(args);
    }
}
