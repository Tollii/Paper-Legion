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
import database.Variables;
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
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static database.Variables.*;
import static database.Variables.waitTurnThread;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Orientation;
import menus.Main;

public class GameMain extends Application {

    ////BOARD SIZE CONTROLS////
    private static final int playerSideSize = 2; //Used to set width of the placement area

    ////SCENE ELEMENTS////
    private Pane root;
    public static Label description = new Label();                //description label for the selected unit
    private Label turnCounter = new Label("TURN: " + turn); //describe which turn it is
    private Label phaseLabel = new Label();
    private static Label resourceLabel = new Label();    //Static so that Tile can update the label
    private JFXButton endTurnButton;                                //button for ending turn
    private JFXButton surrenderButton;                              //button for surrendering

    ////WINDOW SIZE////
    private final double windowWidth = screenWidth;
    private final double windowHeight = screenHeight;

    ////SURRENDER WINDOW SIZE////
    private final int surrenderWidth = 250;
    private final int surrenderHeight = 180;

    ////GAME OVER WINDWOW SIZE////
    private final int gameOverWidth = 450;
    private final int gameOverHeight = 200;

    ////SIZE VARIABLES////

    //BUTTONS SIZE//
    private final int buttonWidth = 175;
    private final int buttonHeight = 75;

    //LABELS SIZES//
    private final int phaseLabelWidth = 300;
    private final int phaseLabelHeight = 50;
    private final int resourceLabelWidth = 300;

    //TILE STROKES//
    private final int standardStrokeWidth = 1;
    private final int selectedStrokeWidth = 3;

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
    private final int recruitYPadding = 150;
    private final int placementButtonXPadding = 150;
    private final int placementDescriptionYPadding = 200;
    private final int unitTilesYPadding = 60;
    private final int resourceLabelXPadding = (int)((unitTilesWidth - resourceLabelWidth) / 2);

    //MOVEMENT AND ATTACK PHASE SIDE PANEL//
    private final int sidePanelXPadding = gridXPadding + tileSize * boardSize + (int)(screenWidth * 0.08);
    private final int sidePanelYPadding = 150;
    private final int descriptionXPadding = 0;
    private final int descriptionYPadding = 100;
    private final int turnCounterXPadding = 0;
    private final int turnCounterYPadding = 0;
    private final int buttonSpacing = 10;

    //PHASELABEL PANE//
    private final int phaseLabelXPadding = gridXPadding + (int) ((tileSize * boardSize - phaseLabelWidth) / 2);
    private final int phaseLabelYPadding = (int) ((gridYPadding - phaseLabelHeight) / 2);

    //SURRENDER WINDOW//
    private final int surrenderButtonSpacing = 50;
    private final int surrenderContentSpacing = 20;

    //GAME OVER WINDOW//
    private final int gameOverContentSpacing = 20;

    ////GAME CONTROL VARIABLES////
    private boolean unitSelected = false;
    private ArrayList<Move> movementList = new ArrayList<>();           //Keeps track of the moves made for the current turn.
    private ArrayList<Attack> attackList = new ArrayList<>();           //Keeps track of the attacks made for the current turn.
    private ArrayList<Move> importedMovementList = new ArrayList<>();   //Keeps track of the moves made during the opponents turn
    private ArrayList<Attack> importedAttackList = new ArrayList<>();   //Keeps track of the attacks made during the opponents turn
    private boolean movementPhase = true;                               //Controls if the player is in movement or attack phase
    private boolean surrendered = false;
    private boolean gameFinished = false;
    public static boolean hasPlacedAUnit = false;

    ////THREAD TIMER////
    private final int threadTimer = 1000;

    ////UNIT GENERATOR////
    private UnitGenerator unitGenerator = new UnitGenerator();

    ////STYLING////
    private String gameTitle = "PAPER LEGION";
    private String descriptionFont = "-fx-font-family: 'Arial Black'";
    private String buttonBackgroundColor = "-fx-background-color: #000000";
    private String fontSize32 = "-fx-font-size:32px;";
    private Paint standardTileColor = Color.WHITE;
    private Paint selectionOutlineColor = Color.RED;
    private Paint buttonTextColor = Color.WHITE;
    private Paint movementHighlightColor = Color.GREENYELLOW;
    private Paint attackHighlightColor = Color.DARKRED;
    private Paint untargetableTileColor = Color.color(155.0 / 255.0, 135.0 / 255.0, 65.0 / 255.0);

    GameLogic game;

    public void start(Stage window) throws Exception {
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

        window.setTitle(gameTitle);
        window.setScene(scene);
        window.show();

    }

