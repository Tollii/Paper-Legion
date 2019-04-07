package gameplay;


import database.Variables;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

import javafx.scene.media.AudioClip;

import static database.Variables.testing;

public class Unit extends StackPane {
    private Label healthbar;
    private Rectangle rect;

    ////UNIT INFO////
    private final UnitType type;
    private final int unitTypeId;
    private double hp;
    private int attack;
    private int abilityCooldown;
    private double defenceMultiplier;
    private int minAttackRange;
    private int maxAttackRange;
    private int movementRange;
    private int cost;
    private String description;
    private String descriptionTag;


    ////UTILITIES////
    private final int LOW_HP_THRESHOLD = 20;
    private boolean enemy;
    private boolean hasAttackedThisTurn;
    private int healthbarPosY = 40;
    private int pieceId;

    public Unit(boolean enemy, UnitType type, int unitId) {
        rect = new Rectangle();
        rect.setWidth(Variables.tileSize);
        rect.setHeight(Variables.tileSize);
        this.enemy = enemy;

        ///SETS UNIT INFO////
        this.type = type;
        unitTypeId = type.getUnitTypeId();
        hp = type.getHp();
        attack = type.getAttack();
        abilityCooldown = type.getAbilityCooldown();
        defenceMultiplier = type.getDefenceMultiplier();
        minAttackRange = type.getMinAttackRange();
        maxAttackRange = type.getMaxAttackRange();
        movementRange = type.getMovementRange();
        cost = type.getCost();
        description = type.getDescription();
        descriptionTag = type.getDescriptionTag();
        pieceId = unitId;

        String hpText = String.valueOf(hp);
        if(!testing) {
            healthbar = new Label(hpText);

            this.getChildren().addAll(rect, healthbar);
            healthbar.setPrefWidth(Variables.tileSize);
            healthbar.setAlignment(Pos.CENTER);
            healthbar.setTranslateY(healthbarPosY);

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
    }

    public boolean getEnemy() {
        return enemy;
    }

    public boolean getHasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    public void setHasAttackedThisTurn(boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    ////GET UNIT INFO////
    public String getTypeString() {
        return type.getType();
    }

    public UnitType getType() {
        return type;
    }

    public int getUnitTypeId() {
        return unitTypeId;
    }

    public double getHp() {
        return hp;
    }

    public int getAttack() {
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

    public int getCost() {
        return cost;
    }

    public int getPieceId() {
        return pieceId;
    }

    public String getDescription() {

        return descriptionTag + "\n" +
                "\nHp: " + hp +
                "\nMovement Range: " + movementRange +
                "\nAttack: " + attack + "x\n" + "\n" + description;
    }

    public void setHp(double hp) {
        this.hp = hp;

        healthbar.setText(String.valueOf(hp));

        ////CHANGES THE COLOUR OF THE HP-BAR////
        if (hp <= LOW_HP_THRESHOLD) {
            healthbar.setStyle("-fx-background-color: Red");
        }
    }

    public void takeDamage(double damageDealt) {

        this.hp -= damageDealt / defenceMultiplier;

        healthbar.setText(String.valueOf(hp));

        ////CHANGES THE COLOUR OF THE HP-BAR////
        if (hp <= LOW_HP_THRESHOLD) {
            healthbar.setStyle("-fx-background-color: Red");
        }
    }

    public Image getUnitImage() {
        return type.getUnitImage();
    }

    public AudioClip getAudio() {
        return type.getAudio();
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
