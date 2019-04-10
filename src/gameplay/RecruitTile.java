package gameplay;

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
class RecruitTile extends StackPane{ //each tile is a stackpane with an encapsuled rectangle

  private final Recruit recruit;
  private final Rectangle rect;

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

  public Recruit getRecruit() { //possibly unnecessary
    return recruit;
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
