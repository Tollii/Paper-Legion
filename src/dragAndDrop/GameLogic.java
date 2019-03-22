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


package dragAndDrop;

//TEST

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
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
import javafx.stage.Stage;


public class GameLogic extends Application {
    private static final int boardSize = 7; // 7x7 for example
    public static final int tileSize = 100; //
    static Unit[][] unitListe = new Unit[boardSize][boardSize];
    public static final int offsetX = 100;
    public static final int offsetY = 100;
    private final int initialWindowSizeX = 1024;
    private final int initialWindowSizeY = 768;


    ////SCENE ELEMENTS////
    private Scene scene;                                //Scene for second and third phase of the game
    private HBox root = new HBox();                     //Root container
    private StackPane pieceContainer = new StackPane(); //Unit and obstacle placement
    private VBox rSidePanel = new VBox();               //Sidepanel for unit description and End turn button
    private Label description = new Label();
    private JFXButton endTurnButton = new JFXButton("end turn");
    private Pane board = new Pane();                    // Holds all the tiles.
    private Grid grid = new Grid(boardSize, boardSize); //Sets up a grid which is equivalent to boardSize x boardSize.

    ////GAME CONTROL VARIABLES////
    private int selectedPosX;                           //Holds the X position to the selected piece.
    private int selectedPosY;                           //Holds the Y position to the selected piece.
    private int moveCounter = 0;                        // Counter for movement phase.
    private int attackCount = 0;                        // Counter for attack phase.
    private boolean selected = false;                   // True or false for selected piece.
    private boolean movementPhase = true;               //Controls if the player is in movement or attack phase
    private UnitGenerator unitGenerator = new UnitGenerator();

    ////AUDIO ELEMENTS////
    private AudioClip sword = new AudioClip(this.getClass().getResource("/dragAndDrop/assets/hitSword.wav").toString());
    private AudioClip bow = new AudioClip(this.getClass().getResource("/dragAndDrop/assets/arrow.wav").toString());

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

        sceneSetUp();


        //////////////////////ADD ENEMY TO ARRAY; TEST SAMPLE /////////////////////////////////////
        unitListe[0][1] = new Unit( 0*tileSize, 1*tileSize,  true, unitGenerator.newArcher());
        unitListe[0][2] = new Unit( 0*tileSize, 2*tileSize,  true, unitGenerator.newSwordsMan());
        unitListe[1][4] = new Unit( 1*tileSize, 4*tileSize,  false, unitGenerator.newSwordsMan());
        ///////////////////////////////////////////////////////////////////////////////////////////


