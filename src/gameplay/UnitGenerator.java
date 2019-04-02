package gameplay;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;


public class  UnitGenerator {

    //Variables

    ////Unit ID generator////
    private static int unitId = 0;

    ////Swordsman////
    private static int swordsmanUnitTypeId;
    private static double swordsmanMaxHP;
    private static int swordsmanAttack;
    private static int swordsmanAbilityCooldown;
    private static double swordsmanDefenceMultiplier;
    private static int swordsmanMinAttackRange;
    private static int swordsmanMaxAttackRange;
    private static int swordsmanMovementRange;
    private static String swordsmanDescription =
            "Because he failed to get into clown college,\n" +
            "he was so distraught that he wowed to get stronger and faster,\n" +
            "but incidentally he was now better suited to be a legendary swordsman. \n" +
            "Has a longsword, which can slay even the most dangerous of foes.";
    private static String swordsmanDescriptionTag =
            "Legendary swordsman";
    private static Image swordsmanImage = new Image(UnitGenerator.class.getResource("assets/swordsman.png").toExternalForm(), GameLogic.tileSize, GameLogic.tileSize, false, false);
    private static AudioClip swordsmanSound = new AudioClip(UnitGenerator.class.getResource("assets/hitSword.wav").toString());

    private static UnitType swordsmanUnitType;

    ////Archer////
    private static int archerUnitTypeId;
    private static double archerMaxHP;
    private static int archerAttack;
    private static int archerAbilityCooldown;
    private static double archerDefenceMultiplier;
    private static int archerMinAttackRange;
    private static int archerMaxAttackRange;
    private static int archerMovementRange;
    private static String archerDescription =
            "He has mastered his Sodoku in such a ingenious way,\n"+
            "he is now considered godlike amongst his peers.\n"+
            "Too bad this doesn't help him in battle though.\n"+
            "Because of his bow, he has a longer range than others.";
    private static String archerDescriptionTag =
            "Heroic Archer";
    private static Image archerImage = new Image(UnitGenerator.class.getResource("assets/archer.png").toExternalForm(), GameLogic.tileSize, GameLogic.tileSize, false, false);
    private static AudioClip archerSound = new AudioClip(UnitGenerator.class.getResource("assets/arrow.wav").toString());

    private static UnitType archerUnitType;

    ////Juggernaut////
    private static String juggernautDescription =
            "Big angry man with stick, sometimes turns green";
    private static String juggernautDescriptionTag =
            "Juggernaut";
    //TODO
    private static Image juggernautImage = null;
    //TODO
    private static AudioClip juggernautSound = null;

    ////Catapult////
    private static String catapultDescription=
            "Usurped by the clearly better trebuchet";
    private static String catapultDescriptionTag =
            "Throwy-McGig";
    //TODO
    private static Image catapultImage = null;
    //TODO
    private static AudioClip catapultSound = null;


    public UnitGenerator(){}

    public UnitGenerator(ProtoUnitType swordsmanProto,
                         ProtoUnitType archerProto, ProtoUnitType juggernautProto, ProtoUnitType catapultProto){

        this.swordsmanUnitTypeId = swordsmanProto.getUnitTypeId();
        this.swordsmanMaxHP = swordsmanProto.getHp();
        this.swordsmanAttack = swordsmanProto.getAttack();
        this.swordsmanAbilityCooldown = swordsmanProto.getAbilityCooldown();
        this.swordsmanDefenceMultiplier = swordsmanProto.getDefenceMultiplier();
        this.swordsmanMinAttackRange = swordsmanProto.getMinAttackRange();
        this.swordsmanMaxAttackRange = swordsmanProto.getMaxAttackRange();
        this.swordsmanMovementRange = swordsmanProto.getMovementRange();
        swordsmanUnitType = new Swordsman("Swordsman", swordsmanUnitTypeId, swordsmanMaxHP, swordsmanAttack, swordsmanAbilityCooldown, swordsmanDefenceMultiplier, swordsmanMinAttackRange, swordsmanMaxAttackRange, swordsmanMovementRange, swordsmanDescription, swordsmanDescriptionTag, swordsmanImage, swordsmanSound);

        this.archerUnitTypeId = archerProto.getUnitTypeId();
        this.archerMaxHP = archerProto.getHp();
        this.archerAttack = archerProto.getAttack();
        this.archerAbilityCooldown = archerProto.getAbilityCooldown();
        this.archerDefenceMultiplier = archerProto.getDefenceMultiplier();
        this.archerMinAttackRange = archerProto.getMinAttackRange();
        this.archerMaxAttackRange = archerProto.getMaxAttackRange();
        this.archerMovementRange = archerProto.getMovementRange();
        archerUnitType =  new Archer("Archer", archerUnitTypeId, archerMaxHP, archerAttack, archerAbilityCooldown, archerDefenceMultiplier, archerMinAttackRange, archerMaxAttackRange, archerMovementRange, archerDescription, archerDescriptionTag, archerImage, archerSound);


    }

    public UnitType newArcher(){

        return archerUnitType;

    }

    public UnitType newSwordsMan(){

        return swordsmanUnitType;

    }

    public Unit newFriendlyUnit(int unitType) {

      switch (unitType) {
        case 1:
            return new Unit( false, swordsmanUnitType, unitId++);

        case 2:
            return new Unit( false, archerUnitType, unitId++);
      }
      return null;
    }

    public Unit newFriendlyUnit(String unitType) {

        switch (unitType) {
            case "Swordsman":
                return new Unit( false, swordsmanUnitType, unitId++);

            case "Archer":
                return new Unit( false, archerUnitType, unitId++);
        }

        return null;
    }

    public Unit newEnemyUnit(int unitType, int unitIdInput) {

        switch (unitType) {
            case 1:
                return new Unit( true, swordsmanUnitType, unitIdInput);

            case 2:
                return new Unit( true, archerUnitType, unitIdInput);
        }
        return null;
    }

    public Unit newEnemyUnit(String unitType, int unitIdInput) {

        switch (unitType) {
            case "Swordsman":
                return new Unit( true, swordsmanUnitType, unitIdInput);

            case "Archer":
                return new Unit( true, archerUnitType, unitIdInput);
        }

        return null;
    }



    public Recruit newRecruit(int unitType) {
        switch (unitType) {
            case 1:
                return new Recruit(swordsmanUnitType);

            case 2:
                return new Recruit(archerUnitType);
        }
        return null;
    }
}
