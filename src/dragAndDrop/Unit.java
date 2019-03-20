package dragAndDrop;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Unit extends Rectangle {
    private Label healthbar;

    private double oldPosX;
    private double oldPosY;


    ////UNIT INFO////
    private UnitType type;
    private double hp;
    private int attack;
    private int abilityCooldown;
    private double defenceMultiplier;
    private int minAttackRange;
    private int maxAttackRange;
    private int movementRange;
    private String description;
    private String descriptionTag;


    ////UTILITIES////
    private final int LOW_HP_THRESHOLD = 20;
    private boolean enemy;
    private GridPane pieceAvatar =  new GridPane();


    public Unit(double row, double column, boolean enemy, UnitType type){
        super.setWidth(GameLogic.tileSize);
        super.setHeight(GameLogic.tileSize);
        this.enemy = enemy;

        ///SETS UNIT INFO////
        this.type = type;
        this.hp = type.getHp();
        attack = type.getAttack();
        abilityCooldown = type.getAbilityCooldown();
        this.defenceMultiplier = type.getDefenceMultiplier();
        minAttackRange = type.getMinAttackRange();
        maxAttackRange = type.getMaxAttackRange();
        this.movementRange = type.getMovementRange();
        description = type.getDescription();
        descriptionTag = type.getDescriptionTag();

        setPositionArray(column,row);
        String hpText = String.valueOf(hp);
        healthbar = new Label(hpText);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(this, healthbar);
        pieceAvatar.getChildren().add(stackPane);
        healthbar.setPrefWidth(GameLogic.tileSize);
        healthbar.setAlignment(Pos.CENTER);



        healthbar.setTranslateX(this.getTranslateX());
        healthbar.setTranslateY(this.getTranslateY()+40);


        healthbar.setStyle("-fx-background-color: Green;" + "-fx-text-fill: White;");


        ///SETS UNIT IMAGE////
        switch (type.getType()){

            case "Archer":
                if(enemy){
                    Image archerGold = new Image("/dragAndDrop/assets/archer_gold.png");
                    this.setFill(new ImagePattern(archerGold));
                } else{
                    Image archerBlue = new Image("/dragAndDrop/assets/archer_blue.png");
                    this.setFill(new ImagePattern(archerBlue));
                }
                break;

            case "Swordsman":
                if(enemy){
                    Image swordsmanGold = new Image("/dragAndDrop/assets/sword_gold.png");
                    this.setFill(new ImagePattern(swordsmanGold));
                } else{
                    Image swordsmanBlue = new Image("/dragAndDrop/assets/sword_blue.png");
                    this.setFill(new ImagePattern(swordsmanBlue));
                }
                break;
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

    public GridPane getPieceAvatar(){
        return pieceAvatar;
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

    ////GET UNIT INFO////
    public String getType(){
        return type.getType();
    }

    public double getHp(){
        return hp;
    }

    public int getAttack(){
        return attack;
    }

    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    public double getDefenceMultiplier() {
        return defenceMultiplier;
    }

    public int getMinAttackRange() {
        return minAttackRange;
    }

    public int getMaxAttackRange() {
        return maxAttackRange;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public String getDescription(){

        return descriptionTag + "\n" +
                "\nHp: " + hp +
                "\nMovement Range: " + movementRange +
                "\nAttack: " + attack + "x\n" + "\n" + description;
    }


    public void takeDamage(double damageDealt){

        this.hp -= damageDealt / defenceMultiplier;

        healthbar.setText(String.valueOf(hp));

        ////CHANGES THE COLOUR OF THE HP-BAR////
        if(hp <= LOW_HP_THRESHOLD){
            healthbar.setStyle("-fx-background-color: Red");
        }
    }

}
