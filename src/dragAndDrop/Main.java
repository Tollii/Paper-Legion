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

import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;


public class Main extends Application {
    private static final int boardSize = 7; // 7x7 for example
    public static final int tileSize = 100; //
    static Piece[][] piecesListe = new Piece[boardSize][boardSize];
    public static final int offsetX = 100;
    public static final int offsetY = 100;
    private Label description = new Label();
    private int selectedPosX; //Holds the X position to the selected piece.
    private int selectedPosY; //Holds the Y position to the selected piece.
    private boolean selected = false; // True or false for selected piece.
    private GridPane ins = new GridPane(); // Holds all the tiles.
    private Grid testGrid = new Grid(boardSize, boardSize); //Sets up a grid which is equivalent to boardSize x boardSize.
    private int moveCounter =0; // Counter for movement phase.
    private int attackCount = 0; // Counter for attack phase.

    private AudioClip sword = new AudioClip(this.getClass().getResource("/dragAndDrop/assets/hitSword.wav").toString());
    private AudioClip bow = new AudioClip(this.getClass().getResource("/dragAndDrop/assets/arrow.wav").toString());





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

        //////////////////////////SCENE AND CONTAINER SETUP///////////////////////////////////////
        Stage window = primaryStage; // Program window
        Scene scene1;                //Scene for second and third phase of the game
        StackPane sp = new StackPane();
        sp.setAlignment(Pos.BASELINE_LEFT); //Only baseline_Left is correct according to positions.
        ins.getChildren().add(testGrid.gp); //Insert grid from Grid class.
        sp.getChildren().add(ins);  //Legger alle tiles til i stackpane som blir lagt til scenen.

        //BorderPane bp = new BorderPane();
        VBox vbox = new VBox();
        JFXButton endturn = new JFXButton("end turn");
        HBox hbox = new HBox();
        hbox.getChildren().add(sp);
        hbox.getChildren().add(vbox);
        hbox.setSpacing(150);
        sp.setPrefWidth(900);
        description.setStyle("-fx-font-family: 'Arial Black'");
        vbox.setPrefWidth(250);
        endturn.setPrefWidth(150);
        endturn.setPrefHeight(75);
        endturn.setTextFill(Color.WHITE);
        endturn.setStyle("-fx-background-color: #000000");
        vbox.getChildren().add(endturn);
        vbox.setPrefWidth(650);
        description.setPadding(new Insets(0,0,350,0));
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setPadding(new Insets(offsetY,offsetX,offsetY,offsetX));


        // IF INSETS ARE ADDED THEN REMEMBER THAT THE OFFSET VALUE HAS TO WORK WITH THE TILES AND PIECES POSITION.



        scene1 = new Scene(hbox, 1024, 768);

        ///////////////////////////////////SETUP END/////////////////////////////////////////////



        //////////////////////ADD ENEMY TO ARRAY; TEST SAMPLE /////////////////////////////////////
        piecesListe[0][1] = new Piece( 0*tileSize, 1*tileSize,  true, new UnitType("Archer",60,1,2));
        piecesListe[0][2] = new Piece( 0*tileSize, 2*tileSize,  true, new UnitType("Swordsman",120,2.5,1));
        piecesListe[1][4] = new Piece( 1*tileSize, 4*tileSize,  false, new UnitType("Archer",60,1,2));
        ///////////////////////////////////////////////////////////////////////////////////////////




