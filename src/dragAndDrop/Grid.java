package dragAndDrop;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;


public class Grid extends Rectangle {
    Tile[][]liste;
    GridPane gp = new GridPane();


    public Grid(int rows, int columns){
        this.liste = new Tile[rows][columns];
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                Tile tile2 = new Tile(100,100);
                tile2.setTranslateX(j *100);
                tile2.setTranslateY(i *100);
                this.liste[i][j] = tile2;
                gp.getChildren().add(tile2);
            }
        }
    }





}
