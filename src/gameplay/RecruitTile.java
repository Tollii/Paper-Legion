package gameplay;

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
import javafx.scene.input.ClipboardContent;

//own tile class for unit selector to avoid conflicts with gridtile class
public class RecruitTile extends StackPane{ //each tile is a stackpane with an encapsuled rectangle

  private Recruit recruit;

  public RecruitTile(int sizeX, int sizeY, Recruit recruit) {
    this.recruit = recruit;

    Rectangle rect = new Rectangle(sizeX, sizeY, Color.WHITE);
    rect.setStrokeType(StrokeType.INSIDE);
    rect.setStroke(Color.BLACK);

    this.getChildren().add(rect);
    this.getChildren().add(this.recruit);

    //starts a dragevent with an image for appearances and an identifying string for identifying unit type
    this.setOnDragDetected(event -> {
      Dragboard dragboard = this.startDragAndDrop(TransferMode.COPY);

      ClipboardContent content = new ClipboardContent();

      content.putImage(recruit.getImage());
      content.putString(recruit.getType());

      dragboard.setContent(content);
    });
  }

  public Recruit getRecruit() { //possibly unnecessary
    return recruit;
  }
}
