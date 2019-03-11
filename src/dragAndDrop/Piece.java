package dragAndDrop;

import javafx.scene.shape.Rectangle;

public class Piece extends Rectangle {
    private double oldPosX; // Nødvendig?
    private double oldPosY; // Nødvendig?
    //private Rectangle piece;

    public Piece(double width, double height){
        super.setWidth(width);
        super.setHeight(height);
        //this.piece = new Rectangle(width, height);
        super.setTranslateY(200);

    }

}
