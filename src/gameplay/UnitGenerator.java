package gameplay;

import database.Database;

import database.Variables;
import gameplay.units.Archer;
import gameplay.units.Catapult;
import gameplay.units.Juggernaut;
import gameplay.units.Swordsman;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import static database.Variables.*;


public class  UnitGenerator {

    /**
     * Holds the variables and  and methods for creating new units.
     * When generated, it will create a blueprint UnitType for each unit type in the game.
     * This is done so that unit can be created more efficiently later.
     * @see Unit
     * @see UnitType
     */

    ////Unit ID generator////
    private static int unitId = 0;

    ////Swordsman////
    private static String swordsmanDescription =
            "Because he failed to get into clown college,\n" +
            "he was so distraught that he wowed \n" +
            "to get stronger and faster, but incidentally \n" +
            "he was now better suited to be a legendary swordsman. \n" +
            "Has a longsword, which can slay even the most dangerous of foes.";
    private static String swordsmanDescriptionTag =
            "Legendary swordsman";
    private static Image swordsmanImage;
    private static AudioClip swordsmanSound;

    private static UnitType swordsmanUnitType;

    ////Archer////
    private static String archerDescription =
            "He has mastered his Sodoku in such a ingenious way,\n"+
            "he is now considered godlike amongst his peers.\n"+
            "Too bad this doesn't help him in battle though.\n"+
            "Because of his bow, he has a longer range than others.";
    private static String archerDescriptionTag =
            "Heroic Archer";
    private static Image archerImage;
    private static AudioClip archerSound;

    private static UnitType archerUnitType;

    ////Juggernaut////
    private static String juggernautDescription =
            "After receiving notice of owing taxes to the IRS, \n" +
            "he went into a raging killing frenzy. \n"+
            "Known as as Berserker to some, and to others, \n"+
            "a big angry man with a stick.\n"+
            "Attacks all units in a line, but takes damage every attack";
    private static String juggernautDescriptionTag =
            "Juggernaut";
    private static Image juggernautImage;
    private static AudioClip juggernautSound;

    private static UnitType juggernautUnitType;

    ////Catapult////
    private static String catapultDescription=
            "The sharpest minds, created the greatest\n" +
            "ballistic device the world has ever seen. \n" +
            "This is not that device. \n"+
            "It can launch a payload a great distance, \n"+
            "and has splash damage to nearby enemies.";
    private static String catapultDescriptionTag =
            "Throwy-McGig";
    private static Image catapultImage;
    private static AudioClip catapultSound;

    private static UnitType catapultUnitType;

    /**
     * Constructor that takes in the initial values for the different unit types from SetUp.
     * This constructor has to be used first to have the UnitGenerator work properly
     * @see ProtoUnitType
     */

    public UnitGenerator(){

        //Constructor variables
        ProtoUnitType swordsmanProto;
        ProtoUnitType archerProto;
        ProtoUnitType juggernautProto;
        ProtoUnitType catapultProto;

        //Safety measure, if the Database has yet to be initialized, initialize the database.
        if(db == null){
            db = new Database();
        }

        //Fills in the IDs of all the Units Types in the game
        unitTypeList = db.fetchUnitTypeList();


        swordsmanProto = db.importUnitType(unitTypeList.get(0));
        archerProto = db.importUnitType(unitTypeList.get(1));
        juggernautProto = db.importUnitType(unitTypeList.get(2));
        catapultProto = db.importUnitType(unitTypeList.get(3));


        //Safety measure, if the Database has yet to be initialized, initialize the database.
        if(db == null){
            db = new Database();
        }


        if (!testing){ //if not testing, images and sounds are initialized
            swordsmanImage= new Image(UnitGenerator.class.getResource("./assets/swordsman.png").toExternalForm(), Variables.tileSize, Variables.tileSize, false, false);
            swordsmanSound = new AudioClip(UnitGenerator.class.getResource("assets/hitSword.wav").toString());
            archerSound = new AudioClip(UnitGenerator.class.getResource("assets/arrow.wav").toString());
            archerImage = new Image(UnitGenerator.class.getResource("assets/archer.png").toExternalForm(), Variables.tileSize, Variables.tileSize, false, false);
            juggernautImage = new Image(UnitGenerator.class.getResource("assets/juggernaut.png").toExternalForm(), Variables.tileSize, Variables.tileSize, false, false);
            juggernautSound = new AudioClip(UnitGenerator.class.getResource("assets/juggernaut.wav").toString());
            catapultImage = new Image(UnitGenerator.class.getResource("assets/catapult.png").toExternalForm(), Variables.tileSize, Variables.tileSize, false, false);
            catapultSound = new AudioClip(UnitGenerator.class.getResource("assets/catapult.wav").toString());
        }
        swordsmanUnitType = new Swordsman("Swordsman",
                swordsmanProto.getUnitTypeId(),
                swordsmanProto.getHp(),
                swordsmanProto.getAttack(),
                swordsmanProto.getDefenceMultiplier(),
                swordsmanProto.getMinAttackRange(),
                swordsmanProto.getMaxAttackRange(),
                swordsmanProto.getMovementRange(),
                swordsmanProto.getCost(),
                swordsmanDescription, swordsmanDescriptionTag, swordsmanImage, swordsmanSound);

        archerUnitType =  new Archer("Archer",
                archerProto.getUnitTypeId(),
                archerProto.getHp(),
                archerProto.getAttack(),
                archerProto.getDefenceMultiplier(),
                archerProto.getMinAttackRange(),
                archerProto.getMaxAttackRange(),
                archerProto.getMovementRange(),
                archerProto.getCost(),
                archerDescription, archerDescriptionTag, archerImage, archerSound);

        juggernautUnitType = new Juggernaut("Juggernaut",
                juggernautProto.getUnitTypeId(),
                juggernautProto.getHp(),
                juggernautProto.getAttack(),
                juggernautProto.getDefenceMultiplier(),
                juggernautProto.getMinAttackRange(),
                juggernautProto.getMaxAttackRange(),
                juggernautProto.getMovementRange(),
                juggernautProto.getCost(),
                juggernautDescription, juggernautDescriptionTag, juggernautImage, juggernautSound);

        catapultUnitType = new Catapult("Catapult",
                catapultProto.getUnitTypeId(),
                catapultProto.getHp(),
                catapultProto.getAttack(),
                catapultProto.getDefenceMultiplier(),
                catapultProto.getMinAttackRange(),
                catapultProto.getMaxAttackRange(),
                catapultProto.getMovementRange(),
                catapultProto.getCost(),
                catapultDescription, catapultDescriptionTag, catapultImage, catapultSound);

        unitGenerator = this;
    }