        ///////////////////////////////LOAD ALL PIECES ONTO BOARD ///////////////////////////////
        for (int i = 0; i < piecesListe.length; i++) {
            for (int j = 0; j < piecesListe[i].length; j++) {
                if (piecesListe[i][j] != null) {
                    sp.getChildren().add(piecesListe[i][j]);


                }
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////



        ///////////////////////////////////SELECTION//////////////////////////////////////////////
        scene1.addEventHandler(MouseEvent.MOUSE_CLICKED, event2 -> {
            int counter=0;
            if(counter<1){
                if(!(event2.getButton() == MouseButton.SECONDARY)) {
                    int posX = getPosXFromEvent(event2);
                    int posY = getPosYFromEvent(event2);

                    if (piecesListe[posY][posX] != null) {
                        if (!selected) {
                            piecesListe[posY][posX].setOldPos(piecesListe[posY][posX].getTranslateX() / 100, piecesListe[posY][posX].getTranslateY() / 100);
                            piecesListe[posY][posX].setStrokeType(StrokeType.INSIDE);
                            piecesListe[posY][posX].setStrokeWidth(3);
                            piecesListe[posY][posX].setStroke(Color.RED);
                            selected = true;
                            selectedPosX = posX;
                            selectedPosY = posY;
                            counter++;
                            highlightPossibleMoves();
                            description.setText(piecesListe[selectedPosY][selectedPosX].getDescription());
                            vbox.getChildren().add(description);
                            description.toBack();

                        }
                    }
                }
            }
            ////////////////////////////SELECTION END/////////////////////////////////////////////

            /////////////////////////////////MOVE/////////////////////////////////////////////////
            if (event2.getClickCount() == 2){
                if(selected){
                    int nyPosX = getPosXFromEvent(event2);
                    int nyPosY = getPosYFromEvent(event2);
                    if(attackRange(nyPosX,nyPosY)){
                        if (piecesListe[nyPosY][nyPosX] == null) {
                            piecesListe[selectedPosY][selectedPosX].setTranslateX(nyPosX*100);
                            piecesListe[selectedPosY][selectedPosX].setTranslateY(nyPosY*100);
                            clearHighlight();
                            piecesListe[nyPosY][nyPosX] = piecesListe[selectedPosY][selectedPosX];
                            piecesListe[selectedPosY][selectedPosX] = null;
                            selectedPosX = nyPosX;
                            selectedPosY = nyPosY;
                            piecesListe[nyPosY][nyPosX].setOldPos(nyPosX,nyPosY);
                            moveCounter++;
                            highlightPossibleMoves();
                        }
                    }

                }
            }
            ///////////////////////////////MOVE END///////////////////////////////////////////////

            /////////////////////////////////ATTACK///////////////////////////////////////////////
            if(event2.getClickCount() == 2){
                if(selected){

                    int nyPosX = getPosXFromEvent(event2);
                    int nyPosY = getPosYFromEvent(event2);
                    if (piecesListe[nyPosY][nyPosX] != null) {
                        if(attackRange(nyPosX,nyPosY)){
                            if (piecesListe[selectedPosY][selectedPosX] != piecesListe[nyPosY][nyPosX]){
                                piecesListe[nyPosY][nyPosX].takeDamage(piecesListe[selectedPosY][selectedPosX].getDamageMultiplier());
                                attackCount++;
                                System.out.println(piecesListe[nyPosY][nyPosX].getHp());
                                if(piecesListe[selectedPosY][selectedPosX].getType().equalsIgnoreCase("Swordsman")){
                                    sword.play();
                                }

                                else if(piecesListe[selectedPosY][selectedPosX].getType().equalsIgnoreCase("Archer")){
                                    bow.play();
                                }

                                if (piecesListe[nyPosY][nyPosX].getHp() <= 0) {
                                    sp.getChildren().removeAll(piecesListe[nyPosY][nyPosX]);
                                    piecesListe[nyPosY][nyPosX] = null;
                                }
                            }
                        }
                    }

                }
            }
            //////////////////////////////ATTACK END////////////////////////////////////////////

            //////////////////////////////UNSELECT/////////////////////////////////////////////
            if(event2.getButton() == MouseButton.SECONDARY){
                for (int i = 0; i < piecesListe.length; i++) {
                    for (int j = 0; j < piecesListe[i].length; j++) {
                        if (piecesListe[i][j] != null) {
                            piecesListe[i][j].setStroke(Color.TRANSPARENT);
                        }
                    }
                }

                vbox.getChildren().remove(description);
                selected = false;
                counter = 0;
                clearHighlight();
            }
            //////////////////////////UNSELECT END/////////////////////////////////////////////

        }); // MOUSE EVENT END

        ///////////////////////////////////////////////////////////////////////////////////////




        window.setTitle("BINARY WARFARE");
        window.setScene(scene1);
        window.show();
    }


    private void highlightPossibleMoves(){
        int posX = selectedPosX;
        int posY = selectedPosY;
        int maxPossibleMoves = piecesListe[selectedPosY][selectedPosX].getRange();

        System.out.println(selectedPosX + "SelectposX");
        System.out.println("PosX+1: " +(posX+2));
        System.out.println("PosX-1: " +(posX-2));
        System.out.println("PosY+1: " +(posY+2));
        System.out.println("PosY-1: " +(posY-2));

        ///////////////////////LEFT, RIGHT, UP, DOWN//////////////////////////
        if(selectedPosX-1>=0){
            testGrid.liste[posY][posX-1].setFill(Color.DARKRED);
        }

        if(selectedPosX+1<boardSize){
            testGrid.liste[posY][posX + 1].setFill(Color.DARKRED);
        }

        if(selectedPosY-1>=0){
            testGrid.liste[posY - 1][posX].setFill(Color.DARKRED);
        }

        if(selectedPosY+1<boardSize){
            testGrid.liste[posY + 1][posX].setFill(Color.DARKRED);
        }

        //////////////////////////////////////////////////////////////////////


        ////////////////////////////CORNERS///////////////////////////////////

        if(selectedPosX+1<boardSize && selectedPosY+1<boardSize){
            testGrid.liste[posY + 1][posX + 1].setFill(Color.DARKRED);
        }

        if(selectedPosX-1>=0 && selectedPosY-1>=0){
            testGrid.liste[posY - 1][posX - 1].setFill(Color.DARKRED);
        }

        if(selectedPosX-1>=0 && selectedPosY+1<boardSize){
            testGrid.liste[posY + 1][posX - 1].setFill(Color.DARKRED);
        }

        if(selectedPosX+1<boardSize && selectedPosY-1>=0){
            testGrid.liste[posY - 1][posX + 1].setFill(Color.DARKRED);

        }
        ////////////////////////////////////////////////////////////////////

        //////////////IF PIECE HAS LONGER RANGE////////////////////////////
        if(piecesListe[selectedPosY][selectedPosX].getRange()>1){

            if(selectedPosX-maxPossibleMoves>=0){
                testGrid.liste[posY][posX-maxPossibleMoves].setFill(Color.DARKRED);
            }

            if(selectedPosX+maxPossibleMoves<boardSize){
                testGrid.liste[posY][posX + maxPossibleMoves].setFill(Color.DARKRED);
            }

            if(selectedPosY-maxPossibleMoves>=0){
                testGrid.liste[posY - maxPossibleMoves][posX].setFill(Color.DARKRED);
            }

            if(selectedPosY+maxPossibleMoves<boardSize){
                testGrid.liste[posY + maxPossibleMoves][posX].setFill(Color.DARKRED);
            }



        }

        ///////////////////////////////////////////////////////////////////
    }

    private void clearHighlight(){
        for (int i = 0; i < testGrid.liste.length; i++) {
            for (int j = 0; j < testGrid.liste[i].length; j++) {
                testGrid.liste[i][j].setFill(Color.TRANSPARENT);

            }
        }

    }


    private boolean attackRange(int nyPosX, int nyPosY) {
        if (!(Math.abs(nyPosX - piecesListe[selectedPosY][selectedPosX].getOldPosX()) > piecesListe[selectedPosY][selectedPosX].getRange()) && (!(Math.abs(nyPosY - piecesListe[selectedPosY][selectedPosX].getOldPosY()) > piecesListe[selectedPosY][selectedPosX].getRange()))) {
            return true;
        }
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
        launch(args);
    }

}
