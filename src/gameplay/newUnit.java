package gameplay;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;

public class Unit extends StackPane {
    private Label healthbar;
    private Rectangle rect;

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

    public Unit(boolean enemy, UnitType type){
        rect = new Rectangle();
        rect.setWidth(GameLogic.tileSize);
        rect.setHeight(GameLogic.tileSize);
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

        String hpText = String.valueOf(hp);
        healthbar = new Label(hpText);

        this.getChildren().addAll(rect, healthbar);
        healthbar.setPrefWidth(GameLogic.tileSize);
        healthbar.setAlignment(Pos.CENTER);
        healthbar.setTranslateY(40);


        healthbar.setStyle("-fx-background-color: Green;" + "-fx-text-fill: White;");


        ///SETS UNIT IMAGE////
        if (enemy) { //sets gold if enemy, blue if friendly
          BufferedImage imgBuf = SwingFXUtils.fromFXImage(type.getUnitImage(), null);
          changeColor(imgBuf, 0, 0, 0, 155, 135, 65);
          rect.setFill(new ImagePattern(SwingFXUtils.toFXImage(imgBuf, null)));
        } else {
          BufferedImage imgBuf = SwingFXUtils.fromFXImage(type.getUnitImage(), null);
          changeColor(imgBuf, 0, 0, 0, 56, 31, 217);
          rect.setFill(new ImagePattern(SwingFXUtils.toFXImage(imgBuf, null)));
        }
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

    public void setHp(double hp){
        this.hp = hp;

        healthbar.setText(String.valueOf(hp));

        ////CHANGES THE COLOUR OF THE HP-BAR////
        if(hp <= LOW_HP_THRESHOLD){
            healthbar.setStyle("-fx-background-color: Red");
        }
    }

    public void takeDamage(double damageDealt){

        this.hp -= damageDealt / defenceMultiplier;

        healthbar.setText(String.valueOf(hp));

        ////CHANGES THE COLOUR OF THE HP-BAR////
        if(hp <= LOW_HP_THRESHOLD){
            healthbar.setStyle("-fx-background-color: Red");
        }
    }

    public Image getUnitImage() {
      return type.getUnitImage();
    }

    private static void changeColor(BufferedImage imgBuf, int oldRed, int oldGreen, int oldBlue, int newRed, int newGreen, int newBlue) {

      int RGB_MASK = 0x00ffffff;
      int ALPHA_MASK = 0xff000000;

      int oldRGB = oldRed << 16 | oldGreen << 8 | oldBlue;
      int toggleRGB = oldRGB ^ (newRed << 16 | newGreen << 8 | newBlue);

      int w = imgBuf.getWidth();
      int h = imgBuf.getHeight();

      int[] rgb = imgBuf.getRGB(0, 0, w, h, null, 0, w);
      for (int i = 0; i < rgb.length; i++) {
          if ((rgb[i] & RGB_MASK) == oldRGB) {
              rgb[i] ^= toggleRGB;
          }
      }
      imgBuf.setRGB(0, 0, w, h, rgb, 0, w);
    }
}
