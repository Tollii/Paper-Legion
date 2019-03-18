package dragAndDrop;

public interface UnitType {
  final double hp, dmg, atkRange, movement;
  final String type;

  static void attack() {

 }
  static public String getType() {
    return type;
  }

  static public double getHp() {
    return hp;
  }

  static public double getDmg() {
    return dmg;
  }

  static public double getAtkRange() {
    return atkRange;
  }

  static public double getMovement() {
    return movement;
  }
}