    /**
     * Returns a friendly unit according to an int parameter.
     * The unit is given an incremented unitID
     * @param unitType int. Uses an int to identify which type of Unit to return
     * @return Unit. Returns a unit of the specified type
     * @see Unit
     */

    public Unit newFriendlyUnit(int unitType) {

      switch (unitType) {
        case 1:
            return new Unit( false, swordsmanUnitType, unitId++);

        case 2:
            return new Unit( false, archerUnitType, unitId++);

          case 3:
              return new Unit(false, juggernautUnitType, unitId++);

          case 4:
              return new Unit(false, catapultUnitType, unitId++);
      }
      return null;
    }

    /**
     * Returns a friendly unit according to a String parameter.
     * The unit is given an incremented unitID
     * @param unitType String. Uses a String to identify which type of Unit to return
     * @return Unit. Returns a unit of the specified type
     * @see Unit
     */

    public Unit newFriendlyUnit(String unitType) {

        switch (unitType) {
            case "Swordsman":
                return new Unit( false, swordsmanUnitType, unitId++);

            case "Archer":
                return new Unit( false, archerUnitType, unitId++);

            case "Juggernaut":
                return new Unit(false, juggernautUnitType, unitId++);

            case "Catapult":
                return new Unit(false, catapultUnitType, unitId++);
        }
        return null;
    }

    /**
     * Returns an enemy unit according to an int parameter.
     * The unit is given an imported unit ID
     * This is used as a part of the import enemy units process
     * @param unitType int. Uses an int to identify which type of Unit to return
     * @param unitIdInput int. This is set as the units ID
     * @return Unit. Returns a unit of the specified type with the specified ID
     * @see Unit
     */

    public Unit newEnemyUnit(int unitType, int unitIdInput) {

        switch (unitType) {
            case 1:
                return new Unit( true, swordsmanUnitType, unitIdInput);

            case 2:
                return new Unit( true, archerUnitType, unitIdInput);

            case 3:
                return new Unit(true, juggernautUnitType, unitIdInput);

            case 4:
                return new Unit(true, catapultUnitType, unitIdInput);
        }
        return null;
    }

    /**
     * Returns an enemy unit according to String parameter.
     * The unit is given an imported unit ID
     * This is used as a part of the import enemy units process
     * @param unitType String. Uses a String to identify which type of Unit to return
     * @param unitIdInput int. This is set as the units ID
     * @return Unit. Returns a unit of the specified type with the specified ID
     * @see Unit
     */
    public Unit newEnemyUnit(String unitType, int unitIdInput) {

        switch (unitType) {
            case "Swordsman":
                return new Unit( true, swordsmanUnitType, unitIdInput);

            case "Archer":
                return new Unit( true, archerUnitType, unitIdInput);

            case "Juggernaut":
                return new Unit(true, juggernautUnitType, unitIdInput);

            case "Catapult":
                return new Unit(true, catapultUnitType, unitIdInput);
        }

        return null;
    }

    /**
     * Returns a recruit according to an int parameter.
     * @param unitType int. Uses an int to identify which type of Recruit to return
     * @return Recruit. Returns a recruit of the specified type
     * @see Recruit
     */

    public Recruit newRecruit(int unitType) {
        switch (unitType) {
            case 1:
                return new Recruit(swordsmanUnitType);

            case 2:
                return new Recruit(archerUnitType);

            case 3:
                return new Recruit(juggernautUnitType);

            case 4:
                return new Recruit(catapultUnitType);
        }
        return null;
    }
}
