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
    private Label healthbar;
    private UnitType type;
    private double oldPosX;
    private double oldPosY;
    private double hp;
    private double damageMultiplier;
    private int range;
    private boolean enemy;

    public Piece(double row, double column, boolean enemy, UnitType type){
        super.setWidth(Main.tileSize);
        super.setHeight(Main.tileSize);
        this.enemy = enemy;
        this.type = type;
        this.damageMultiplier = type.getAttackMultiplier();
        this.hp = type.getHp();
        this.range = type.getRange();

        setPosition(column*Main.tileSize,row*Main.tileSize);
        String hpText = String.valueOf(hp);
        healthbar = new Label(hpText);

        if(type.getType().equalsIgnoreCase("Archer")){
            Image archer = new Image("/dragAndDrop/assets/archer.png");
            this.setFill(new ImagePattern(archer));
        }
        if(type.getType().equalsIgnoreCase("Swordsman")){
            Image swordsman = new Image("/dragAndDrop/assets/sword.png");
            this.setFill(new ImagePattern(swordsman));
        }
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

    public void takeDamage(double damageDealt){
        this.hp -= 20*damageDealt;
        healthbar.setText(String.valueOf(hp));
    }

    public double getHp(){
        return hp;
    }

    public int getRange() {
        return range;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

//

}
