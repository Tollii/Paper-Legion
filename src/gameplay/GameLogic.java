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

import static database.Variables.db;
import static database.Variables.user_id;


public class GameLogic extends Application {
    private static final int boardSize = 7; // 7x7 for example
    static final int tileSize = 100; //Size(in pixels) of each tile
    private static Unit[][] unitPosition = new Unit[boardSize][boardSize];
    private static final int offsetX = 100;
    private static final int offsetY = 100;
    private final int initialWindowSizeX = 1024;
    private final int initialWindowSizeY = 768;
    private Thread thread;

    ////SCENE ELEMENTS////
    private Scene scene;                                //Scene for second and third phase of the game
    private HBox root = new HBox();                     //Root container
    private StackPane pieceContainer = new StackPane(); //Unit and obstacle placement
    private VBox rSidePanel = new VBox();               //Sidepanel for unit description and End turn button
    private Label description = new Label();
    private JFXButton endTurnButton = new JFXButton("End turn");
    private JFXButton surrenderButton = new JFXButton("Surrender");
    private Pane board = new Pane();                    // Holds all the tiles.
    private Grid grid = new Grid(boardSize, boardSize); //Sets up a grid which is equivalent to boardSize x boardSize.
    private Label turnCounter = new Label("TURN: " + turn);            //Describes what turn it is.

    ////GAME CONTROL VARIABLES////
    private int selectedPosX;                                   //Holds the X position to the selected piece.
    private int selectedPosY;                                   //Holds the Y position to the selected piece.
    private int moveCounter = 0;                                // Counter for movement phase.
    private int attackCount = 0;                                // Counter for attack phase.
    private Unit selectedUnit;                                  //Reference to the selected unit. Used for move, attack, etc.
    private boolean selected = false;                           // True or false for selected piece.
    private ArrayList<Move> movementList = new ArrayList<>();   //Keeps track of the moves made for the current turn.
    private boolean movementPhase = true;                       //Controls if the player is in movement or attack phase
    private UnitGenerator unitGenerator = new UnitGenerator();
    ArrayList<PieceSetup> setupPieces;
    ArrayList<Unit> unitList = new ArrayList<Unit>();

    ////AUDIO ELEMENTS////
    private AudioClip sword = new AudioClip(this.getClass().getResource("/gameplay/assets/hitSword.wav").toString());
    private AudioClip bow = new AudioClip(this.getClass().getResource("/gameplay/assets/arrow.wav").toString());

    ////COLORS////
    private Paint movementHighlightColor = Color.GREENYELLOW;
    private Paint attackHighlightColor = Color.DARKRED;


    //////////////////////////GAME INFO FROM MYSQL//////////////////////////////////////////////////////////////////////////////////
    //Match: player1, player2, match id, game_started                                                                             //
    //Attacks: turn_id, attacker, receiever, match_id, damage_dealt                                                               //
    //Movements: turn_id, piece_id, match_id, start_pos, end_pos                                                                  //
    //Pieces: piece_id, match_id, position, player                                                                                //
    //Turns: turn_id, match_id, player                                                                                            //
    //Unit_types: unit_type_id, unit_name, max_health, attack, attack_range, ability_cooldown                                     //
    //Units: piece_id, match_id, current_health, current_attack, current_attack_range, current_ability_cooldown,unit_type_id      //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void start(Stage primaryStage) throws Exception {

        Stage window = primaryStage; // Program window

        // Sets static variables for players and opponent id.
        db.getPlayers();

        sceneSetUp();

        SetUp setUp = new SetUp();
        setUp.importUnitTypes();

        ///Inserts units into DB for game. Only player1 draws the units. This is a temporary filler for the placement phase.
        if (user_id == player1) {
            db.insertPieces();
        } else {
            int number = 0;
            do {

                number = db.pollForUnits();
                thread.sleep(5000);
            } while (number != 10);
        }
        createUnits();

        drawUnits();

        //If you are player 2. Start polling the database for next turn.
        if (!yourTurn) {
            endTurnButton.setText("Waiting for other player");
            waitForTurn();
        } else {
            //Enters turn 1 into database.
            db.sendTurn(turn);
        }

