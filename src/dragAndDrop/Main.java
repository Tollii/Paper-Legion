
package dragAndDrop;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    static int draggedTilePosX;
    static int draggedTilePosY;
    static boolean selected=false;
    GridPane ins = new GridPane(); // For alle tiles.
    static Grid testGrid = new Grid(6,6); //Sets up Grid 6x6
    private MouseEvent event9;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage window = primaryStage; // Program window
        Scene scene1;                //Scene for second and third phase of the game
        StackPane sp = new StackPane();
        sp.setAlignment(Pos.BASELINE_LEFT); //Only baseline_Left is correct according to positions.

        ins.getChildren().add(testGrid.gp); //Insert grid from Grid class.
        scene1 = new Scene(sp, 800,600);

        //Bruker denne som spiller brikke foreløpig.
        //Piece tile = new Piece(100,100, 0,0,100,false);

        //////////////Add enemy in array and place them on board ///////////////
        piecesListe[0][1] = new Piece(tileSize,tileSize,0,1,100, true);
        piecesListe[0][2] = new Piece(tileSize,tileSize,0,2,60, true);
        piecesListe[1][4] = new Piece(tileSize,tileSize,1,4,100,false);


        //Når musen klikkes utforbi spillerbrikken endres fargen tilbake til normalt.
        ins.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            for (int i=0; i<piecesListe.length; i++){
                for(int j=0; j<piecesListe[i].length; j++){
                    if(piecesListe[i][j]!= null){
                        piecesListe[i][j].setStroke(Color.TRANSPARENT);

                        //testGrid.liste[i][j].setFill(Color.TRANSPARENT);
                    }
                }
            }
            selected = false;
        });
