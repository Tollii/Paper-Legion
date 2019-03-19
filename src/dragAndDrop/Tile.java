package dragAndDrop;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.layout.*;

public class Tile extends StackPane { //hver rute er en stackpane omkapslet rundt et rektangel

  Unit unit;

    public Tile(int sizeX, int sizeY){
      Rectangle rect = new Rectangle(sizeX, sizeY, Color.WHITE);
      rect.setStrokeType(StrokeType.INSIDE);
      rect.setStroke(Color.BLACK);

      this.getChildren().add(rect);
    }

    //get og set metoder
    public void setUnit(Piece unit) {
      this.unit = unit;
      this.getChildren().add(unit);
    }

    public Unit getUnit() {
      return unit;
    }

    //metode for å fjerne unit når den flyttes
    public void removeUnit() {
      this.getChildren().remove(unit);
    }
}
