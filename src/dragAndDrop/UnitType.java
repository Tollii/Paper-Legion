package dragAndDrop;

public class UnitType {
    private String type;
    private double hp;
    private double attackMultiplier;
    private int range;
    private String description;


    public UnitType(String type, double hp, double attackMultiplier, int range){
        this.type = type;
        this.hp = hp;
        this.attackMultiplier = attackMultiplier;
        this.range = range;


    }

    public String getType() {
        return type;
    }

    public double getHp() {
        return hp;
    }

    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    public int getRange() {
        return range;
    }
}
