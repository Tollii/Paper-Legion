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

import java.io.IOException;

import static database.Variables.*;

import java.util.ArrayList;

import javafx.geometry.Orientation;
import menus.Main;

public class GameMain extends Application {

    ////BOARD SIZE CONTROLS////
    private static final int playerSideSize = 2; //Used to set width of the placement area

    ////SCENE ELEMENTS////
    private Pane root;
    private static final Label description = new Label();                //description label for the selected unit
    private final static Label resourceLabel = new Label();    //Static so that Tile can update the label
    private final Label  turnCounter = new Label("TURN: " + turn); //describe which turn it is
    private final Label phaseLabel = new Label();
    private JFXButton endTurnButton;                                //button for ending turn
    private static FlowPane recruitUnits;

    ////WINDOW SIZE////
    private final double windowWidth = screenWidth;
    private final double windowHeight = screenHeight;

    ////SIZE VARIABLES////

    //BUTTONS SIZE//
    private final int buttonWidth = 175;
    private final int buttonHeight = 75;

    //LABELS SIZES//
    private final int phaseLabelWidth = 300;
    private final int phaseLabelHeight = 50;
    private final int resourceLabelWidth = 300;

    //RECRUIT PANE UNIT TILES WIDTH//
    private final int unitPadding = 5;
    private final int unitTilesWidth = tileSize * 5 + unitPadding * 4;

    ////PANE PADDINGS////
    private final int buttonYPadding = 500;

    //GRID//
    private final int gridXPadding = (int)(windowWidth * 0.15);
    private final int gridYPadding = (int)(windowHeight * 0.10);

    //PLACEMENT PHASE SIDE PANEL//
    private final int recruitXPadding = gridXPadding + tileSize * boardSize + (int)(screenWidth * 0.08);
    private final int resourceLabelXPadding = ((unitTilesWidth - resourceLabelWidth) / 2);

    //MOVEMENT AND ATTACK PHASE SIDE PANEL//
    private final int sidePanelXPadding = gridXPadding + tileSize * boardSize + (int)(screenWidth * 0.08);
    private final int descriptionXPadding = 0;

    //PHASELABEL PANE//
    private final int phaseLabelXPadding = gridXPadding + ((tileSize * boardSize - phaseLabelWidth) / 2);
    private final int phaseLabelYPadding = ((gridYPadding - phaseLabelHeight) / 2);

    ////GAME CONTROL VARIABLES////
    private boolean unitSelected = false;
    private ArrayList<Move> movementList = new ArrayList<>();           //Keeps track of the moves made for the current turn.
    private ArrayList<Attack> attackList = new ArrayList<>();           //Keeps track of the attacks made for the current turn.
    private boolean movementPhase = true;                               //Controls if the player is in movement or attack phase
    private boolean surrendered = false;
    private boolean gameFinished = false;
    public static boolean hasPlacedAUnit = false;

    ////THREAD TIMER////
    private final int threadTimer = 1000;

    ////UNIT GENERATOR////
    private UnitGenerator unitGenerator = new UnitGenerator();

    private final String descriptionFont = "-fx-font-family: 'Arial Black'";
    private final String buttonBackgroundColor = "-fx-background-color: #000000";
    private final String fontSize32 = "-fx-font-size:32px;";
    private final Paint buttonTextColor = Color.WHITE;
    private final Paint movementHighlightColor = Color.GREENYELLOW;
    private final Paint attackHighlightColor = Color.DARKRED;
    private final Paint untargetableTileColor = Color.color(155.0 / 255.0, 135.0 / 255.0, 65.0 / 255.0);

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

        SetUp setUp = new SetUp();
        setUp.importUnitTypes();

        root = new Pane();
        Scene scene = new Scene(root, windowWidth, windowHeight);

        Pane gridPane = createGrid(); //creates the grid

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

        System.out.println("There are " + obstacles.size() + " obstacles");

