package sample.Model;

import dragAndDrop.Unit;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Tile {
    Rectangle tile;
    double posX = tile.getX();
    double posY = tile.getY();
    double distanceFromPiece;
    boolean pieceOnTile;
    Unit currenPiece;

    public Tile(int sizeX, int sizeY){
        this.tile = new Rectangle(sizeX,sizeY);
        tile.setFill(Color.WHITE);
        tile.setStrokeType(StrokeType.OUTSIDE);
        tile.setStroke(Color.BLACK);
    }

    public void setTranslateX(double a){
        tile.setTranslateX(a);
    }

    public void setTranslateY(double a){
        tile.setTranslateY(a);
    }

    public Rectangle getRectangle(){
        return tile;
    }

    public void setDistanceFromPiece(double a){
        this.distanceFromPiece = a;
    }

    public void setPieceOnTile(boolean piece){
        this.pieceOnTile = piece;

    }
}
