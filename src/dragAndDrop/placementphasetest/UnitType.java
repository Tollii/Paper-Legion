package dragAndDrop;
//Basic inteface klasse for unittypes
public interface UnitType {
  //attributtene er final, siden de er referanseverdier for brikketypen
  final double hp, dmg, atkRange, movement;
  final String type;
  //default attack metode, kan ovverides i enkelt unit typen
  static void attack() {

  }
  //basic getmetoder
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
