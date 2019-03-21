package dragAndDrop;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.layout.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;

public class Tile extends StackPane { //hver rute er en stackpane omkapslet rundt et rektangel

  private Unit unit;

    public Tile(int sizeX, int sizeY){
      Rectangle rect = new Rectangle(sizeX, sizeY, Color.WHITE);
      rect.setStrokeType(StrokeType.INSIDE);
      rect.setStroke(Color.BLACK);

      this.getChildren().add(rect);

      //lagger merke til om en dragevent skjer over tilen, og klargjør tilen for en overføring i form av kopiering
      this.setOnDragOver(event -> {
        Dragboard db = event.getDragboard();

        if (event.getGestureSource() != this && db.hasImage() && db.hasString()) {
          event.acceptTransferModes(TransferMode.COPY);
        }
      });

      //event på når drageventen droppes over tilen
      this.setOnDragDropped(event -> {
        Dragboard db = event.getDragboard();

        boolean success = false;
        if (db.hasImage() && db.hasString()) {
          Unit newUnit = newUnit(false, db.getString()); //skaper ny unit og legger den på tilen
          setUnit(newUnit);
          success = true;
        }
        event.setDropCompleted(success);
      });
    }

    //get og set metoder
    public void setUnit(Unit unit) {
      this.unit = unit;
      this.getChildren().add(unit);
    }

    public Unit getUnit() {
      return unit;
    }

    //metode for å fjerne unit når den flyttes
    public void removeUnit() {
      unit = null;
      this.getChildren().remove(unit);
    }
}
