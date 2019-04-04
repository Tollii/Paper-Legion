package backup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


public class Tile extends Rectangle {
  // Vet ikke om disse attributtene er nødvendige.
  double oldPosX;
  double oldPosY;


  public Tile(int sizeX, int sizeY){
    super(sizeX,sizeY,Color.WHITE);
    super.setStrokeType(StrokeType.INSIDE);
    super.setStroke(Color.BLACK);
    //Image tile = new Image("/gameplay/assets/tile.jpg");
    //this.setFill(new ImagePattern(tile));
  }

  public void setPos(double posX, double posY){
    this.oldPosX = posX;
    this.oldPosY = posY;
  }
}
//
