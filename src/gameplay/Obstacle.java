package gameplay;

import database.Variables;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import menus.Controller.MatchSetupController;

import static database.Variables.testing;

/**
 * Creates a Obstacle, very similar to a unit, but the obstacle has a fixed position,
 * and cannot be damaged or moved by any player. It's purpose is to shape the dynamic of
 * the game play. It holds the variables: posX, posY, obstacleID, Rectangle, and a image.
 * @see Rectangle
 * @see Image
 */
public class Obstacle extends StackPane {
    private int posX;
    private int posY;
    private int obstacleID;
    private Rectangle rect;
    private Image obstacleImg;

    /**
     * Sets the variables posX, posY, and obstacleID for the object.
     * Also sets up a image to fill a rectangle which is used with StackPane to show the Obstacle.
     * @param posX Takes in the x-position for the obstacle
     * @param posY Takes in the y-position for the obstacle
     * @param obstacleID Takes in a unique id for the obstacle.
     * @see Rectangle
     * @see Image
     * @see StackPane
     */
    public Obstacle(int posX, int posY, int obstacleID){

        rect = new Rectangle();
        rect.setWidth(Variables.tileSize);
        rect.setHeight(Variables.tileSize);
        this.posX = posX;
        this.posY = posY;
        this.obstacleID = obstacleID;
        if(!testing){
            obstacleImg = new Image("/gameplay/assets/obstacle.png");
            rect.setFill(new ImagePattern(obstacleImg));
            this.getChildren().add(rect);
        }
    }

    /**
     * Sets the positon of the Obstacle in the form of graph coordinates.
     * The grid coordinates are single digits, but is transformed into graph coordinates.
     */
    public void setTranslate(){
        super.setTranslateX(posX*100);
        super.setTranslateY(posY*100);
    }

    /**
     * Returns a Integer with the X-position in relation to the game Grid.
     * @return int
     * @see Grid
     */
    public int getPosX() { return posX; }

    /**
     * Returns a Integer with the Y-position in relation to the game Grid.
     * @return int
     * @see Grid
     */
    public int getPosY() { return posY; }
}
