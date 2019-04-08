package gameplay;


import database.Variables;
import database.Variables;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


/**
 * Recruit is a own lightweight unit class for avoiding conflicts with the unit class.
 * It is primarily used in the placement phase with DragPane in Game Main, to set up units in the sidebar of the game, where
 * the player can drag a recruit into the grid. Recruit is used in accordance with a Recruit tile.
 * @see javafx.scene.input.Dragboard
 * @see GameMain
 * @see Unit
 * @see RecruitTile
 */
public class Recruit extends StackPane {
  private Rectangle rect;
  private UnitType type;
  private int costLabelHeight = 10;
  private int costPosY = (int)((Variables.tileSize-costLabelHeight) / 2);

  /**
   * Sets up recruit with a UnitType, and also set images and styles of Recruit.
   * It also adds a cost label to each recruit, to show how much it costs do buy.
   * @param type Takes a UnitType class.
   */
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
    costLabel.setMinHeight(costLabelHeight);
    costLabel.setAlignment(Pos.CENTER);
    costLabel.setTranslateY(costPosY);
    costLabel.setStyle("-fx-background-color: Blue;" + "-fx-text-fill: White;");

  }

  /**
   * Returns a String of which UnitType the recruit is.
   * @return String Returns the UnitType in a String.
   */
  public String getType() {
    return type.getType();
  }

  /**
   * Returns the image associated with the recruit.
   * @return Image Returns the Image associated with the recruit.
   */
  public Image getImage() {
    return type.getUnitImage();
  }

  /**
   * Returns the attributes like health, movement range, attack, description tag, and description in a single String.
   * Very similar to a toString method(). It is used in the sidepanel to show recruit information on mouse click.
   * @return String Returns the the attributes of a unit, and also it's description. Similar to toString().
   * @see Unit
   */
  public String getDescription(){

    return type.getDescriptionTag() + "\n" +
            "\nHp: " + type.getHp() +
            "\nMovement Range: " + type.getMovementRange() +
            "\nAttack: " + type.getAttack() + "x\n" + "\n" + type.getDescription();
  }
}