    private void addObstacles() {
        // Player 2 adds obstacles when he joins.
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

    ////PLACEMENT PHASE STARTER AND FINISHER METHODS////
    private void placementPhaseStart() {

        currentResources = startingResources; //Starts current resources to starting resources;

        resourceLabel.setText("Resources: " + currentResources);

        Pane recruitPane = createRecruitPane();

        description.setStyle(descriptionFont);
        description.setLayoutX(descriptionXPadding);
        description.setLayoutY(placementDescriptionYPadding);
        descriptionVisible(true);


        JFXButton finishedPlacingButton = new JFXButton("Finished placing units"); //creates a button for ending the placementphase
        finishedPlacingButton.setMinSize(buttonWidth, buttonHeight);
        finishedPlacingButton.setTextFill(buttonTextColor);
        finishedPlacingButton.setStyle(buttonBackgroundColor);
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

    static void updateResourceLabel() {
        resourceLabel.setText("Resources: " + currentResources);
    }

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

        if (exportUnitList != null) {
            db.exportPlacementUnits(exportUnitList, exportPositionXList, exportPositionYList);
        }

        db.setReady(true);
        waitForOpponentReady();
    }

    ////METHOD FOR POLLING IF OPPONENT IS FINISHED WITH PLACEMENT PHASE////
    private void waitForOpponentReady() {
        // Runnable lambda implementation for turn waiting with it's own thread
        RunnableInterface waitPlacementRunnable = new RunnableInterface() {
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

                                System.out.println(opponent_id + ": " + opponentReady);
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

        surrenderButton = new JFXButton("Surrender");
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

        surrenderButton.setOnAction(event -> {
            surrender(endTurnButton);
        });

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
                if (unitSelected && !movementPhase && !selectedUnit.getHasAttackedThisTurn() && event.getButton() == MouseButton.PRIMARY) {
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
                grid.tileList[selectedPosY][selectedPosX].setStroke(Color.BLACK);
                grid.tileList[selectedPosY][selectedPosX].setStrokeType(StrokeType.INSIDE);
                grid.tileList[selectedPosY][selectedPosX].setStrokeWidth(1);

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

    ////HIGHLIGHTING METHODS////
    private void highlightPossibleMoves() {
        if (unitSelected) { //checks if there is a unit selected
            ArrayList<Tile> movementTargets = game.getMovementPossibleTiles();

            //colors all tiles that are possible movement targets green
            //colors all tiles that are possible movement targets green
            for (int i = 0; i < movementTargets.size(); i++) {
                movementTargets.get(i).setFill(movementHighlightColor);
            }
        }
    }

    private void highlightPossibleAttacks() {
        if (unitSelected) { //checks if there is a unit selected
            ArrayList<Tile> attackTargets = game.getAttackableTiles();

            //colors all attackable tiles red
            for (int i = 0; i < attackTargets.size(); i++) {
                attackTargets.get(i).setFill(attackHighlightColor);
            }
        }
    }

    private void clearHighLight() {
        for (int i = 0; i < grid.tileList.length; i++) {
            for (int j = 0; j < grid.tileList[i].length; j++) {
                grid.tileList[i][j].setFill(standardTileColor);
            }
        }
    }

    ////UNIT SELECTOR AND DESELECTORS////
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
            grid.tileList[selectedPosY][selectedPosX].setStrokeType(StrokeType.INSIDE);
            grid.tileList[selectedPosY][selectedPosX].setStrokeWidth(selectedStrokeWidth);

            //displays possible moves or attacks
            if (yourTurn) {
                if (movementPhase) {
                    highlightPossibleMoves();
                } else if (!selectedUnit.getHasAttackedThisTurn()) {
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

    private void deselect() {
        if (unitSelected) {
            //removes selection of unit tile
            grid.tileList[selectedPosY][selectedPosX].setStroke(Color.BLACK);
            grid.tileList[selectedPosY][selectedPosX].setStrokeType(StrokeType.INSIDE);
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
            System.out.println("Turn increased in endTurn");
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
        RunnableInterface waitTurnRunnable = new RunnableInterface() {
            private boolean doStop = false;

            @Override
            public void run() {
                while (keepRunning()) {
                    try {
                        while (!yourTurn) {
                            if (gameFinished) {
                                //this.doStop();
                                checkForGameOver();
                                waitTurnThread = null;
                            }
                            System.out.println("Sleeps thread " + Thread.currentThread());
                            Thread.sleep(threadTimer);
                            //When player in database matches your own user_id it is your turn again.
                            int getTurnPlayerResult = db.getTurnPlayer();
                            // If its your turn or you have left the game.
                            System.out.println("Whose turn is it? " + db.getTurnPlayer());
                            if (getTurnPlayerResult == user_id || getTurnPlayerResult == -1) {
                                yourTurn = true;
                                this.doStop();
                            }
                        }
                        //What will happen when it is your turn again.

                        //Increments turn. Back to your turn.
                        Platform.runLater(() -> {

                            setUpNewTurn(endTurnButton);

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

        waitTurnThread = new Thread(waitTurnRunnable);
        waitTurnThread.start();
    }

    ////SETS UP THE NEXT TURN BY COMMITTING OPPONENTS ATTACKS AND MOVEMENTS////
    private void setUpNewTurn(JFXButton endTurnButton) {
        deselect();
        selectedUnit = null;
        turn++;
        System.out.println("Turn increased in setUpNewTurn");
        movementPhase = true;
        turnCounter.setText("TURN: " + turn);
        endTurnButton.setText("End turn");
        phaseLabel.setText("MOVEMENT PHASE");

        importedMovementList = db.importMoveList(turn - 1, match_id);
        importedAttackList = db.importAttackList(turn - 1, match_id, opponent_id);

        System.out.println("importedAttackList size is: " + importedAttackList.size());

        ////EXECUTES MOVES FROM OPPONENTS TURN////
        for (int i = 0; i < importedMovementList.size(); i++) {
            //gets the unit from the tile and removes it from the same tile
            Unit movingUnit = grid.tileList[importedMovementList.get(i).getStartPosY()][importedMovementList.get(i).getStartPosX()].getUnit();
            grid.tileList[importedMovementList.get(i).getStartPosY()][importedMovementList.get(i).getStartPosX()].removeUnit();
            //adds the unit to new tile
            grid.tileList[importedMovementList.get(i).getEndPosY()][importedMovementList.get(i).getEndPosX()].setUnit(movingUnit);
        }

        ////EXECUTES ATTACKS FROM OPPONENTS TURN////
        for (int i = 0; i < importedAttackList.size(); i++) {

            for (int j = 0; j < grid.tileList.length; j++) {
                for (int k = 0; k < grid.tileList[j].length; k++) {

                    if (grid.tileList[j][k].getUnit() != null && !grid.tileList[j][k].getUnit().getEnemy() && grid.tileList[j][k].getUnit().getPieceId() == importedAttackList.get(i).getReceiverPieceID()) {
                        System.out.println(importedAttackList.get(i).getDamage());

                        System.out.println("DOING AN ATTACK!" + grid.tileList[j][k].getUnit().getHp());

                        grid.tileList[j][k].getUnit().takeDamage(importedAttackList.get(i).getDamage());

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

        surrender_no.setOnAction(event -> {
            confirm_alert.close();
        });

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
            content.setSpacing(gameOverContentSpacing);
            content.getChildren().addAll(winnerTextHeader, winnerText, endGameBtn);
            Scene scene = new Scene(content, gameOverWidth, gameOverHeight);
            winner_alert.initStyle(StageStyle.UNDECORATED);
            winner_alert.setScene(scene);
            winner_alert.showAndWait();
        }
    }

    private void gameCleanUp() {
        //Stuff that need to be closed or reset. Might not warrant its own method.
        if (waitTurnThread != null) {
            waitTurnThread.stop();
            waitTurnThread = null;
        }
        if (waitPlacementThread != null) {
            waitPlacementThread.stop();
            waitPlacementThread = null;
        }
        //Resets variables to default.
        turn = 1;
        obstacles = null;
        grid = null;
        player1 = -1;
        player2 = -1;
        match_id = -1;
        opponent_id = -1;
        opponentReady = false;
        unitGenerator = null;
        selectedUnit = null;
        selectedPosX = -1;
        selectedPosY = -1;

    }

    ////METHODS FOR SETTING UP THE DIFFERENT PANES CONTAINING THE UI ELEMENTS////
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
        FlowPane units = new FlowPane(Orientation.HORIZONTAL, unitPadding, unitPadding);

        units.setPrefWidth(unitTilesWidth);

        for (int i = 0; i < SetUp.unitTypeList.size(); i++) {
            RecruitTile tile = new RecruitTile(tileSize, tileSize, unitGenerator.newRecruit(SetUp.unitTypeList.get(i)));
            units.getChildren().add(tile);
        }

        units.setLayoutY(unitTilesYPadding);

        resourceLabel.setMinWidth(resourceLabelWidth);
        resourceLabel.setLayoutX(resourceLabelXPadding);
        resourceLabel.setStyle(fontSize32);

        unitPane.getChildren().addAll(resourceLabel, units);

        unitPane.setLayoutX(recruitXPadding);
        unitPane.setLayoutY(recruitYPadding);
        root.getChildren().add(unitPane);

        return unitPane;
    }

    private Pane createSidePanel() { //creates the side panel for movement/attack phase
        Pane sidePanel = new Pane();

        turnCounter.setStyle(fontSize32);
        turnCounter.setLayoutX(turnCounterXPadding);
        turnCounter.setLayoutY(turnCounterYPadding);

        description.setStyle(descriptionFont);
        description.setLayoutX(descriptionXPadding);
        description.setLayoutY(descriptionYPadding);
        description.setVisible(false);

        sidePanel.getChildren().addAll(turnCounter, phaseLabel, description);

        sidePanel.setLayoutX(sidePanelXPadding);
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

    public static void changeDescriptionLabel(String newDescription){
        description.setText(newDescription);
    }

    public static void descriptionVisible(boolean visible){
        description.setVisible(visible);
    }

    ////MAIN USED FOR SHUTDOWN HOOK////
    public void main(String[] args) {
        System.out.println("SHUTDOWN HOOK CALLED");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Main.closeAndLogout();
        }));
        launch(args);
    }
}
