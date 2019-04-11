package gameplay.gameboard;

import database.Variables;
import javafx.scene.layout.GridPane;

/**
 * Creates a visible grid with the number of rows and columns with the specified arguments
 */
public class Grid extends GridPane {
    public final Tile[][] tileList;
    private final int columns;
    private final int rows;

    /**
     * When Object of Grid is created, constructor creates a visible grid (rows X columns)
     * by adding tiles with position relative to tilesize, and adds them
     * to a gridpane. When Grid is called upon, it will render a Grid for you.
     *
     * @param  rows  the number of rows wanted in the grid
     * @param  columns the number of columns wanted in the grid
     */
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

    /**
     * Returns the number of columns in the grid.
     * @return int
     */
    public int getColumns(){
        return columns;
    }

    /**
     * Returns the number of rows in the grid.
     * @return int
     */
    public int getRows(){
        return rows;
    }
}
