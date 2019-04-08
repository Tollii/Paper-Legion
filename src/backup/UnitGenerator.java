package backup;

class UnitGenerator {

    //Variables
    private static UnitType swordsmanUnitType;

    private static UnitType archerUnitType;


    public UnitGenerator(){}

    public UnitGenerator(ProtoUnitType swordsmanProto,
                         ProtoUnitType archerProto){

        double swordsmanMaxHP = swordsmanProto.getHp();
        int swordsmanAttack = swordsmanProto.getAttack();
        int swordsmanAbilityCooldown = swordsmanProto.getAbilityCooldown();
        double swordsmanDefenceMultiplier = swordsmanProto.getDefenceMultiplier();
        int swordsmanMinAttackRange = swordsmanProto.getMinAttackRange();
        int swordsmanMaxAttackRange = swordsmanProto.getMaxAttackRange();
        int swordsmanMovementRange = swordsmanProto.getMovementRange();
        String swordsmanDescriptionTag = "Legendary swordsman";
        String swordsmanDescription = "Because he failed to get into clown college,\n" +
                "he was so distraught that he wowed to get stronger and faster,\n" +
                "but incidentally he was now better suited to be a legendary swordsman. \n" +
                "Has a longsword, which can slay even the most dangerous of foes.";
        swordsmanUnitType = new Swordsman("Swordsman", swordsmanMaxHP, swordsmanAttack, swordsmanAbilityCooldown, swordsmanDefenceMultiplier, swordsmanMinAttackRange, swordsmanMaxAttackRange, swordsmanMovementRange, swordsmanDescription, swordsmanDescriptionTag);


        double archerMaxHP = archerProto.getHp();
        int archerAttack = archerProto.getAttack();
        int archerAbilityCooldown = archerProto.getAbilityCooldown();
        double archerDefenceMultiplier = archerProto.getDefenceMultiplier();
        int archerMinAttackRange = archerProto.getMinAttackRange();
        int archerMaxAttackRange = archerProto.getMaxAttackRange();
        int archerMovementRange = archerProto.getMovementRange();
        String archerDescriptionTag = "Heroic Archer";
        String archerDescription = "He has mastered his Sodoku in such a ingenious way,\n" +
                "he is now considered godlike amongst his peers.\n" +
                "Too bad this doesn't help him in battle though.\n" +
                "Because of his bow, he has a longer range than others.";
        archerUnitType = new Archer("Archer", archerMaxHP, archerAttack, archerAbilityCooldown, archerDefenceMultiplier, archerMinAttackRange, archerMaxAttackRange, archerMovementRange, archerDescription, archerDescriptionTag);

    }


    public UnitType newArcher(){

        return archerUnitType;

    }

    public UnitType newSwordsMan(){

        return swordsmanUnitType;

    }
}