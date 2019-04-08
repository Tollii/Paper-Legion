package gameplay;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

/**
 * Interface for ensuring the functionally and variables of the different unit type classes.
 * @see gameplay.units.Swordsman
 * @see gameplay.units.Archer
 * @see gameplay.units.Juggernaut
 * @see gameplay.units.Catapult
 */

public interface UnitType {

    String getType();

    int getUnitTypeId();

    double getHp();

    int getAttack();

    int getAbilityCooldown();

    double getDefenceMultiplier();

    int getMinAttackRange();

    int getMaxAttackRange();

    int getMovementRange();

    int getCost();

    String getDescription();

    String getDescriptionTag();

    Image getUnitImage();

    AudioClip getAudio();
  }