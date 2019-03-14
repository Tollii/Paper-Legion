package dragAndDrop;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class Piece extends Rectangle {
    GridPane a = new GridPane();
    private double oldPosX;
    private double oldPosY;
    private double hp;
    private Label healthbar;
    private boolean enemy;
    private int maxMovement=2;

    public Piece(double width, double height, double row, double column, double hp, boolean enemy){
        super.setWidth(width);
        super.setHeight(height);
        this.enemy = enemy;


        setPosition(column*100,row*100);
        this.hp = hp;

        String hpText = String.valueOf(hp);
        healthbar = new Label(hpText);
        healthbar.setTranslateX(column*100);
        healthbar.setTranslateY(row*100);
        healthbar.setTextFill(Color.RED);
        healthbar.setTextAlignment(TextAlignment.CENTER);

        healthbar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        healthbar.setAlignment(Pos.CENTER);
        healthbar.setPrefWidth(100);
        StackPane test = new StackPane();

        test.getChildren().addAll(this);
        Image archer = new Image("/dragAndDrop/assets/archer.png");
        this.setFill(new ImagePattern(archer));

        a.getChildren().addAll(test, healthbar);
    }

    public void setOldPos(double oldPosX, double oldPosY){
        this.oldPosX = oldPosX;
        this.oldPosY = oldPosY;
    }

    public double getOldPosX(){
        return oldPosX;
    }

    public double getOldPosY(){
        return oldPosY;
    }

    public void setPosition(double x, double y){
        super.setTranslateX(x);
        super.setTranslateY(y);
    }

    public boolean getEnemy(){
        return enemy;
    }

    public void takeDamage(){
        this.hp -=20;
        healthbar.setText(String.valueOf(hp));
    }

    public double getHp(){
        return hp;
    }

    public int getMaxMoveMent() {
        return maxMovement;
    }

//

}