        //Adds obstacles to the board.
        for (int i = 0; i < obstacles.size(); i++) {
            System.out.println(i + " - X: " + obstacles.get(i).getPosX() + " - Y:" + obstacles.get(i).getPosY());
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

        resourceLabel.setText("Resources: " + currentResources);

        Pane recruitPane = createRecruitPane();

        description.setStyle(descriptionFont);
        description.setLayoutX(descriptionXPadding);
        int placementDescriptionYPadding = 200;
        description.setLayoutY(placementDescriptionYPadding);
        descriptionVisible(true);


        JFXButton finishedPlacingButton = new JFXButton("Finished placing units"); //creates a button for ending the placementphase
        finishedPlacingButton.setMinSize(buttonWidth, buttonHeight);
        finishedPlacingButton.setTextFill(buttonTextColor);
        finishedPlacingButton.setStyle(buttonBackgroundColor);
        int placementButtonXPadding = 150;
        finishedPlacingButton.setLayoutX(placementButtonXPadding);
        finishedPlacingButton.setLayoutY(buttonYPadding);

        recruitPane.getChildren().addAll(description,finishedPlacingButton);

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
    static void updateResourceLabel() {
        resourceLabel.setText("Resources: " + currentResources);
    }

    /**
     * Deselects the other recruit tiles in the sidebar when selecting
     * another recruit tile. This method unstrokes the recruitTiles which is
     * not selected.
     * @see Recruit
     * @see RecruitTile
     */
    static void deselectRecruitTiles() {
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
        Pane phaseLabelPane = createPhaseLabelPane();
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

    ////METHOD FOR POLLING IF OPPONENT IS FINISHED WITH PLACEMENT PHASE////
    private void waitForOpponentReady() {
        // Runnable lambda implementation for turn waiting with it's own thread
        waitPlacementRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while (keepRunning()) {
                    try {
                        while (!opponentReady) {
                            Thread.sleep(threadTimer);
                            //When player in database matches your own user_id it is your turn again.

                            System.out.println(opponent_id + ": " + opponentReady);

                            if (db.checkIfOpponentReady()) {
                                opponentReady = true;

                                System.out.println(opponent_id + ": ready!");
                                this.doStop();
                            }
                        }

                        //What will happen when your opponent is ready.

                        Platform.runLater(() -> {
                            System.out.println("RUN LATER!!!");

                            importOpponentPlacementUnits();
                            movementActionPhaseStart();


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

    private void importOpponentPlacementUnits() {
        ArrayList<PieceSetup> importList = db.importPlacementUnits();
        System.out.println("SIZE OF IMPORT LIST: " + importList.size());

        for (PieceSetup piece : importList) {
            System.out.println("ADDING OPPONENT UNIT");
            grid.tileList[piece.getPositionY()][piece.getPositionX()].setUnit(unitGenerator.newEnemyUnit(piece.getUnit_type_id(), piece.getPieceId()));
        }
    }

    ////MOVEMENT AND ACTION PHASE STARTER////
    private void movementActionPhaseStart() {
        Pane sidePanel = createSidePanel();
        Pane phaseLabelPane = createPhaseLabelPane();

        endTurnButton = new JFXButton("End turn");
        endTurnButton.setMinSize(buttonWidth, buttonHeight);
        endTurnButton.setTextFill(buttonTextColor);
        endTurnButton.setStyle(buttonBackgroundColor);

        //button for surrendering
        JFXButton surrenderButton = new JFXButton("Surrender");
        surrenderButton.setMinSize(buttonWidth, buttonHeight);
        surrenderButton.setTextFill(buttonTextColor);
        surrenderButton.setStyle(buttonBackgroundColor);

        HBox hboxSidePanelButtons = new HBox();
        int buttonSpacing = 10;
        hboxSidePanelButtons.setSpacing(buttonSpacing);
        hboxSidePanelButtons.getChildren().addAll(endTurnButton, surrenderButton);
        sidePanel.getChildren().addAll(hboxSidePanelButtons);
        hboxSidePanelButtons.setLayoutY(buttonYPadding);

        movementPhase = true;
        phaseLabel.setText("MOVEMENT PHASE");

        //If you are player 2. Start polling the database for next turn.
        if (!yourTurn) {
            phaseLabel.setText("OPPONENT'S TURN");
            endTurnButton.setText("Waiting for other player");
            waitForTurn(endTurnButton);
        } else {
            //Enters turn 1 into database.
            db.sendTurn(turn);
        }

        ////BUTTON EvenT HANDLERS////
        endTurnButton.setOnAction(event -> {
            endTurn(endTurnButton);
            phaseLabel.setText("OPPONENT'S TURN");

        });

        surrenderButton.setOnAction(event -> surrender(endTurnButton));

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
        System.out.println("X " + selectedPosX);
        System.out.println("Y " + selectedPosY);
        System.out.println(grid.tileList[selectedPosY][selectedPosX].getUnit());
        System.out.println(!grid.tileList[selectedPosY][selectedPosX].getUnit().getEnemy());
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
            description.setVisible(true);
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
            description.setVisible(false);

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

    ////TURN ENDING METHOD AND WAIT FOR TURN POLLER////
    private void endTurn(JFXButton endTurnButton) {
        if (yourTurn) {
            //Increments turn. Opponents Turn.
            turn++;

            turnCounter.setText("TURN: " + turn);
            endTurnButton.setText("Waiting for other player");
            yourTurn = false;

            ////SEND MOVEMENT////
            if (movementList.size() != 0) {
                System.out.println("SENDING MOVE LIST!");
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
            if (!surrendered) {
                waitForTurn(endTurnButton);
            }
        }
    }

    private void waitForTurn(JFXButton endTurnButton) {
        // Runnable lambda implementation for turn waiting with it's own thread
        waitTurnRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while (keepRunning()) {
                    try {
                        while (!yourTurn) {
                            if (gameFinished) {
                                this.doStop();
                                waitTurnThread = null;
                            }
                            System.out.println("Sleeps thread " + Thread.currentThread());
                            Thread.sleep(threadTimer);
                            //When player in database matches your own user_id it is your turn again.
                            int getTurnPlayerResult = db.getTurnPlayer();
                            // If its your turn or you have left the game.
                            System.out.println("Whose turn is it? " + db.getTurnPlayer());
                            if (getTurnPlayerResult == user_id || getTurnPlayerResult == -1) {
                                System.out.println("yourTurn changes");
                                yourTurn = true;
                                this.doStop();
                            }
                        }
                        //What will happen when it is your turn again.

                        //Increments turn. Back to your turn.
                        Platform.runLater(() -> setUpNewTurn(endTurnButton));

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

    ////SETS UP THE NEXT TURN BY COMMITTING OPPONENTS ATTACKS AND MOVEMENTS////
    private void setUpNewTurn(JFXButton endTurnButton) {
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

        System.out.println("importedAttackList size is: " + importedAttackList.size());

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
                        System.out.println(a.getDamage());

                        System.out.println("DOING AN ATTACK!" + grid.tileList[j][k].getUnit().getHp());

                        grid.tileList[j][k].getUnit().takeDamage(a.getDamage());

                        //If units health is zero. Remove it from the board.
                        if (grid.tileList[j][k].getUnit().getHp() <= 0) {
                            //TODO legg til at uniten blir skada inn i databasen med en gang, før den blir slettet. (sett hp 0). Legg også til syncing av Unit stats mellom de to klientene(via Units i DB)
                            grid.tileList[j][k].removeUnit();
                        }
                    }
                }

            }
        }
        checkForGameOver();
    }

    ////METHOD FOR HANDLING SURRENDER////
    private void surrender(JFXButton endTurnButton) {
        Stage confirm_alert = new Stage();
        confirm_alert.initModality(Modality.APPLICATION_MODAL);
        confirm_alert.setTitle("Game over!");

        Text surrender_text = new Text();
        surrender_text.setText("Are you sure?");
        surrender_text.setStyle(fontSize32);

        JFXButton surrender_yes = new JFXButton("Yes");
        JFXButton surrender_no = new JFXButton("No");

        surrender_yes.setOnAction(event -> {
            db.surrenderGame();
            surrendered = true;

            if (yourTurn) {
                endTurn(endTurnButton);
            } else {
                checkForGameOver();
            }
            confirm_alert.close();
        });

        surrender_no.setOnAction(event -> confirm_alert.close());

        HBox buttons = new HBox();
        buttons.getChildren().addAll(surrender_yes, surrender_no);
        buttons.setAlignment(Pos.CENTER);
        //SURRENDER WINDOW//
        int surrenderButtonSpacing = 50;
        buttons.setSpacing(surrenderButtonSpacing);

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        int surrenderContentSpacing = 20;
        content.setSpacing(surrenderContentSpacing);

        content.getChildren().addAll(surrender_text, buttons);
        int surrenderHeight = 180;////SURRENDER WINDOW SIZE////
        int surrenderWidth = 250;
        Scene scene = new Scene(content, surrenderWidth, surrenderHeight);
        confirm_alert.initStyle(StageStyle.UNDECORATED);
        confirm_alert.setScene(scene);
        confirm_alert.showAndWait();
    }

    ////METHODS FOR ENDING THE GAME////
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
                String fxmlDir = "/menus/View/mainMenu.fxml";
                Parent root = null;
                try {
                    root = FXMLLoader.load(this.getClass().getResource(fxmlDir));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("load failed");
                }
                winner_alert.close();
                gameCleanUp();
                Main.rootScene.setRoot(root);
                Main.window.setScene(Main.rootScene);
            });

            VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            //GAME OVER WINDOW//
            int gameOverContentSpacing = 20;
            content.setSpacing(gameOverContentSpacing);
            content.getChildren().addAll(winnerTextHeader, winnerText, endGameBtn);
            int gameOverHeight = 200;////GAME OVER WINDWOW SIZE////
            int gameOverWidth = 450;
            Scene scene = new Scene(content, gameOverWidth, gameOverHeight);
            winner_alert.initStyle(StageStyle.UNDECORATED);
            winner_alert.setScene(scene);
            winner_alert.showAndWait();
        }
    }

    private void gameCleanUp() {
        //Stuff that need to be closed or reset. Might not warrant its own method.
        if (waitTurnThread != null) {
            waitTurnRunnable.doStop();
            waitTurnThread = null;
        }
        if (waitPlacementThread != null) {
            waitPlacementRunnable.doStop();
            waitPlacementThread = null;
        }
        if (searchGameThread != null) {
            searchGameRunnable.doStop();
            searchGameThread = null;
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
    }
    /**
     * Method for setting up different panes containing the UI elements.
     * Sets up Grid and styles it.
     * @see Grid
     */
    private Pane createGrid() {
        Pane gridPane = new Pane();

        gridPane.getChildren().add(grid);

        gridPane.setLayoutX(gridXPadding);
        gridPane.setLayoutY(gridYPadding);
        root.getChildren().add(gridPane);

        return gridPane;
    }



    private Pane createRecruitPane() { //adds unit selector/recruiter and styles it
        Pane unitPane = new Pane();
        recruitUnits = new FlowPane(Orientation.HORIZONTAL, unitPadding, unitPadding);

        recruitUnits.setMinWidth(unitTilesWidth);

        for (int i = 0; i < SetUp.unitTypeList.size(); i++) {
            RecruitTile tile = new RecruitTile(tileSize, tileSize, unitGenerator.newRecruit(SetUp.unitTypeList.get(i)));
            recruitUnits.getChildren().add(tile);
        }

        int unitTilesYPadding = 60;
        recruitUnits.setLayoutY(unitTilesYPadding);

        resourceLabel.setMinWidth(resourceLabelWidth);
        resourceLabel.setLayoutX(resourceLabelXPadding);
        resourceLabel.setStyle(fontSize32);

        unitPane.getChildren().addAll(resourceLabel, recruitUnits);

        unitPane.setLayoutX(recruitXPadding);
        int recruitYPadding = 150;
        unitPane.setLayoutY(recruitYPadding);
        root.getChildren().add(unitPane);

        return unitPane;
    }

    private Pane createSidePanel() { //creates the side panel for movement/attack phase
        Pane sidePanel = new Pane();

        turnCounter.setStyle(fontSize32);
        int turnCounterXPadding = 0;
        turnCounter.setLayoutX(turnCounterXPadding);
        int turnCounterYPadding = 0;
        turnCounter.setLayoutY(turnCounterYPadding);

        description.setStyle(descriptionFont);
        description.setLayoutX(descriptionXPadding);
        int descriptionYPadding = 100;
        description.setLayoutY(descriptionYPadding);
        description.setVisible(false);

        sidePanel.getChildren().addAll(turnCounter, phaseLabel, description);

        sidePanel.setLayoutX(sidePanelXPadding);
        int sidePanelYPadding = 150;
        sidePanel.setLayoutY(sidePanelYPadding);
        root.getChildren().add(sidePanel);

        return sidePanel;
    }

    private Pane createPhaseLabelPane() {
        Pane phaseLabelPane = new Pane();

        phaseLabel.setStyle(fontSize32);
        phaseLabel.setMinWidth(phaseLabelWidth);
        phaseLabel.setMinHeight(phaseLabelHeight);

        phaseLabelPane.getChildren().add(phaseLabel);

        phaseLabelPane.setLayoutX(phaseLabelXPadding);
        phaseLabelPane.setLayoutY(phaseLabelYPadding);
        root.getChildren().add(phaseLabelPane);

        return phaseLabelPane;
    }

    ////SHUTDOWN METHODS////
    @Override
    public void stop() {
        // Executed when the application shuts down. User is logged out and database connection is closed.
        surrender(endTurnButton);
        Main.closeAndLogout();
    }

    /**
     * Is a static method which sets the description used in the sidebar.
     * It is used in Recruit, but can also be called from other Classes.
     * @param newDescription Takes a String with the description that should show in the sidebar.
     * @see Recruit
     */
    public static void changeDescriptionLabel(String newDescription){
        description.setText(newDescription);
    }

    /**
     * Is a static method which controls if the description used in the sidebar is visible.
     * It is used in Recruit, but can also be called from other Classes.
     * @param visible Takes a boolean to control whether the description is visible or not.
     */
    public static void descriptionVisible(boolean visible){
        description.setVisible(visible);
    }

    ////MAIN USED FOR SHUTDOWN HOOK////
    public void main(String[] args) {
        System.out.println("SHUTDOWN HOOK CALLED");
        Runtime.getRuntime().addShutdownHook(new Thread(Main::closeAndLogout));
        launch(args);
    }
}
