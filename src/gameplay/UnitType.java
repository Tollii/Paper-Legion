package Gameplay;

public interface UnitType {

    String getType();

    double getHp();

    int getAttack();

    int getAbilityCooldown();

    double getDefenceMultiplier();

    int getMinAttackRange();

    int getMaxAttackRange();

    int getMovementRange();

    String getDescription();

    String getDescriptionTag();
}
