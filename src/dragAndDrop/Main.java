
//  ██████╗ ██╗███╗   ██╗ █████╗ ██████╗ ██╗   ██╗    ██╗    ██╗ █████╗ ██████╗ ███████╗ █████╗ ██████╗ ███████╗
//  ██╔══██╗██║████╗  ██║██╔══██╗██╔══██╗╚██╗ ██╔╝    ██║    ██║██╔══██╗██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝
//  ██████╔╝██║██╔██╗ ██║███████║██████╔╝ ╚████╔╝     ██║ █╗ ██║███████║██████╔╝█████╗  ███████║██████╔╝█████╗
//  ██╔══██╗██║██║╚██╗██║██╔══██║██╔══██╗  ╚██╔╝      ██║███╗██║██╔══██║██╔══██╗██╔══╝  ██╔══██║██╔══██╗██╔══╝
//  ██████╔╝██║██║ ╚████║██║  ██║██║  ██║   ██║       ╚███╔███╔╝██║  ██║██║  ██║██║     ██║  ██║██║  ██║███████╗
//  ╚═════╝ ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝        ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
//


package dragAndDrop;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;


import java.util.ArrayList;


public class Main extends Application {
    static Tile[][] liste = new Tile[6][6];
    static Piece[][] piecesListe = new Piece[6][6];
    private static final int tileSize = 100; //
    static int selectedPosX; //Holder position X til den brikken som er markert.
    static int selectedPosY; //Holder position Y til den brikken som er markert.
    static boolean selected = false;
    GridPane ins = new GridPane(); // For alle tiles.
    static Grid testGrid = new Grid(6, 6); //Sets up Grid 6x6
    private int moveCounter =0;
    private int attackCount = 0;


    @Override
    public void start(Stage primaryStage) throws Exception {

        //////////////////////////SCENE AND CONTAINER SETUP///////////////////////////////////////
        Stage window = primaryStage; // Program window
        Scene scene1;                //Scene for second and third phase of the game
        StackPane sp = new StackPane();
        sp.setAlignment(Pos.BASELINE_LEFT); //Only baseline_Left is correct according to positions.
        ins.getChildren().add(testGrid.gp); //Insert grid from Grid class.
        sp.getChildren().add(ins);  //Legger alle tiles til i stackpane som blir lagt til scenen.
        scene1 = new Scene(sp, 800, 600);

        ///////////////////////////////////SETUP END/////////////////////////////////////////////



        //////////////////////ADD ENEMY TO ARRAY; TEST SAMPLE /////////////////////////////////////
        piecesListe[0][1] = new Piece(tileSize, tileSize, 0, 1, 100, true);
        piecesListe[0][2] = new Piece(tileSize, tileSize, 0, 2, 60, true);
        piecesListe[1][4] = new Piece(tileSize, tileSize, 1, 4, 100, false);
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

                            System.out.println(counter);
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
                    if (piecesListe[nyPosY][nyPosX] == null) {
                        piecesListe[selectedPosY][selectedPosX].setTranslateX(nyPosX*100);
                        piecesListe[selectedPosY][selectedPosX].setTranslateY(nyPosY*100);
                        piecesListe[nyPosY][nyPosX] = piecesListe[selectedPosY][selectedPosX];
                        piecesListe[selectedPosY][selectedPosX] = null;
                        selectedPosX = nyPosX;
                        selectedPosY = nyPosY;
                        moveCounter++;
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
                            if (piecesListe[selectedPosY][selectedPosX] != piecesListe[nyPosY][nyPosX]){
                                piecesListe[nyPosY][nyPosX].takeDamage();
                                attackCount++;
                                System.out.println(piecesListe[nyPosY][nyPosX].getHp());

                                if (piecesListe[nyPosY][nyPosX].getHp() <= 0) {
                                    sp.getChildren().removeAll(piecesListe[nyPosY][nyPosX]);
                                    piecesListe[nyPosY][nyPosX] = null;
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
                selected = false;
                counter = 0;
                System.out.println(counter);
            }
            //////////////////////////UNSELECT END/////////////////////////////////////////////

        }); // MOUSE EVENT END

        ///////////////////////////////////////////////////////////////////////////////////////



        window.setTitle("BINARY WARFARE");
        window.setScene(scene1);
        window.show();
    }





    private boolean withinBounds(int nyPosX, int nyPosY) {
        if (!(Math.abs(nyPosX - piecesListe[selectedPosY][selectedPosX].getOldPosX()) > 2) && (!(Math.abs(nyPosY - piecesListe[selectedPosY][selectedPosX].getOldPosY()) > 2))) {
            return true;
        }
        return false;
    }

    private int getPosXFromEvent(MouseEvent event2) {
        double rectPosX1 = tileSize;
        double posX1 = event2.getSceneX();
        double movementX1 = posX1 - rectPosX1;
        return (int) (Math.ceil(movementX1 / 100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }

    private int getPosYFromEvent(MouseEvent event2) {
        double rectPosY1 = tileSize;
        double posY1 = event2.getSceneY();
        double movementY1 = posY1 - rectPosY1;
        return (int) (Math.ceil(movementY1 / 100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }


    public static void main(String[] args) {
        launch(args);
    }

}