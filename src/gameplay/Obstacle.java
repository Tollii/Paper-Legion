package gameplay;

import database.Variables;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Obstacle extends StackPane {
    private int posX;
    private int posY;
    private int obstacleID;
    private Rectangle rect;
    private Image obstacleImg = new Image("/gameplay/assets/obstacle.png");


    public Obstacle(int posX, int posY, int obstacleID){
        rect = new Rectangle();
        rect.setWidth(Variables.tileSize);
        rect.setHeight(Variables.tileSize);
        this.posX = posX;
        this.posY = posY;
        this.obstacleID = obstacleID;

        rect.setFill(new ImagePattern(obstacleImg));
        this.getChildren().add(rect);
    }

    public void setTranslate(){
        super.setTranslateX(posX*100);
        super.setTranslateY(posY*100);
    }

    public int getPosX() { return posX; }

    public int getPosY() { return posY; }
}
