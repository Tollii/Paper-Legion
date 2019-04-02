package gameplay;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Paint;

public class Tile extends StackPane { //each tile is a stackpane containing a rectangle

  private Unit unit;
  private Rectangle rect;
  private boolean isTargetable = true;

    public Tile(int sizeX, int sizeY){
      rect = new Rectangle(sizeX, sizeY, Color.WHITE);
      rect.setStrokeType(StrokeType.INSIDE);
      rect.setStroke(Color.BLACK);

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
          Unit newUnit = SetUp.unitGenerator.newFriendlyUnit(dragboard.getString()); //creates new friendly unit and adds to tile

          setUnit(newUnit);
          setUntargetable();
          success = true;
        }
        event.setDropCompleted(success);
      });
    }

    //get and set methods
    public void setUnit(Unit unit) {
      this.unit = unit;
      this.getChildren().add(unit);
    }

    public Unit getUnit() {
      return unit;
    }

    //method for removing unit when moved or killed
    public void removeUnit() {
      unit = null;
      this.getChildren().remove(unit);
    }

    public void setUntargetable() {
      if (isTargetable) {
        isTargetable = false;
      }
    }

    public void setFill(Paint color) {
      rect.setFill(color);
    }

    public void setStroke(Paint color) {
      rect.setStroke(color);
    }

    public void setStrokeType(StrokeType value) {
      rect.setStrokeType(value);
    }

    public void setStrokeWidth(double value) {
      rect.setStrokeWidth(value);
    }
}
