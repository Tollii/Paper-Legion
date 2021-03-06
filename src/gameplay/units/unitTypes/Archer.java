package gameplay.units.unitTypes;

import gameplay.units.UnitGenerator;
import gameplay.units.UnitType;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;


public class Archer implements UnitType {

    private final String type;
    private final int unitTypeId;
    private final double hp;
    private final int attack;
    private final double defenceMultiplier;
    private final int minAttackRange;
    private final int maxAttackRange;
    private final int movementRange;
    private final int cost;
    private final String description;
    private final String descriptionTag;
    private final Image image;
    private final AudioClip audio;

    /**
     * Constructor that fills inn all the class variables
     * @param type String
     * @param unitTypeId int
     * @param hp double
     * @param attack int
     * @param defenceMultiplier double
     * @param minAttackRange int
     * @param maxAttackRange int
     * @param movementRage int
     * @param cost int
     * @param description String
     * @param descriptionTag String
     * @param image Image
     * @param audio AudioClip
     */
    public Archer(String type, int unitTypeId, double hp, int attack, double defenceMultiplier, int minAttackRange, int maxAttackRange, int movementRage, int cost, String description, String descriptionTag, Image image, AudioClip audio){
        this.type = type;
        this.unitTypeId = unitTypeId;
        this.hp = hp;
        this.attack = attack;
        this.defenceMultiplier = defenceMultiplier;
        this.minAttackRange = minAttackRange;
        this.maxAttackRange = maxAttackRange;
        this.movementRange = movementRage;
        this.cost = cost;
        this.description = description;
        this.descriptionTag = descriptionTag;
        this.image = image;
        this.audio = audio;
    }

    /**
     * Returns the Type as a String
     * The type can be used as an identifier for what unit type an unit is.
     * @return String
     */

    @Override
    public String getType() {
        return type;
    }

    /**
     * Returns the UnitTypeId as an int
     * @return int
     */

    @Override
    public int getUnitTypeId() {
        return unitTypeId;
    }

    /**
     * Returns the Hp as a double
     * @return double
     */

    @Override
    public double getHp() {
        return hp;
    }

    /**
     * Returns the Attack as an int
     * @return int
     */

    @Override
    public int getAttack() {
        return attack;
    }

    /**
     * Returns the DefenceMultiplier as a double
     * @return double
     */

    @Override
    public double getDefenceMultiplier() {
        return defenceMultiplier;
    }

    /**
     * Returns the MinAttackRange as an int
     * @return int
     */

    @Override
    public int getMinAttackRange() {
        return minAttackRange;
    }

    /**
     * Returns the MaxAttackRange as an int
     * @return int
     */

    @Override
    public int getMaxAttackRange() {
        return maxAttackRange;
    }

    /**
     * Returns the MovementRange as an int
     * @return int
     */

    @Override
    public int getMovementRange() {
        return movementRange;
    }

    /**
     * Returns the Cost as an int
     * @return int
     */

    @Override
    public int getCost() {
        return cost;
    }

    /**
     * Returns the Description as a String
     * @return String
     * @see UnitGenerator
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns the DescriptionTag as a String
     * The DescriptionTag is used at the start of the Description
     * @return String
     * @see UnitGenerator
     */

    @Override
    public String getDescriptionTag() {
        return descriptionTag;
    }

    /**
     * Returns the UnitImage as an Image
     * @return Image
     */

    @Override
    public Image getUnitImage() {
        return image;
    }

    /**
     * Returns the Audio as an AudioClip
     * @return AudioClip
     */

    @Override
    public AudioClip getAudio() {
      return audio;
    }
}
