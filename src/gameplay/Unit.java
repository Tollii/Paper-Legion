package gameplay;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

import javafx.scene.media.AudioClip;
/**
 * Creates a unit which holds several attributes; The units movement range,
 * attack range, description, health, attack multiplier, UnitType etc.
 * The unit is created with a StackPane to hold a image in accordance with the UnitType.
 * Units are used to be placed within a tile on the Grid.
 * @see Grid
 * @see Tile
 * @see UnitType
 */
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

    /**
     * Sets the unit variables tile size, whether unit is enemy or not, attack multiplier, max-, min attack range,
     * ability cooldown, defence multiplier, movement range, cost, description, description tag, piece id.
     * The constructor sets the image to show for the Unit, the colors in accordance to the player who owns the unit, and
     * also adds a health bar label on top of the unit image, to show current hp.
     * It takes three parameters, boolean enemy, UnitType type, and int unitId.
     * @param enemy   Sets the unit as either enemy or friendly
     * @param type  Sets the UnitType for the unit.
     * @param unitId  Sets the unitId for the unit.
     * @see UnitType
     */
    public Unit(boolean enemy, UnitType type, int unitId) {
        rect = new Rectangle();
        rect.setWidth(GameMain.tileSize);
        rect.setHeight(GameMain.tileSize);
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
        healthbar = new Label(hpText);

        this.getChildren().addAll(rect, healthbar);
        healthbar.setPrefWidth(GameMain.tileSize);
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

    /**
     * Returns a boolean to show whether or not the unit is a enemy or not.
     * @return boolean Returns boolean true if enemy, and false for friendly units.
     */
    public boolean getEnemy() {
        return enemy;
    }

    /**
     * Returns a boolean to show whether or not the unit has attacked for the players turn.
     * Used to find out which unit has attacked in the course of a turn, and is used to send
     * the information to the database.
     * @return boolean Returns true if the unit has attacked on the players turn.
     */
    public boolean getHasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    /**
     * Sets the boolean value of the unit if this unit has attack within his turn.
     * Is to be used to collect the information of only the units who has attacked,
     * and the information is sent to the Database.
     * @param hasAttackedThisTurn Sets the boolean value if the unit has attacked within a turn.
     */
    public void setHasAttackedThisTurn(boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    /**
     * Returns the UnitType in the form of a String.
     * @return String Returns the UnitType in the form of a String.
     */
    public String getTypeString() {
        return type.getType();
    }

    /**
     * Returns the class UnitType of the unit.
     * @return UnitType Returns the UnitType
     */
    public UnitType getType() {
        return type;
    }

    /**
     * Returns a integer with the UnitType Id nr.
     * @return int Returns the UnitType id nr.
     * @see UnitType
     */
    public int getUnitTypeId() {
        return unitTypeId;
    }

    /**
     * Returns the units current health.
     * @return double Returns the units current health.
     */
    public double getHp() {
        return hp;
    }

    /**
     * Returns the units damage multiplier
     * @return int Returns the units damage multiplier
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Returns a int with the ability cooldown of a unit.
     * @return int Returns the ability cooldown of a unit.
     */
    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    /**
     * Returns a double with the defence multiplier of a unit.
     * @return double Returns the defence multiplier of a unit.
     */
    public double getDefenceMultiplier() {
        return defenceMultiplier;
    }

    /**
     * Returns a integer with the minimum attack range of a unit.
     * @return int Returns the minimum attack range of a unit.
     */
    public int getMinAttackRange() {
        return minAttackRange;
    }

    /**
     * Returns a integer with the max attack range of a unit.
     * @return int Returns the max attack range of a unit.
     */
    public int getMaxAttackRange() {
        return maxAttackRange;
    }

    /**
     * Returns the movement range of a unit.
     * @return int Returns a integer of allowed movement range in any direction.
     */
    public int getMovementRange() {
        return movementRange;
    }

    /**
     * Returns the cost of the Unit to use with the placement phase.
     * @return  Returns the cost of the unit.
     * @see GameMain
     */
    public int getCost() {
        return cost;
    }

    /**
     * Returns a integer with the piece ID of the unit.
     * @return int Returns the piece id of the unit.
     */
    public int getPieceId() {
        return pieceId;
    }

    /**
     * Returns the cost of the Unit to use with the placement phase.
     * @return String Returns a string with the units attributes and description text and tags.
     */
    public String getDescription() {

        return descriptionTag + "\n" +
                "\nHp: " + hp +
                "\nMovement Range: " + movementRange +
                "\nAttack: " + attack + "x\n" + "\n" + description;
    }

    /**
     * Sets the current health for the unit.
     * Also changes the health bar from green to red if hp is below threshold.
     * @param hp Sets the current health for the unit
     */
    public void setHp(double hp) {
        this.hp = hp;

        healthbar.setText(String.valueOf(hp));

        ////CHANGES THE COLOUR OF THE HP-BAR////
        if (hp <= LOW_HP_THRESHOLD) {
            healthbar.setStyle("-fx-background-color: Red");
        }
    }

    /**
     * Reduces the health of a unit by taking damage dealt by another unit.
     * The amount of damage taken is calculated by using the units defence multiplier
     * against the Attackers damage multiplier.
     * @param damageDealt Gets the damage multiplier of the unit that attacked.
     */
    public void takeDamage(double damageDealt) {

        this.hp -= damageDealt / defenceMultiplier;

        healthbar.setText(String.valueOf(hp));

        ////CHANGES THE COLOUR OF THE HP-BAR////
        if (hp <= LOW_HP_THRESHOLD) {
            healthbar.setStyle("-fx-background-color: Red");
        }
    }

    /**
     * Returns the image associated with the UnitType
     * @return Image  returns the Image of the UnitType
     * @see Image
     */
    public Image getUnitImage() {
        return type.getUnitImage();
    }


    /**
     * Returns the AudioClip associated with the UnitType
     * @return AudioClip  returns the AudioClip of the UnitType
     * @see AudioClip
     */
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