        ///////////////////////////////LOAD ALL PIECES ONTO BOARD ///////////////////////////////
        for (int i = 0; i < unitListe.length; i++) {
            for (int j = 0; j < unitListe[i].length; j++) {
                if (unitListe[i][j] != null) {
                    pieceContainer.getChildren().add(unitListe[i][j].getPieceAvatar());
                }
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////


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
            if(event.getClickCount() == 2){
                if(selected && !movementPhase){

                    attack(event);

                }
            }
            //////////////////////////////ATTACK END////////////////////////////////////////////

            //////////////////////////////DESELECT/////////////////////////////////////////////
            if(event.getButton() == MouseButton.SECONDARY){

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
        root.setPadding(new Insets(offsetY,offsetX,offsetY,offsetX));

        pieceContainer.setAlignment(Pos.BASELINE_LEFT); //Only baseline_Left is correct according to positions.
        pieceContainer.setPrefWidth(900);

        board.getChildren().add(grid.gp); //Insert grid from Grid class.
        pieceContainer.getChildren().add(board);  //Legger alle tiles til i stackpane som blir lagt til scenen.


        endTurnButton.setPrefWidth(150);
        endTurnButton.setPrefHeight(75);
        endTurnButton.setTextFill(Color.WHITE);
        endTurnButton.setStyle("-fx-background-color: #000000");

        description.setStyle("-fx-font-family: 'Arial Black'");
        description.setPadding(new Insets(0,0,350,0));

        rSidePanel.setPrefWidth(250);
        rSidePanel.getChildren().add(endTurnButton);
        rSidePanel.setPrefWidth(650);
        rSidePanel.setAlignment(Pos.BOTTOM_CENTER);

        // IF INSERTS ARE ADDED THEN REMEMBER THAT THE OFFSET VALUE HAS TO WORK WITH THE TILES AND PIECES POSITION.
        //window.widthProperty().addListener();

        scene = new Scene(root, initialWindowSizeX, initialWindowSizeY);
    }

    private void select(MouseEvent event, VBox vBox, Label description){
        if (!(event.getButton() == MouseButton.SECONDARY)) {
            int posX = getPosXFromEvent(event);
            int posY = getPosYFromEvent(event);

            if (unitListe[posY][posX] != null) {
                if (!selected) {
                    unitListe[posY][posX].setOldPos(unitListe[posY][posX].getTranslateX() / 100, unitListe[posY][posX].getTranslateY() / 100);
                    unitListe[posY][posX].setStrokeType(StrokeType.INSIDE);
                    unitListe[posY][posX].setStrokeWidth(3);
                    unitListe[posY][posX].setStroke(Color.RED);
                    selected = true;
                    selectedPosX = posX;
                    selectedPosY = posY;
                    highlightPossibleMoves();
                    description.setText(unitListe[selectedPosY][selectedPosX].getDescription());
                    vBox.getChildren().add(description);
                    description.toBack();

                }
            }
        }
    }

    private void deSelect(VBox sidePanel, Label description){

        for (int i = 0; i < unitListe.length; i++) {

            for (int j = 0; j < unitListe[i].length; j++) {
                if (unitListe[i][j] != null) {
                    unitListe[i][j].setStroke(Color.TRANSPARENT);
                }
            }
        }

        sidePanel.getChildren().remove(description);
        selected = false;
        clearHighlight();

    }

    private void move(MouseEvent event){
        int nyPosX = getPosXFromEvent(event);
        int nyPosY = getPosYFromEvent(event);
        if(attackRange(nyPosX,nyPosY)){
            if (unitListe[nyPosY][nyPosX] == null) {
                unitListe[selectedPosY][selectedPosX].setTranslate(nyPosX, nyPosY);
                // unitListe[selectedPosY][selectedPosX].setTranslateX(nyPosX*100);
                //unitListe[selectedPosY][selectedPosX].setTranslateY(nyPosY*100);
                clearHighlight();
                unitListe[nyPosY][nyPosX] = unitListe[selectedPosY][selectedPosX];
                unitListe[selectedPosY][selectedPosX] = null;
                selectedPosX = nyPosX;
                selectedPosY = nyPosY;
                unitListe[nyPosY][nyPosX].setOldPos(nyPosX,nyPosY);
                moveCounter++;
                highlightPossibleMoves();

                movementPhase = false;
                clearHighlight();
                highlightPossibleAttacks();
            }
        }
    }

    private void attack(MouseEvent event){

        int nyPosX = getPosXFromEvent(event);
        int nyPosY = getPosYFromEvent(event);
        if (unitListe[nyPosY][nyPosX] != null) {
            if(attackRange(nyPosX,nyPosY)){
                if (unitListe[selectedPosY][selectedPosX] != unitListe[nyPosY][nyPosX]){
                    unitListe[nyPosY][nyPosX].takeDamage(unitListe[selectedPosY][selectedPosX].getAttack());
                    attackCount++;
                    System.out.println(unitListe[nyPosY][nyPosX].getHp());

                    if(unitListe[selectedPosY][selectedPosX].getType().equalsIgnoreCase("Swordsman")){
                        sword.play();
                    }

                    else if(unitListe[selectedPosY][selectedPosX].getType().equalsIgnoreCase("Archer")){
                        bow.play();
                    }

                    if (unitListe[nyPosY][nyPosX].getHp() <= 0) {
                        pieceContainer.getChildren().removeAll(unitListe[nyPosY][nyPosX].getPieceAvatar());
                        unitListe[nyPosY][nyPosX] = null;
                    }
                }
            }
        }
    }


    private void highlightPossibleMoves() {
        int posX = selectedPosX;
        int posY = selectedPosY;
        int movementRange = unitListe[selectedPosY][selectedPosX].getMovementRange();

        System.out.println(selectedPosX + "SelectposX");
        System.out.println("PosX+1: " + (posX + 2));
        System.out.println("PosX-1: " + (posX - 2));
        System.out.println("PosY+1: " + (posY + 2));
        System.out.println("PosY-1: " + (posY - 2));

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
        if(unitListe[selectedPosY][selectedPosX].getMovementRange() > 1){

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

    private void highlightPossibleAttacks(){

//        System.out.println(Math.abs(selectedPosX * 100 - unitListe[0][1].getTranslateX())/100);
  //      System.out.println(Math.abs(selectedPosY * 100 - unitListe[0][1].getTranslateY())/100);
//        System.out.println(unitListe[selectedPosY][selectedPosX].getMinAttackRange());
  //      System.out.println(unitListe[selectedPosY][selectedPosX].getMaxAttackRange());

        for(int i=0; i<unitListe.length; i++){
            for(int j=0; j<unitListe[i].length; j++){
                if (unitListe[i][j] != null && unitListe[i][j] != unitListe[selectedPosY][selectedPosX]) {


                    if (Math.abs(selectedPosX * 100 - unitListe[i][j].getTranslateX())/100 <= unitListe[selectedPosY][selectedPosX].getMaxAttackRange()
                            && Math.abs(selectedPosX * 100 - unitListe[i][j].getTranslateX())/100 >= unitListe[selectedPosY][selectedPosX].getMinAttackRange()
                            || Math.abs(selectedPosY * 100 - unitListe[i][j].getTranslateY())/100 <= unitListe[selectedPosY][selectedPosX].getMaxAttackRange()
                            && Math.abs(selectedPosY * 100 - unitListe[i][j].getTranslateY())/100 >= unitListe[selectedPosY][selectedPosX].getMinAttackRange()){

                        System.out.println(Math.abs(selectedPosX * 100 - unitListe[i][j].getTranslateX()));
                        System.out.println(Math.abs(selectedPosX * 100 - unitListe[i][j].getTranslateY()));

                        grid.liste[i][j].setFill(attackHighlightColor);
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


    private boolean attackRange(int nyPosX, int nyPosY) {

        ///////////////////////ORDINARY ATTACK RANGE == 1//////////////////////
        if (unitListe[selectedPosY][selectedPosX].getMovementRange()<2){
            if((Math.abs(nyPosX-unitListe[selectedPosY][selectedPosX].getOldPosX())<2) && (Math.abs(nyPosY- unitListe[selectedPosY][selectedPosX].getOldPosY())<2)){
                return true;
            } else{
                return false;
            }
        }

        ////////////////////////////////////////////////////////////////////

        /////////////ATTACK RANGE > 1///////////////////////////////////////

           if(Math.abs(nyPosX-selectedPosX)+Math.abs(nyPosY-selectedPosY) <= unitListe[selectedPosY][selectedPosX].getMovementRange()){ //Beautiful math skills in progress.
               return true;
           }


//        if (!(Math.abs(nyPosX - unitListe[selectedPosY][selectedPosX].getOldPosX()) > unitListe[selectedPosY][selectedPosX].getRange()) &&
//                (!(Math.abs(nyPosY - unitListe[selectedPosY][selectedPosX].getOldPosY()) > unitListe[selectedPosY][selectedPosX].getRange()))) {
//            return true;
//        }

        ////////////////////////////////////////////////////////////////////
        return false;
    }

    private int getPosXFromEvent(MouseEvent event2) {
        double rectPosX1 = tileSize+offsetX;
        double posX1 = event2.getSceneX();
        double movementX1 = posX1 - rectPosX1;
        return (int) (Math.ceil(movementX1 / 100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }

    private int getPosYFromEvent(MouseEvent event2) {
        double rectPosY1 = tileSize+offsetY;
        double posY1 = event2.getSceneY();
        double movementY1 = posY1 - rectPosY1;
        return (int) (Math.ceil(movementY1 / 100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }


    public static void main(String[] args) {

        SetUp setUp = new SetUp();
        setUp.importUnitTypes();

        launch(args);
    }
}
