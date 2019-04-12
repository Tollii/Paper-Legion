package gameplay.gameboard;


import gameplay.GameMain;
import gameplay.units.Unit;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.layout.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Paint;

import static database.Variables.*;

/**
 * Creates a Tile to be put inside a Grid. The tile can hold units or obstacles.
 * Tile is set up with a position in Grid, and has a static position. Each tile has a Rectangle
 * that can set a Image if there is a unit or obstacle in it. Tile extends to a StackPane, which
 * will center the tile in a fixed position and works in accordance with Grid.
 * @see Grid
 * @see Unit
 * @see Obstacle
 * @see StackPane
 */
public class Tile extends StackPane { //each tile is a stackpane containing a rectangle

    private Unit unit;
    private Obstacle obstacle;
    private final Rectangle rect;
    private boolean isTargetable = true;

    /**
     * Sets the size and shape of a tile, and places a image in the tile for use with the Placement phase,
     * it uses DragBoard for this. It also sets stroke size, type, width and color the tile.
     * @param sizeX Takes a integer to set the tiles length.
     * @param sizeY Takes a integer to set the tiles height.
     * @see Dragboard
     * @see Unit
     * @see Obstacle
     */
    public Tile(int sizeX, int sizeY) {
        rect = new Rectangle(sizeX, sizeY, Color.WHITE);
        rect.setStrokeType(standardStrokePlacement);
        rect.setStroke(standardStrokeColor);
        rect.setStrokeWidth(standardStrokeWidth);

        this.getChildren().add(rect);

        //detects a dragevent over the tile, and readies the tile for accepting a copy transfer
        this.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();

            if (event.getGestureSource() != this && dragboard.hasImage() && dragboard.hasString() && isTargetable) {
                event.acceptTransferModes(TransferMode.COPY);
            }
        });

        //detects when a dragevent is dropped on the tile
        this.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();

            boolean success = false;

            if (dragboard.hasImage() && dragboard.hasString()) {
                Unit newUnit = unitGenerator.newFriendlyUnit(dragboard.getString()); //creates new friendly unit and adds to tile

                if(currentResources - newUnit.getCost() >= 0){
                    setUnit(newUnit);
                    setUntargetable();
                    GameMain.removePlaceUnitLabel();
                    success = true;
                }
            }

            if(success){

                currentResources -= getUnit().getCost();
                GameMain.updateResourceLabel();
            }

            event.setDropCompleted(success);
        });
    }

    /**
     * Sets a unit in the tile, it is used if a player moved the unit on top of the tile.
     * @param unit Takes in a unit to be placed on the tile.
     * @see Unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
        this.getChildren().add(unit);
    }

    /**
     * Returns a Unit if it is set. Else it returns null. It is used to get information about
     * whether the Tile holds a Unit or not.
     * @return Unit Returns a unit, if a unit is set on the tile, else returns null.
     * @see Unit
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Removes a unit from the tile's StackPane and sets the unit=null;
     * @see Unit
     * @see StackPane
     */
    public void removeUnit() {
        this.getChildren().remove(unit);
        unit = null;
    }


    /**
     * Returns a Obstacle if it is set. Else it returns null. It is used to get information about
     * whether the Tile holds a Obstacle or not.
     * @return Obstacle Returns the obstacle if set, else returns null;
     * @see Obstacle
     */
    public Obstacle getObstacle() {
        return obstacle;
    }

    /**
     * Sets a Obstacle in the tile.
     * @param obstacle Takes in a obstacle to be placed on the tile.
     * @see Obstacle
     */
    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
        this.getChildren().add(obstacle);
    }

    /**
     * Is used to set any tile untargetable for Dragboard either when a unit is already placed on the tile,
     * or if the tile is in the non-targetable zone at the start of the placement phase
     * @see Dragboard
     * @see GameMain
     */
    public void setUntargetable() {
        if (isTargetable) {
            isTargetable = false;
        }
    }

    /**
    * Is used to set tile targetable again if the unit is sold during the placementphase.
    * @see GameMain
    */
    public void setTargetable() {
      if (!isTargetable) {
        isTargetable = true;
      }
    }

    /**
     * Sets the fill color for the tile.
     * @param color Takes Paint class as parameter for which color to paint tile.
     * @see Rectangle
     * @see Paint
     */
    public void setFill(Paint color) {
        rect.setFill(color);
    }


    /**
     * Sets the stroke color for the tile.
     * @param color Takes Paint class as parameter for which color to have as stroke around Tile.
     * @see Rectangle
     * @see Paint
     */
    public void setStroke(Paint color) {
        rect.setStroke(color);
    }

    /**
     * Sets the stroke type for the tile.
     * @param value Takes StrokeType class as parameter for which type of stroke to have around Tile.
     * @see Rectangle
     * @see StrokeType
     */
    public void setStrokeType(StrokeType value) {
        rect.setStrokeType(value);
    }

    /**
     * Sets the stroke width for the tile.
     * @param value Takes a double value for the stroke width.
     * @see Rectangle
     * @see java.awt.Stroke
     */
    public void setStrokeWidth(double value) {
        rect.setStrokeWidth(value);
    }
}
