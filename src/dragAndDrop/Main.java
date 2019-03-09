

import dragAndDrop.Tile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
    private static final int tileSize = 100;


    //Har alle tiles i seg.
    GridPane ins = new GridPane();


    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage window = primaryStage;
        Scene scene1;

        StackPane sp = new StackPane();
        sp.setAlignment(Pos.BASELINE_LEFT);
        //Bruker denne som spiller brikke foreløpig.
        Rectangle tile = new Rectangle(100,100);

        //Når musen klikkes utforbi spillerbrikken endres fargen tilbake til normalt.
        ins.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            tile.setStroke(Color.BLACK);
        });

        tile.setStrokeType(StrokeType.INSIDE);
        tile.setStrokeWidth(3);
        tile.setStroke(Color.BLACK);
        tile.setFill(Color.BLACK);

        //Lager gridmap med tiles.
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
               Tile tile2 = new Tile(100,100);
                tile2.setTranslateX(j *100);
                tile2.setTranslateY(i *100);
               // sp.getChildren().add(tile2.getRectangle());
                ins.getChildren().add(tile2.getRectangle());
                liste[i][j] = tile2;
            }
        }

        //Legger alle tiles til i stackpane.
        sp.getChildren().addAll(ins, tile);


            tile.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
                tile.setStroke(Color.RED);
            });
            
            //Mouse hovered over spillerbrikke så blir fargen på brikken rød.
            tile.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                tile.setFill(Color.RED);
            });

            //Når musen flyttes over grid og vekk fra spillerbrikke så blir orginalfarge satt tilbake igjen.
            ins.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                tile.setFill(Color.BLACK);
            });



            //Drar spillerbrikke når mus blir holdt inne. 
            tile.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {

                double rectPosX = tile.getLayoutX()+ tile.getWidth();
                double rectPosY = tile.getLayoutY() + tile.getHeight();
                double posX = event.getSceneX();
                double posY =event.getSceneY();
                double movementX = posX-rectPosX;
                double movementY = posY-rectPosY;
                double a = (int) (Math.ceil(movementX/100.0))*100; // Runder til nærmeste 100 for snap to grid funksjonalitet
                double b = (int) (Math.ceil(movementY/100.0))*100; // Runder til nærmeste 100 for snap to grid funksjonalitet

                tile.setTranslateX(a);
                tile.setTranslateY(b);

                //En måte å holde oversikt på posisjonen til spillerbrikken på. Oppgitt i 0,1, -- 1,2 osv. passer med array.
                System.out.println("PosX: " + (int)tile.getTranslateX()/100 + " PosY: " + (int) tile.getTranslateY()/100);

                //tile.setTranslateY(posX-rectPosX);
                //tile.setTranslateY(posY-rectPosY);
            });






        scene1 = new Scene(sp, 500,400);

        window.setTitle("Hello World");
        window.setScene(scene1);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
