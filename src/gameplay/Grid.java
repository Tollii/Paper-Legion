package gameplay;

import database.Variables;
import javafx.scene.layout.GridPane;

public class Grid extends GridPane {
    public Tile[][] tileList;
    int columns;
    int rows;


    public Grid(int rows, int columns){
        this.columns = columns;
        this.rows = rows;

        this.tileList = new Tile[rows][columns];
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                Tile tile2 = new Tile(Variables.tileSize, Variables.tileSize);
                tile2.setTranslateX(j * Variables.tileSize);
                tile2.setTranslateY(i * Variables.tileSize);
                this.tileList[i][j] = tile2;
                this.getChildren().add(tile2);
            }
        }
    }

    public int getColumns(){
        return columns;
    }

    public int getRows(){
        return rows;
    }
}