        ////END TURN HANDLER////
        endTurnButton.setOnAction(event -> {
            endTurn();
        });

        ////SURRENDER HANDLER////
        surrenderButton.setOnAction(event -> {
            surrender();
        });


        ///////////////////////////////////SELECTION//////////////////////////////////////////////
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!selected) {
                select(event, rSidePanel, description);
            }
            ////////////////////////////SELECTION END/////////////////////////////////////////////

            /////////////////////////////////MOVE/////////////////////////////////////////////////
            if (event.getClickCount() == 1) {
                if (selected && movementPhase && event.getButton() == MouseButton.PRIMARY) {
                    move(event);
                }
            }
            ///////////////////////////////MOVE END///////////////////////////////////////////////

            /////////////////////////////////ATTACK///////////////////////////////////////////////
            if (event.getClickCount() == 2) {
                if (selected && !movementPhase && !selectedUnit.getHasAttackedThisTurn()) {

                    attack(event);

                }
            }
            //////////////////////////////ATTACK END////////////////////////////////////////////

            //////////////////////////////DESELECT/////////////////////////////////////////////
            if (event.getButton() == MouseButton.SECONDARY) {

                deSelect(rSidePanel, description);
            }
            //////////////////////////DESELECT END/////////////////////////////////////////////

        }); // MOUSE EVENT END

        ///////////////////////////////////////////////////////////////////////////////////////


        window.setTitle("BINARY WARFARE");
        window.setScene(scene);
        window.show();
    }

    private void sceneSetUp() {
        root.getChildren().add(pieceContainer);
        root.getChildren().add(rSidePanel);
        root.setSpacing(150);
        root.setPadding(new Insets(offsetY, offsetX, offsetY, offsetX));

        pieceContainer.setAlignment(Pos.BASELINE_LEFT); //Only baseline_Left is correct according to positions.
        pieceContainer.setPrefWidth(900);

        board.getChildren().add(grid.gp); //Insert grid from Grid class.
        pieceContainer.getChildren().add(board);  //Legger alle tiles til i stackpane som blir lagt til scenen.

        turnCounter.setStyle("-fx-font-size:32px;");

        endTurnButton.setPrefWidth(150);
        endTurnButton.setPrefHeight(75);
        endTurnButton.setTextFill(Color.WHITE);
        endTurnButton.setStyle("-fx-background-color: #000000");

        surrenderButton.setPrefWidth(150);
        surrenderButton.setPrefHeight(75);
        surrenderButton.setTextFill(Color.WHITE);
        surrenderButton.setStyle("-fx-background-color: #000000");

        description.setStyle("-fx-font-family: 'Arial Black'");
        description.setPadding(new Insets(0, 0, 350, 0));

        rSidePanel.setPrefWidth(250);
        rSidePanel.getChildren().addAll(turnCounter,endTurnButton,surrenderButton);
        rSidePanel.setPrefWidth(650);
        rSidePanel.setAlignment(Pos.BOTTOM_CENTER);

        // IF INSERTS ARE ADDED THEN REMEMBER THAT THE OFFSET VALUE HAS TO WORK WITH THE TILES AND PIECES POSITION.
        //window.widthProperty().addListener();

        scene = new Scene(root, initialWindowSizeX, initialWindowSizeY);
    }

    private void endTurn(){
        if (yourTurn) {
            //Increments turn. Opponents Turn.
            turn++;


            turnCounter.setText("TURN: " + turn);
            endTurnButton.setText("Waiting for other player");
            yourTurn = false;


            ////SEND MOVEMENT////

            //db.exportMoveList(movementList); when we use movement table use this
            if (movementList.size() != 0) {
                db.exportPieceMoveList(movementList);
                movementList = new ArrayList<>(); //Resets the movementList for the next turn.
            }


            /////SEND ATTACKS////
            //TODO Having a attackList just like movementList. Alternative solution below.

            // Alternative solution to creating a List of attacks in the future.
            // Finds every enemy unit that was damaged and sends their new info the database.
            //Wait for you next turn

            //Add the next turn into the database.
            db.sendTurn(turn);
            deSelect(rSidePanel, description);
            selectedUnit = null;
            winner();
            waitForTurn();
        }
    }




    private void surrender() {
        //TODO
        Stage confirm_alert = new Stage();
        confirm_alert.initModality(Modality.APPLICATION_MODAL);
        confirm_alert.setTitle("Game over!");

        Text surrender_text = new Text();
        surrender_text.setText("Are you sure?");
        surrender_text.setStyle("-fx-font-size:32px;");

        JFXButton surrender_yes = new JFXButton("Yes");
        JFXButton surrender_no = new JFXButton("No");

        surrender_yes.setOnAction(event -> {
            db.surrenderGame();
        });

        surrender_no.setOnAction(event -> {
            //TODO
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(surrender_yes,surrender_no);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(20);

        content.getChildren().addAll(surrender_text,buttons);
        Scene scene = new Scene(content, 250, 150);
        confirm_alert.initStyle(StageStyle.UNDECORATED);
        confirm_alert.setScene(scene);
        confirm_alert.showAndWait();
    }

    private void createUnits() {
        setupPieces = db.importPlacementPieces();
        for (int i = 0; i < setupPieces.size(); i++) {

            int pieceId = setupPieces.get(i).getPieceId();
            int matchId = setupPieces.get(i).getMatchId();
            int playerId = setupPieces.get(i).getPlayerId();
            int positionX = setupPieces.get(i).getPositionX();
            int positionY = setupPieces.get(i).getPositionY();
            int unitType_id = setupPieces.get(i).getUnit_type_id();

            boolean enemyStatus;
            if (playerId == user_id) {
                enemyStatus = false;
            } else {
                enemyStatus = true;
            }

            if (unitType_id == 1) {
                unitList.add(new Unit(positionY * tileSize, positionX * tileSize, enemyStatus, unitGenerator.newSwordsMan(), pieceId));

            } else if (unitType_id == 2) {
                unitList.add(new Unit(positionY * tileSize, positionX * tileSize, enemyStatus, unitGenerator.newArcher(), pieceId));
            }
        }
    }


    private void drawUnits() {
        ArrayList<PieceSetup> pieces = db.importPlacementPieces();
        int posX;
        int posY;

        ///////////////////////////////LOAD ALL PIECES ONTO BOARD ///////////////////////////////
        for (int i = 0; i < unitList.size(); i++) {
            System.out.println(i + " playerid: " + pieces.get(i).getPlayerId() + " pos X: " + pieces.get(i).getPositionX() + " pos y: "
                    + pieces.get(i).getPositionY() + " hp: " + pieces.get(i).getCurrent_health() + " piece id: "
                    + pieces.get(i).getPieceId());
            if (unitList.get(i).getHp() > 0) {

                PieceSetup correspondingPiece = null;

                //TODO review this for-loop
                for (int j = 0; j < pieces.size(); j++) {
                    if (unitList.get(i).getEnemy()) {
                        if (pieces.get(j).getPlayerId() != user_id && pieces.get(j).getPieceId() == unitList.get(i).getPieceID()) {
                            correspondingPiece = pieces.get(j);
                            System.out.println(i + " is enemy " + j + " in this position in Pieces");

                        }
                    } else {
                        if (pieces.get(j).getPlayerId() == user_id && pieces.get(j).getPieceId() == unitList.get(i).getPieceID()) {
                            correspondingPiece = pieces.get(j);
                            System.out.println(i + " is friendly " + j + " in this position in Pieces");
                        }
                    }
                }

                if (correspondingPiece != null) {

                    unitList.get(i).setHasAttackedThisTurn(false);
                    unitList.get(i).setHp(correspondingPiece.getCurrent_health());
                    unitList.get(i).setPosition(correspondingPiece.getPositionX(), correspondingPiece.getPositionY());
                    if (unitList.get(i).getHp() > 0) {
                        pieceContainer.getChildren().add(unitList.get(i).getPieceAvatar());
                        posX = unitList.get(i).getPositionX();
                        posY = unitList.get(i).getPositionY();
                        unitPosition[posY][posX] = unitList.get(i);
                    }

                }
            } else {
            }
            System.out.println(i + " enemy: " + unitList.get(i).getEnemy() + " pos X: " + unitList.get(i).getPositionX() + " pos y: "
                    + unitList.get(i).getPositionY() + " hp: " + unitList.get(i).getHp() + " piece id: "
                    + unitList.get(i).getPieceID());
        }
        ///////////////////////////////////////////////////////////////////////////////////
    }

    public void deDrawUnits() {
        for (int i = 0; i < unitList.size(); i++) {
            pieceContainer.getChildren().remove(unitList.get(i).getPieceAvatar());
        }


        for (int i = 0; i < unitPosition.length; i++) {
            for (int j = 0; j < unitPosition[i].length; j++) {
                unitPosition[i][j] = null;
            }
        }
        System.out.println(pieceContainer.getChildren().size());
    }

    private void select(MouseEvent event, VBox vBox, Label description) {

        int posX = getPosXFromEvent(event);
        int posY = getPosYFromEvent(event);


        if (!(posX > boardSize || posY > boardSize || posX < 0 || posY < 0) && (unitPosition[posY][posX] != null) && !unitPosition[posY][posX].getEnemy() && unitPosition[posY][posX].getHp() > 0) {

            unitPosition[posY][posX].setPosition((int) (unitPosition[posY][posX].getTranslateX() / tileSize), (int) (unitPosition[posY][posX].getTranslateY() / tileSize));
            unitPosition[posY][posX].setStrokeType(StrokeType.INSIDE);
            unitPosition[posY][posX].setStrokeWidth(3);
            unitPosition[posY][posX].setStroke(Color.RED);

            selected = true;

            selectedPosX = posX;
            selectedPosY = posY;

            selectedUnit = unitPosition[selectedPosY][selectedPosX];

            if (movementPhase) {
                highlightPossibleMoves();
            } else if (!selectedUnit.getHasAttackedThisTurn()) {
                highlightPossibleAttacks();
            }

            description.setText(selectedUnit.getDescription());
            vBox.getChildren().add(description);
            description.toBack();

        }
    }

    private void deSelect(VBox sidePanel, Label description) {

        for (int i = 0; i < unitPosition.length; i++) {

            for (int j = 0; j < unitPosition[i].length; j++) {
                if (unitPosition[i][j] != null) {
                    unitPosition[i][j].setStroke(Color.TRANSPARENT);
                }
            }
        }

        sidePanel.getChildren().remove(description);
        selectedUnit = null;
        selected = false;
        clearHighlight();
    }

    private void move(MouseEvent event) {
        if (yourTurn) {
            int nyPosX = getPosXFromEvent(event);
            int nyPosY = getPosYFromEvent(event);

            if (movementRange(nyPosX, nyPosY)) {
                if (unitPosition[nyPosY][nyPosX] == null) {
                    selectedUnit.setTranslate(nyPosX, nyPosY);
                    clearHighlight();

                    ////ADDS A NEW MOVE TO THE MOVE LIST////
                    movementList.add(new Move(turn, selectedUnit.getPieceID(), match_id, selectedUnit.getPositionX(), selectedUnit.getPositionY(), nyPosX, nyPosY));

                    ////EXECUTES THE MOVE////
                    unitPosition[nyPosY][nyPosX] = selectedUnit;
                    unitPosition[selectedPosY][selectedPosX] = null;

                    selectedPosX = nyPosX;
                    selectedPosY = nyPosY;

                    selectedUnit.setPosition(nyPosX, nyPosY);


                    moveCounter++;

                    ////CHANGES PHASE////
                    movementPhase = false;
                    clearHighlight();
                    highlightPossibleAttacks();
                }
            }
        }
    }

    private void attack(MouseEvent event) {

        int attackPosX = getPosXFromEvent(event);
        int attackPosY = getPosYFromEvent(event);
        int id;
        // If there is a unit on the selected tile.
        if (yourTurn) {
            if (unitPosition[attackPosY][attackPosX] != null && unitPosition[attackPosY][attackPosX].getHp() > 0) {
                // If within attack range.
                if (attackRange(attackPosX, attackPosY)) {
                    // If attacked unit is not itself.
                    if (selectedUnit != unitPosition[attackPosY][attackPosX]  && unitPosition[attackPosY][attackPosX].getEnemy()) {
                        // Attack is executed and unit takes damage.
                        unitPosition[attackPosY][attackPosX].takeDamage(selectedUnit.getAttack());

                        attackCount++;

                        //Plays audio cue for each type.
                        if (selectedUnit.getType().equalsIgnoreCase("Swordsman")) {
                            sword.play();
                        } else if (selectedUnit.getType().equalsIgnoreCase("Archer")) {
                            bow.play();
                        }
                        db.sendHealthInfo(unitPosition[attackPosY][attackPosX].getPieceID(), unitPosition[attackPosY][attackPosX].getHp());


                        //If units health is zero. Remove it from the board.
                        if (unitPosition[attackPosY][attackPosX].getHp() <= 0) {
                            //TODO legg til at uniten blir skada inn i databasen med en gang, før den blir slettet. (sett hp 0)
                            pieceContainer.getChildren().removeAll(unitPosition[attackPosY][attackPosX].getPieceAvatar());
                            //unitPosition[attackPosY][attackPosX].setHp(0);
                            for (int i = 0; i < unitList.size(); i++) {
                                if (attackPosX == unitList.get(i).getPositionX() && attackPosY == unitList.get(i).getPositionY()) {
                                    //unitList.remove(i);
                                }
                            }
                            unitPosition[attackPosY][attackPosX] = null;
                            unitList.removeAll(Collections.singletonList(null));

                        }

                        selectedUnit.setHasAttackedThisTurn(true);
                        clearHighlight();
                    }
                }
            }
        }
    }


    private void highlightPossibleMoves() {
        int posX = selectedPosX;
        int posY = selectedPosY;
        int movementRange = selectedUnit.getMovementRange();

//        System.out.println(selectedPosX + "SelectposX");
//        System.out.println("PosX+1: " + (posX + 2));
//        System.out.println("PosX-1: " + (posX - 2));
//        System.out.println("PosY+1: " + (posY + 2));
//        System.out.println("PosY-1: " + (posY - 2));

        ///////////////////////LEFT, RIGHT, UP, DOWN//////////////////////////
        if (selectedPosX - 1 >= 0) {
            grid.liste[posY][posX - 1].setFill(movementHighlightColor);
        }

        if (selectedPosX + 1 < boardSize) {
            grid.liste[posY][posX + 1].setFill(movementHighlightColor);
        }

        if (selectedPosY - 1 >= 0) {
            grid.liste[posY - 1][posX].setFill(movementHighlightColor);
        }

        if (selectedPosY + 1 < boardSize) {
            grid.liste[posY + 1][posX].setFill(movementHighlightColor);
        }

        //////////////////////////////////////////////////////////////////////


        ////////////////////////////CORNERS///////////////////////////////////

        if (selectedPosX + 1 < boardSize && selectedPosY + 1 < boardSize) {
            grid.liste[posY + 1][posX + 1].setFill(movementHighlightColor);
        }

        if (selectedPosX - 1 >= 0 && selectedPosY - 1 >= 0) {
            grid.liste[posY - 1][posX - 1].setFill(movementHighlightColor);
        }

        if (selectedPosX - 1 >= 0 && selectedPosY + 1 < boardSize) {
            grid.liste[posY + 1][posX - 1].setFill(movementHighlightColor);
        }

        if (selectedPosX + 1 < boardSize && selectedPosY - 1 >= 0) {
            grid.liste[posY - 1][posX + 1].setFill(movementHighlightColor);

        }
        ////////////////////////////////////////////////////////////////////

        //////////////IF PIECE HAS LONGER RANGE THAN 1////////////////////////////
        if (selectedUnit.getMovementRange() > 1) {

            if (selectedPosX - movementRange >= 0) {
                grid.liste[posY][posX - movementRange].setFill(movementHighlightColor);
            }

            if (selectedPosX + movementRange < boardSize) {
                grid.liste[posY][posX + movementRange].setFill(movementHighlightColor);
            }

            if (selectedPosY - movementRange >= 0) {
                grid.liste[posY - movementRange][posX].setFill(movementHighlightColor);
            }

            if (selectedPosY + movementRange < boardSize) {
                grid.liste[posY + movementRange][posX].setFill(movementHighlightColor);
            }


        }

        ///////////////////////////////////////////////////////////////////
    }

    private void highlightPossibleAttacks() {

        for (int i = 0; i < unitPosition.length; i++) {
            for (int j = 0; j < unitPosition[i].length; j++) {
                if (unitPosition[i][j] != null && unitPosition[i][j] != selectedUnit) {

                    // Currently shows swordsman attack range wrong.
                    //(((Math.abs(selectedPosX - unitPosition[i][j].getTranslateX() / tileSize)) + Math.abs(selectedPosY - unitPosition[i][j].getTranslateY() / tileSize)) <= selectedUnit.getMaxAttackRange())
                    //                            && ((Math.abs(selectedPosX - unitPosition[i][j].getTranslateX() / tileSize)) + Math.abs(selectedPosY - unitPosition[i][j].getTranslateY() / tileSize)) >= selectedUnit.getMinAttackRange()
                    if (attackRange(j, i)) {

                        if (unitPosition[i][j].getEnemy()) {
                            grid.liste[i][j].setFill(attackHighlightColor);
                        }
                    }
                }
            }
        }
    }

    private void clearHighlight() {
        for (int i = 0; i < grid.liste.length; i++) {
            for (int j = 0; j < grid.liste[i].length; j++) {
                grid.liste[i][j].setFill(Color.TRANSPARENT);

            }
        }

    }


    private boolean movementRange(int nyPosX, int nyPosY) {

        ///////////////////////ORDINARY MOVEMENT RANGE == 1//////////////////////
        if (selectedUnit.getMovementRange() < 2) {
            if ((Math.abs(nyPosX - selectedUnit.getPositionX()) < 2) && (Math.abs(nyPosY - selectedUnit.getPositionY()) < 2)) {
                return true;
            } else {
                return false;
            }
        }

        ////////////////////////////////////////////////////////////////////

        /////////////MOVEMENT RANGE> 1///////////////////////////////////////

        if (Math.abs(nyPosX - selectedPosX) + Math.abs(nyPosY - selectedPosY) <= selectedUnit.getMovementRange()) { //Beautiful math skills in progress.
            return true;
        }


//        if (!(Math.abs(nyPosX - unitPosition[selectedPosY][selectedPosX].getOldPosX()) > unitPosition[selectedPosY][selectedPosX].getRange()) &&
//                (!(Math.abs(nyPosY - unitPosition[selectedPosY][selectedPosX].getOldPosY()) > unitPosition[selectedPosY][selectedPosX].getRange()))) {
//            return true;
//        }

        ////////////////////////////////////////////////////////////////////
        return false;
    }

    private boolean attackRange(int nyPosX, int nyPosY) {

        ///////////////////////ORDINARY ATTACK RANGE == 1//////////////////////
        if (selectedUnit.getMaxAttackRange() < 2) {
            if ((Math.abs(nyPosX - selectedUnit.getPositionX()) < 2) && (Math.abs(nyPosY - selectedUnit.getPositionY()) < 2)) {
                return true;
            } else {
                return false;
            }
        }

        ////////////////////////////////////////////////////////////////////

        /////////////ATTACK RANGE > 1///////////////////////////////////////

        if (Math.abs(nyPosX - selectedPosX) + Math.abs(nyPosY - selectedPosY) <= selectedUnit.getMaxAttackRange()) { //Beautiful math skills in progress.
            return true;
        }


//        if (!(Math.abs(nyPosX - unitPosition[selectedPosY][selectedPosX].getOldPosX()) > unitPosition[selectedPosY][selectedPosX].getRange()) &&
//                (!(Math.abs(nyPosY - unitPosition[selectedPosY][selectedPosX].getOldPosY()) > unitPosition[selectedPosY][selectedPosX].getRange()))) {
//            return true;
//        }

        ////////////////////////////////////////////////////////////////////
        return false;
    }

    private int getPosXFromEvent(MouseEvent event2) {
        double rectPosX1 = tileSize + offsetX;
        double posX1 = event2.getSceneX();
        double movementX1 = posX1 - rectPosX1;
        return (int) (Math.ceil(movementX1 / tileSize)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }

    private int getPosYFromEvent(MouseEvent event2) {
        double rectPosY1 = tileSize + offsetY;
        double posY1 = event2.getSceneY();
        double movementY1 = posY1 - rectPosY1;
        return (int) (Math.ceil(movementY1 / tileSize)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }

    // method that checks if the game has been won by either player. Returns 1 if you lost, 0 if you won or -1 if noone has won yet.
    private int checkForWinner() {
        int yourPieces = 0;
        int opponentsPieces = 0;
        //Goes through all units and counts how many are alive for each player.
        for (int i = 0; i < unitList.size(); i++) {
            if (unitList.get(i).getHp() > 0) {
                if (unitList.get(i).getEnemy()) {
                    opponentsPieces++;
                } else {
                    yourPieces++;
                }
            }
        }
        System.out.println("YourPiceces " + yourPieces + " opponent: " + opponentsPieces);
        if (yourPieces == 0) {
            return 1;
        } else if (opponentsPieces == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    // Opens the winner/loser pop-up on the screen and ends the game.
    public void winner(){
        int winnerOrLoser = checkForWinner();
        if(db.checkForSurrender() == opponent_id){
            winnerOrLoser = 0;
        }
        if (winnerOrLoser != -1) {
            //Game is won or lost.
            Stage winner_alert = new Stage();
            winner_alert.initModality(Modality.APPLICATION_MODAL);
            winner_alert.setTitle("Game over!");

            Text winner = new Text();
            winner.setStyle("-webkit-flex-wrap: nowrap;-moz-flex-wrap: nowrap;-ms-flex-wrap: nowrap;-o-flex-wrap: nowrap;-khtml-flex-wrap: nowrap;flex-wrap: nowrap;t-size:32px;");
            db.incrementGamesPlayed();
            if (winnerOrLoser == 1){
                db.incrementGamesWon();
                winner.setText("You Lose");
            }
            else {
                winner.setText("You win!");
            }
            JFXButton endgame = new JFXButton("Return to menu");
            // maxHeight="30.0" maxWidth="90.0" minHeight="30.0" minWidth="90.0" prefHeight="30.0" prefWidth="90.0" style="-fx-background-color: #e3e4e5#e3e4e5;" text="Play"

            VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            content.setSpacing(20);
            content.getChildren().addAll(winner,endgame);
            Scene scene = new Scene(content,250,150);
            winner_alert.initStyle(StageStyle.UNDECORATED);
            winner_alert.setScene(scene);
            winner_alert.showAndWait();
        }
    }


    public static void main(String[] args) {
        //Shutdown hook. CLoses stuff when program exits.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (user_id > 0) {
                db.logout(user_id);
            }
            try {
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));

        SetUp setUp = new SetUp();
        setUp.importUnitTypes();

        launch(args);
    }

    private void waitForTurn() {
        //TODO make waitForTurn() it's own class and Interupt() and an AtomicBoolean as a control variable

        thread = new Thread(() -> {
            try {
                while (!yourTurn) {
                    Thread.sleep(5000);
                    System.out.println("Whose turn is it? " + db.getTurnPlayer());
                    //Checks the database continually to check if the turn registered in the database is yours.
                    if (db.getTurnPlayer() == user_id) {
                        yourTurn = true;
                    }
                    if (yourTurn) {
                        Platform.runLater(
                                () -> {
                                    thread.stop();


                                    //What will happen when it is your turn again.

                                    //Increments turn. Back to your turn.
                                    deSelect(rSidePanel, description);
                                    selectedUnit = null;

                                    //Increment the turn variable
                                     turn++;

                                    //Updates UI
                                    turnCounter.setText("TURN: " + turn);
                                    endTurnButton.setText("End turn");

                                    //All units are removed from the visual board and then drawn in again in the correct locations.
                                    deDrawUnits();
                                    drawUnits();

                                    //Checks if the opponent won the game.
                                    winner();

                                    //MovementPhase boolean is reset so that you can start your movementphase.
                                    movementPhase = true;
                                });
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public void stop() {
        // Executed when the application shuts down. User is logged out and database connection is closed.
        if (user_id > 0) {
            db.logout(user_id);
        }
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
