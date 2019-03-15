package dragAndDrop;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


public class Tile extends Rectangle {
    // Vet ikke om disse attributtene er nødvendige.
    double oldPosX;
    double oldPosY;
    boolean pieceOnTile;

    public Tile(int sizeX, int sizeY){
        super(sizeX,sizeY,Color.WHITE);
        super.setStrokeType(StrokeType.INSIDE);
        super.setStroke(Color.BLACK);
        //Image tile = new Image("/dragAndDrop/assets/tile.jpg");
        //this.setFill(new ImagePattern(tile));

    }




    // vet ikke om disse metodene er nødvendige.
    public void setPieceOnTile(boolean piece){
        this.pieceOnTile = piece;
    }

    public boolean getPieceOnTile(){
        return pieceOnTile;
    }

    public void setPos(double posX, double posY){
        this.oldPosX = posX;
        this.oldPosY = posY;
    }
}
//

