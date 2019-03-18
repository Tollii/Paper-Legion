package dragAndDrop;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Grid extends Rectangle {
    Tile[][]liste;
    GridPane gp = new GridPane();
    int columns;
    int rows;


    public Grid(int rows, int columns){
        this.columns = columns;
        this.rows = rows;

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

    public int getColumns(){
        return columns;
    }

    public int getRows(){
        return rows;
    }

//


}
