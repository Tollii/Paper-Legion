package gameplay;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


//own lightweight unit class for avoiding conflicts with the unit class
public class Recruit extends StackPane {
  private Rectangle rect;
  private UnitType type;

  public Recruit(UnitType type) {
    rect = new Rectangle();
    this.type = type;

    rect.setWidth(GameLogic.tileSize);
    rect.setHeight(GameLogic.tileSize);
    rect.setFill(new ImagePattern(getImage()));
    this.getChildren().add(rect);
  }

  public String getType() {
    return type.getType();
  }

  public Image getImage() {
    return type.getUnitImage();
  }
}
