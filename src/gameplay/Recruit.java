package gameplay;


import database.Variables;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


//own lightweight unit class for avoiding conflicts with the unit class
public class Recruit extends StackPane {
  private Rectangle rect;
  private UnitType type;
  private int healthbarPosY = 40;


  public Recruit(UnitType type) {
    rect = new Rectangle();
    this.type = type;

    rect.setWidth(Variables.tileSize);
    rect.setHeight(Variables.tileSize);
    rect.setFill(new ImagePattern(getImage()));
    this.getChildren().add(rect);
    Label costLabel = new Label(String.valueOf(type.getCost()));
    this.getChildren().add(costLabel);

    // Styling of costLabel.
    costLabel.setPrefWidth(Variables.tileSize);
    costLabel.setAlignment(Pos.CENTER);
    costLabel.setTranslateY(healthbarPosY);
    costLabel.setStyle("-fx-background-color: Blue;" + "-fx-text-fill: White;");

  }

  public String getType() {
    return type.getType();
  }

  public Image getImage() {
    return type.getUnitImage();
  }

  public String getDescription(){

    return type.getDescriptionTag() + "\n" +
            "\nHp: " + type.getHp() +
            "\nMovement Range: " + type.getMovementRange() +
            "\nAttack: " + type.getAttack() + "x\n" + "\n" + type.getDescription();
  }
}
