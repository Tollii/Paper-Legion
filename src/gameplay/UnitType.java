package gameplay;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;


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
