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

public class Unit extends Rectangle {
    private Label healthbar;
    private UnitType type;
    private double oldPosX;
    private double oldPosY;
    private double hp;
    private double damageMultiplier;
    private int range;
    private boolean enemy;
    GridPane a =  new GridPane();


    public Unit(double row, double column, boolean enemy, UnitType type){
        super.setWidth(Main.tileSize);
        super.setHeight(Main.tileSize);
        this.enemy = enemy;
        this.type = type;
        this.damageMultiplier = type.getAttackMultiplier();
        this.hp = type.getHp();
        this.range = type.getRange();

        setPositionArray(column,row);
        String hpText = String.valueOf(hp);
        healthbar = new Label(hpText);
        StackPane sp = new StackPane();
        sp.getChildren().addAll(this, healthbar);
        a.getChildren().add(sp);
        healthbar.setPrefWidth(Main.tileSize);
        healthbar.setAlignment(Pos.CENTER);


        healthbar.setTranslateX(this.getTranslateX());
        healthbar.setTranslateY(this.getTranslateY()+40);


        healthbar.setStyle("-fx-background-color: Green;" + "-fx-text-fill: White;");




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
        healthbar.setTranslateX(this.getTranslateX());
        healthbar.setTranslateY(this.getTranslateY()+40);
    }

    public double getOldPosX(){
        return oldPosX;
    }

    public double getOldPosY(){
        return oldPosY;
    }

    public void setPositionArray(double x, double y){
        super.setTranslateX(x);
        super.setTranslateY(y);
    }

    public void setTranslate(double x, double y){
        super.setTranslateX(x*100);
        super.setTranslateY(y*100);
        healthbar.setTranslateX(this.getTranslateX());
        healthbar.setTranslateY(this.getTranslateY()+40);
    }

    public boolean getEnemy(){
        return enemy;
    }

    public void takeDamage(double damageDealt){
        this.hp -= 20*damageDealt;
        healthbar.setText(String.valueOf(hp));

        if(hp<=20){
            healthbar.setStyle("-fx-background-color: Red");
        }
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

    public String getDescription(){

        //DUMMY TEXT, HAVE TO INTEGRATE THIS DECRIPTION INTO UNIT TYPE INSTEAD.
        if(type.getType().equalsIgnoreCase("Swordsman")){
            String description =
                    "Legendary Swordsman\n" +
                            "\nHp: " + getHp()
                            +"\nRange: " + getRange() +
                            "\nAttack: " + getDamageMultiplier() + "x\n" +
                            "\nBecause he failed to get into clown college,\n" +
                            "he was so distraught that he wowed to get stronger and faster,\n" +
                            "but incidentally he was now better suited to be a legendary swordsman. \n" +
                            "Has an Attack 2.5x, which can slay even the most dangerous of foes.";
            return description;
        }
        if(type.getType().equalsIgnoreCase("Archer")){
            String description =
                    "Heroic Archer\n" +
                            "\nHp: " + getHp()
                            +"\nRange: " + getRange() +
                            "\nAttack: " + getDamageMultiplier() + "x\n" +
                    "\nHe has mastered his Sodoku in such a ingenious way,\n"+
                            "he is now considered godlike amongst his peers.\n"+
                            "Too bad this doesn't help him in battle though.\n"+
                            "Because of his bow, he has a longer range than others.";
            return description;
        }
        return null;
    }

    public String getType(){
        return type.getType();
    }

}
