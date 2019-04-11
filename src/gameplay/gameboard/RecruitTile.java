package gameplay.gameboard;

import gameplay.GameMain;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.layout.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ClipboardContent;
import static database.Variables.*;
import javafx.scene.paint.Paint;

/**
* Own lightweight version of Tile for use in placementphase for holding recruits. Has some different methods,
* and lacks a lot of unnecessary attributes and methods from Tile. Extends StackPane to stack multiple
* nodes in one place: holding a rectangle as a background with a recruit on top, and a cost label over that again.
* @see Tile
* @see Recruit
* @see StackPane
*/
public class RecruitTile extends StackPane{ //each tile is a stackpane with an encapsuled rectangle

  private final Recruit recruit;
  private final Rectangle rect;

  /**
   * Sets a recruit in the RecruitTile class, and creates a rectangle with the sizeX, and sizeY
   * an adds both recruit and rectangle as children to the stackpane. When the RecruitTile is clicked on
   * with a mouseclick, it changes the description in the sidebar to the recruit's description and
   * sets the description visible. Then it deselects the other recruits, and stylize its own Tile with a
   * red stroke. When Recruit is dragged with MouseEvent, the recruit is dragged and copied onto a tile if
   * it is a valid location. This is done using Dragboard.
   * @param sizeX Takes in the width of the RecruitTile
   * @param sizeY Takes in the lenght of the RecruitTile
   * @param recruit Takes in a Recruit
   * @see Recruit
   * @see GameMain
   */
  public RecruitTile(int sizeX, int sizeY, Recruit recruit) {
    this.recruit = recruit;

    rect = new Rectangle(sizeX, sizeY, Color.WHITE);
    rect.setStrokeType(StrokeType.INSIDE);
    rect.setStroke(Color.BLACK);

    this.getChildren().add(rect);
    this.getChildren().add(this.recruit);

    this.setOnMouseClicked(event -> {
      GameMain.changeDescriptionLabel(recruit.getDescription(), recruit.getDescriptionHead());
      GameMain.descriptionVisible(true);

      GameMain.deselectRecruitTiles();

      setStroke(selectionOutlineColor);
      setStrokeWidth(selectedStrokeWidth);
    });

    //starts a dragevent with an image for appearances and an identifying string for identifying unit type
    this.setOnDragDetected(event -> {
      Dragboard dragboard = this.startDragAndDrop(TransferMode.COPY);

      ClipboardContent content = new ClipboardContent();

      content.putImage(recruit.getImage());
      content.putString(recruit.getType());

      dragboard.setContent(content);

      GameMain.hasPlacedAUnit = true;
    });
  }

  /**
   * Returns a Recruit
   * @return Recruit
   * @see Recruit
   */
  public Recruit getRecruit() { //possibly unnecessary
    return recruit;
  }

  /**
   * Sets the stroke color around the RecruitTile
   * @param color Takes in a Paint object
   * @see Paint
   */
  public void setStroke(Paint color) {
      rect.setStroke(color);
  }

  /**
   * Sets the stroketype to be around the rectangle
   * @param value Takes in a object of the class StrokeType
   * @see StrokeType
   * @see Rectangle
   */
  public void setStrokeType(StrokeType value) {
      rect.setStrokeType(value);
  }

  /**
   * Sets the strokewidth of the Rectangle
   * @param value Takes in a double value.
   * @see Rectangle
   */
  public void setStrokeWidth(double value) {
      rect.setStrokeWidth(value);
  }

}