//
        //Legger alle tiles til i stackpane som blir lagt til scenen.
        sp.getChildren().add(ins);

        //Legger alle brikkene inn på brettet.
        for(int i=0; i<piecesListe.length; i++){
            for(int j=0; j<piecesListe[i].length; j++){
                if(piecesListe[i][j] != null){
                    sp.getChildren().add(piecesListe[i][j]);
                }

            }
        }

        //////////////////////////////////////////////////////////////////////////////ANGRIP/////////////////////////////////////////////////

        //Velger hvilken brikke du vil bruke.
        scene1.addEventHandler(MouseEvent.MOUSE_CLICKED, event2 -> {
            int posX = getPosXFromEvent(event2);
            int posY = getPosYFromEvent(event2);


            if(piecesListe[posY][posX] != null) {
                if (!selected) {
                    piecesListe[posY][posX].setOldPos(piecesListe[posY][posX].getTranslateX()/100, piecesListe[posY][posX].getTranslateY()/100);
                    piecesListe[posY][posX].setStrokeType(StrokeType.INSIDE);
                    piecesListe[posY][posX].setStrokeWidth(3);
                    piecesListe[posY][posX].setStroke(Color.RED);
                    selected = true;
                    selectedPosX = posX;
                    selectedPosY = posY;
                    System.out.println(posX + " : " + posY);
                }
            }

            if (selected) {
                ifDragged(selectedPosX, selectedPosY);
            }

        });


        //Når du har valgt en brikke, så kan du angripe en annen med denne.
        sp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(selected){
                int nyPosX = getPosXFromEvent(event);
                int nyPosY = getPosYFromEvent(event);
                if (piecesListe[nyPosY][nyPosX] != null) {
                    if (piecesListe[nyPosY][nyPosX].getEnemy()) {
                        if(piecesListe[nyPosY][nyPosX] != piecesListe[selectedPosY][selectedPosX]){
                            if (selected) {
                                if(withinBounds(nyPosX,nyPosY)) {
                                    piecesListe[nyPosY][nyPosX].takeDamage();
                                    System.out.println(piecesListe[nyPosY][nyPosX].getHp());
                                    if (piecesListe[nyPosY][nyPosX].getHp() <= 0) {
                                        sp.getChildren().removeAll(piecesListe[nyPosY][nyPosX]);
                                        piecesListe[nyPosY][nyPosX] = null;
                                        event = null;
                                        // må og fjernes fra eventuelle lister denne fienden kan ligge i.
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        ////////////////////////////////////////////////////////ANGRIP SLUTT/////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////BEVEGELSE//////////////////////////////////////////////////////////////




//            piecesListe[][].addEventHandler(MouseEvent.MOUSE_RELEASED, e ->{
//                //Hvis avstanden er 2 eller mindre
//                if(!(Math.abs(a/100-piecesListe[(int)b1][(int)a1].getOldPosX())>2) && (!(Math.abs(b/100-piecesListe[(int)b1][(int)a1].getOldPosY())>2))){
//
//                        if(piecesListe[][] == null){
//                            piecesListe[][].setTranslateX(a);
//                            piecesListe[][].setTranslateY(b);
//
//                            piecesListe[][] = piecesListe[][];
//                            piecesListe[][] = null;
//                        }
//                    }
//                    //Hvis avstanden er stører enn 2, flyttes ikke brikken.
//                } else{
//                    piecesListe[][].setTranslateX(piecesListe[][].getOldPosX());
//                    piecesListe[][].setTranslateY(piecesListe[][].getOldPosY());
//                }
//            });







        /////////////////////////////////////////BEVEGELSE SLUTT///////////////////////////////////////////////////////


        window.setTitle("Hello World");
        window.setScene(scene1);
        window.show();
    }

    private void ifDragged(int startPosX, int startPosY) {
        if(selected) {
            piecesListe[startPosY][startPosX].setOldPos(startPosX, startPosY);

            piecesListe[startPosY][startPosX].addEventHandler(MouseEvent.MOUSE_DRAGGED, event1 -> {
                double precPosX = getPrecPosXFromEvent(event1);
                double precPosY = getPrecPosYFromEvent(event1);
                piecesListe[startPosY][startPosX].setTranslateX(precPosX);
                piecesListe[startPosY][startPosX].setTranslateY(precPosY);


            });
        }


            piecesListe[startPosY][startPosX].addEventHandler(MouseEvent.MOUSE_RELEASED, event2 -> {
                int dropPosX = getPosXFromEvent(event2);
                int dropPosY = getPosYFromEvent(event2);

                if(piecesListe[dropPosY][dropPosX] == null) {
                    if(withinBounds(dropPosX, dropPosY)){
                        if (dropPosX >= 0 && dropPosX < testGrid.getColumns() && dropPosY >= 0 && dropPosY < testGrid.getRows()) {
                            piecesListe[startPosY][startPosX].setTranslateX(dropPosX * 100);
                            piecesListe[startPosY][startPosX].setTranslateY(dropPosY * 100);

                            piecesListe[dropPosY][dropPosX] = piecesListe[startPosY][startPosX];
                            piecesListe[dropPosY][dropPosX].setOldPos(dropPosX, dropPosY);
////                                selectedPosX = dropPosX;
////                                selectedPosY = dropPosY;
                            selected  = false;

                           // piecesListe[selectedPosY][selectedPosX] = null;
                            selectedPosX = dropPosX;
                            selectedPosY = dropPosY;


                            for(int i=0; i<piecesListe.length; i++){
                                for(int j=0; j<piecesListe[i].length; j++){
                                    System.out.println(piecesListe[i][j]);
                                }
                            }
                        }
                    }


                }  else {
                    piecesListe[startPosY][startPosX].setTranslateX(piecesListe[startPosY][startPosX].getOldPosX() * 100);
                    piecesListe[startPosY][startPosX].setTranslateY(piecesListe[startPosY][startPosX].getOldPosY() * 100);

                }
            });




    }

    private void highlightPossibleMoves(int posX, int posY) {

//        System.out.println("PosX+1: " +(posX+1));
//        System.out.println("PosX-1: " +(posX-1));
//        System.out.println("PosY+1: " +(posY+1));
//        System.out.println("PosY-1: " +(posY-1));
//
//        if((posX-1)>=0 && (posX+1)>=0 && (posX-1)<=6 && (posX+1<=6)) {
//            //UP DOWN LEFT RIGHT
//            testGrid.liste[posY][posX - 1].setFill(Color.DARKRED);
//            testGrid.liste[posY][posX + 1].setFill(Color.DARKRED);
//            testGrid.liste[posY - 1][posX].setFill(Color.DARKRED);
//            testGrid.liste[posY + 1][posX].setFill(Color.DARKRED);
//
//
//            //Corners
//            testGrid.liste[posY + 1][posX + 1].setFill(Color.DARKRED);
//            testGrid.liste[posY - 1][posX - 1].setFill(Color.DARKRED);
//            testGrid.liste[posY + 1][posX - 1].setFill(Color.DARKRED);
//            testGrid.liste[posY - 1][posX + 1].setFill(Color.DARKRED);
//
//        }


    }


    private void clearHighlightPossible(int posX, int posY) {
//        //UP DOWN LEFT RIGHT
//        testGrid.liste[posY][posX-1].setFill(Color.TRANSPARENT);
//        testGrid.liste[posY][posX+1].setFill(Color.TRANSPARENT);
//        testGrid.liste[posY-1][posX].setFill(Color.TRANSPARENT);
//        testGrid.liste[posY+1][posX].setFill(Color.TRANSPARENT);
//
//        //Corners
//        testGrid.liste[posY+1][posX+1].setFill(Color.TRANSPARENT);
//        testGrid.liste[posY-1][posX-1].setFill(Color.TRANSPARENT);
//        testGrid.liste[posY+1][posX-1].setFill(Color.TRANSPARENT);
//        testGrid.liste[posY-1][posX+1].setFill(Color.TRANSPARENT);
    }

    private boolean withinBounds(int nyPosX, int nyPosY) {
        if(!(Math.abs(nyPosX-piecesListe[selectedPosY][selectedPosX].getOldPosX())>2) && (!(Math.abs(nyPosY-piecesListe[selectedPosY][selectedPosX].getOldPosY())>2))){
            return true;
        }
        return false;
    }

    private double getPrecPosXFromEvent(MouseEvent event2) {
        double rectPosX1 = tileSize/2;
        double posX1 = event2.getSceneX();
        return (posX1-rectPosX1);
    }

    private double getPrecPosYFromEvent(MouseEvent event2) {
        double rectPosY1 = tileSize/2;
        double posY1 = event2.getSceneY();
        return (posY1-rectPosY1);
    }

    private int getPosXFromEvent(MouseEvent event2) {
        double rectPosX1 = tileSize;
        double posX1 = event2.getSceneX();
        double movementX1 = posX1-rectPosX1;
        return (int) (Math.ceil(movementX1/100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }

    private int getPosYFromEvent(MouseEvent event2) {
        double rectPosY1 = tileSize;
        double posY1 =event2.getSceneY();
        double movementY1 = posY1-rectPosY1;
        return (int) (Math.ceil(movementY1/100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }


    public static void main(String[] args) {
        launch(args);
    }
}
