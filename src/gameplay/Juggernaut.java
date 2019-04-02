package gameplay;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;


public class Juggernaut implements UnitType {

    private final String type;
    private final int unitTypeId;
    private final double hp;
    private final int attack;
    private final int abilityCooldown;
    private final double defenceMultiplier;
    private final int minAttackRange;
    private final int maxAttackRange;
    private final int movementRange;
    private final String description;
    private final String descriptionTag;
    private final Image image;
    private final AudioClip audio;

    public Juggernaut(String type, int unitTypeId, double hp, int attack, int abilityCooldown, double defenceMultiplier, int minAttackRange, int maxAttackRange, int movementRage, String description, String descriptionTag, Image image, AudioClip audio){
        this.type = type;
        this.unitTypeId = unitTypeId;
        this.hp = hp;
        this.attack = attack;
        this.abilityCooldown = abilityCooldown;
        this.defenceMultiplier = defenceMultiplier;
        this.minAttackRange = minAttackRange;
        this.maxAttackRange = maxAttackRange;
        this.movementRange = movementRage;
        this.description = description;
        this.descriptionTag = descriptionTag;
        this.image = image;
        this.audio = audio;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getUnitTypeId() {
        return unitTypeId;
    }

    @Override
    public double getHp() {
        return hp;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    @Override
    public double getDefenceMultiplier() {
        return defenceMultiplier;
    }

    @Override
    public int getMinAttackRange() {
        return minAttackRange;
    }

    @Override
    public int getMaxAttackRange() {
        return maxAttackRange;
    }

    @Override
    public int getMovementRange() {
        return movementRange;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDescriptionTag() {
        return descriptionTag;
    }

    @Override
    public Image getUnitImage() {
        return image;
    }

    @Override
    public AudioClip getAudio() {
        return audio;
    }
}
