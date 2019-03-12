
package dragAndDrop;

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
    private static final int tileSize = 100;
    static int selectedPosX;
    static int selectedPosY;
    static boolean selected=false;


    //Har alle tiles i seg.
    GridPane ins = new GridPane();





    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage window = primaryStage;
        Scene scene1;
        StackPane sp = new StackPane();
        sp.setAlignment(Pos.BASELINE_LEFT);
        Grid testGrid = new Grid(6,6);
        ins.getChildren().add(testGrid.gp);
        scene1 = new Scene(sp, 500,400);

        //Bruker denne som spiller brikke foreløpig.
        //Piece tile = new Piece(100,100, 0,0,100,false);

        //////////////Add enemy in array and place them on board ///////////////
        piecesListe[0][1] = new Piece(100,100,0,1,100, true);
        piecesListe[0][2] = new Piece(100,100,0,2,60, true);
        piecesListe[1][4] = new Piece(100,100,1,4,100,false);



        //Når musen klikkes utforbi spillerbrikken endres fargen tilbake til normalt.
        ins.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            for (int i=0; i<piecesListe.length; i++){
                for(int j=0; j<piecesListe[i].length; j++){
                    if(piecesListe[i][j]!= null){
                        piecesListe[i][j].setStroke(Color.TRANSPARENT);
                        scene1.setOnMouseClicked(null);
                        sp.setOnMouseClicked(null);
                    }
                }
            }
            selected = false;
        });

        //Legger alle tiles til i stackpane.
        sp.getChildren().add(ins);


        for(int i=0; i<piecesListe.length; i++){
            for(int j=0; j<piecesListe[i].length; j++){
                if(piecesListe[i][j] != null){
                    sp.getChildren().add(piecesListe[i][j]);
                }

            }
        }

        //////////////////////////////////////////////////////////////////////////////ANGRIP/////////////////////////////////////////////////

        scene1.addEventHandler(MouseEvent.MOUSE_CLICKED, event2 -> {
            int posX = getPosXFromEvent(event2);
            int posY = getPosYromEvent(event2);

            if(piecesListe[posY][posX] != null) {
                if (!selected) {
                    piecesListe[posY][posX].setOldPos(piecesListe[posY][posX].getTranslateX(), piecesListe[posY][posX].getTranslateY());
                    piecesListe[posY][posX].setStrokeType(StrokeType.INSIDE);
                    piecesListe[posY][posX].setStrokeWidth(3);
                    piecesListe[posY][posX].setStroke(Color.RED);
                    selected = true;
                    selectedPosX = posX;
                    selectedPosY = posY;
                }
            }
        });

        sp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(selected){
                int nyPosX = getPosXFromEvent(event);
                int nyPosY = getPosYromEvent(event);
                if (piecesListe[nyPosY][nyPosX] != null) {
                    if (piecesListe[nyPosY][nyPosX].getEnemy()) {
                        if(piecesListe[nyPosY][nyPosX] != piecesListe[selectedPosY][selectedPosX]){
                            if (selected) {
                                System.out.println("attack");
                                piecesListe[nyPosY][nyPosX].takeDamage();
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
        });

        ////////////////////////////////////////////////////////ANGRIP SLUTT/////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////BEVEGELSE//////////////////////////////////////////////////////////////
//        sp.addEventHandler(MouseEvent.MOUSE_CLICKED,event2 -> {
//            double rectPosX1 = tileSize;
//            double rectPosY1 = tileSize;
//            double posX1 = event2.getSceneX();
//            double posY1 =event2.getSceneY();
//            double movementX1 = posX1-rectPosX1;
//            double movementY1 = posY1-rectPosY1;
//            double a1 = (int) (Math.ceil(movementX1/100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
//            double b1 = (int) (Math.ceil(movementY1/100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
//
//
//            piecesListe[(int)b1][(int)a1].addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
//                double rectPosX = piecesListe[(int)b1][(int)a1].getLayoutX() +tileSize;
//                double rectPosY = piecesListe[(int)b1][(int)a1].getLayoutY() +tileSize;
//                double precRectPosX = tileSize/2;
//                double precRectPosY = tileSize/2;
//                double posX = event.getSceneX();
//                double posY =event.getSceneY();
//                double movementX = posX-rectPosX;
//                double movementY = posY-rectPosY;
//                double movementPrecX = posX-precRectPosX;
//                double movementPrecY = posY-precRectPosY;
//
//                double a = (int) (Math.ceil(movementX/100.0))*100; // Runder til nærmeste 100 for snap to grid funksjonalitet
//                double b = (int) (Math.ceil(movementY/100.0))*100; // Runder til nærmeste 100 for snap to grid funksjonalitet
//
//                piecesListe[(int)b1][(int)a1].setTranslateX(movementPrecX);
//                piecesListe[(int)b1][(int)a1].setTranslateY(movementPrecY);
//
//                piecesListe[(int)b1][(int)a1].addEventHandler(MouseEvent.MOUSE_RELEASED, e ->{
//                    //Hvis avstanden er 2 eller mindre
//                    if(!(Math.abs(a/100-oldPosX/100)>2) && (!(Math.abs(b/100-oldPosY/100)>2))){
//                        if(a/100 >=0 && a/100<testGrid.getColumns() && b/100 >=0 && b/100<testGrid.getRows()){
//                            if(piecesListe[(int) b/100][(int)a/100] == null){
//                                piecesListe[(int)b1][(int)a1].setTranslateX(a);
//                                piecesListe[(int)b1][(int)a1].setTranslateY(b);
//
//                                piecesListe[(int)b][(int)a] = piecesListe[(int)b1][(int)a1];
//                                piecesListe[(int)b1][(int)a1]= null;
//                            }
//                        }
//                        //Hvis avstanden er stører enn 2, flyttes ikke brikken.
//                    } else{
//                        piecesListe[(int)b1][(int)a1].setTranslateX(piecesListe[(int)b1][(int)a1].getOldPosX());
//                        piecesListe[(int)b1][(int)a1].setTranslateY(piecesListe[(int)b1][(int)a1].getOldPosY());
//                    }
//                });
//            });
//
//
//
//
//
//
//        });


        /////////////////////////////////////////BEVEGELSE SLUTT///////////////////////////////////////////////////////







        window.setTitle("Hello World");
        window.setScene(scene1);
        window.show();
    }

    private int getPosXFromEvent(MouseEvent event2) {
        double rectPosX1 = tileSize;
        double posX1 = event2.getSceneX();
        double movementX1 = posX1-rectPosX1;
        return (int) (Math.ceil(movementX1/100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }

    private int getPosYromEvent(MouseEvent event2) {
        double rectPosY1 = tileSize;
        double posY1 =event2.getSceneY();
        double movementY1 = posY1-rectPosY1;
        return (int) (Math.ceil(movementY1/100.0)); // Runder til nærmeste 100 for snap to grid funksjonalitet
    }


    public static void main(String[] args) {
        launch(args);
    }
}
