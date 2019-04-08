package backup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


class Tile extends Rectangle {


    public Tile(int sizeX, int sizeY){
    super(sizeX,sizeY,Color.WHITE);
    super.setStrokeType(StrokeType.INSIDE);
    super.setStroke(Color.BLACK);
    //Image tile = new Image("/gameplay/assets/tile.jpg");
    //this.setFill(new ImagePattern(tile));
  }

  public void setPos(double posX, double posY){
      // Vet ikke om disse attributtene er nødvendige.
      double oldPosX = posX;
      double oldPosY = posY;
  }
}
//
