package gameplay;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Obstacle extends Rectangle {
    private int posX;
    private int posY;
    private int obstacleID;
    private Image obstacle = new Image("/gameplay/assets/obstacle.png");


    public Obstacle(int posX, int posY, int obstacleID){
        super(GameLogic.tileSize,GameLogic.tileSize);
        this.posX = posX;
        this.posY = posY;
        this.obstacleID = obstacleID;
        this.setFill(new ImagePattern(obstacle));
        setTranslate();
    }

    public void setTranslate(){
        super.setTranslateX(posX*100);
        super.setTranslateY(posY*100);
    }


    public int getPosX() { return posX; }

    public int getPosY() { return posY; }
}
