package dragAndDrop;

public class UnitGenerator {

    //Variables
    private static int swordsmanMaxHP;
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
            "Has an Attack 2.5x, which can slay even the most dangerous of foes.";

    private static int archerMaxHP;
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


    public UnitGenerator(){}

    public UnitGenerator(int swordsmanMaxHP, int swordsmanAttack, int swordsmanAbilityCooldown, double swordsmanDefenceMultiplier, int swordsmanMinAttackRange, int swordsmanMaxAttackRange, int swordsmanMovementRange,
                         int archerMaxHP, int archerAttack, int archerAbilityCooldown, double archerDefenceMultiplier, int archerMinAttackRange, int archerMaxAttackRange, int archerMovementRange){

        this.swordsmanMaxHP = swordsmanMaxHP;
        this.swordsmanAttack = swordsmanAttack;
        this.swordsmanAbilityCooldown = swordsmanAbilityCooldown;
        this.swordsmanDefenceMultiplier = swordsmanDefenceMultiplier;
        this.swordsmanMinAttackRange = swordsmanMinAttackRange;
        this.swordsmanMaxAttackRange = swordsmanMaxAttackRange;
        this.swordsmanMovementRange = swordsmanMovementRange;

        this.archerMaxHP = archerMaxHP;
        this.archerAttack = archerAttack;
        this.archerAbilityCooldown = archerAbilityCooldown;
        this.archerDefenceMultiplier = archerDefenceMultiplier;
        this.archerMinAttackRange = archerMinAttackRange;
        this.archerMaxAttackRange = archerMaxAttackRange;
        this.archerMovementRange = archerMovementRange;
    }

    public UnitType newArcher(){

        return new Archer("Archer", (double)archerMaxHP, archerAttack, archerAbilityCooldown, archerDefenceMultiplier, archerMinAttackRange, archerMaxAttackRange, archerMovementRange, archerDescription);

    }

    public UnitType newSwordsMan(){

        return new Swordsman("Swordsman", (double)swordsmanMaxHP, swordsmanAttack, swordsmanAbilityCooldown, swordsmanDefenceMultiplier, swordsmanMinAttackRange, swordsmanMaxAttackRange, swordsmanMovementRange, swordsmanDescription);

    }
}
