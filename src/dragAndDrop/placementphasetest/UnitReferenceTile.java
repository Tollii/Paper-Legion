package dragAndDrop;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.layout.*;

public class UnitReferenceTile extends StackPane{ //hver rute er en stackpane omkapslet rundt et rektangel

  Unit unit;

  public UnitReferenceTile(int sizeX, int sizeY) {
    Rectangle rect = new Rectangle(sizeX, sizeY, Color.WHITE);
    rect.setStrokeType(StrokeType.INSIDE);
    rect.setStroke(Color.BLACK);

    this.getChildren().add(rect);
  }

  public void setUnit(Piece unit) {
    this.unit = unit;
    this.getChildren().add(unit);
  }

  public Unit getUnit() { //muligens unødvendig
    return unit;
  }
}
