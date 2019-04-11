package gameplay.units;

import gameplay.units.unitTypes.Archer;
import gameplay.units.unitTypes.Catapult;
import gameplay.units.unitTypes.Juggernaut;
import gameplay.units.unitTypes.Swordsman;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

/**
 * Interface for ensuring the functionally and variables of the different unit type classes.
 * @see Swordsman
 * @see Archer
 * @see Juggernaut
 * @see Catapult
 */

public interface UnitType {

    String getType();

    int getUnitTypeId();

    double getHp();

    int getAttack();

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