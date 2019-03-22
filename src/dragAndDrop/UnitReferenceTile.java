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
import javafx.scene.input.ClipboardContent;

public class UnitReferenceTile extends StackPane{ //hver rute er en stackpane omkapslet rundt et rektangel

  private Unit unit;

  public UnitReferenceTile(int sizeX, int sizeY) {
    Rectangle rect = new Rectangle(sizeX, sizeY, Color.WHITE);
    rect.setStrokeType(StrokeType.INSIDE);
    rect.setStroke(Color.BLACK);

    this.getChildren().add(rect);

    //starter en dragevent der et dragboard med et bilde for utseende for dragboardet og en string som identifiserer uniten legges til eventen
    this.setOnDragDetected(event -> {
      Dragboard db = this.startDragAndDrop(TransferMode.COPY);

      ClipboardContent content = new ClipboardContent();

      //content.putImage(unit.getImage());
      content.putString(unit.getType());

      db.setContent(content);
    });
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
    this.getChildren().add(unit);
  }

  public Unit getUnit() { //muligens unødvendig
    return unit;
  }
}
