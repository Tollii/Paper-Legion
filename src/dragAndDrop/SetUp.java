package dragAndDrop;

import Database.Database;

public class SetUp {

    Database database = new Database();

    public SetUp(){}

    public void importUnitTypes(){

        ProtoUnitType[] unitTypeArray = new ProtoUnitType[2];

        unitTypeArray[0] = Database.importUnitType("swordsman");
        unitTypeArray[1] = Database.importUnitType("archer");


        UnitGenerator unitGenerator = new UnitGenerator(unitTypeArray[0], unitTypeArray[1]);


    }
}
