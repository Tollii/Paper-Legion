package dragAndDrop;

import Database.Database;

import java.util.ArrayList;

import static sample.Main.db;

public class SetUp {

    protected static ArrayList<String> unitTypeList;

    public SetUp(){

        unitTypeList = db.fetchUnitTypeList();
    }

    public void importUnitTypes(){

        ProtoUnitType[] unitTypeArray = new ProtoUnitType[unitTypeList.size()];

        for (int i = 0; i < unitTypeList.size(); i++) {

            unitTypeArray[i] = db.importUnitType(unitTypeList.get(i));
        }

        UnitGenerator unitGenerator = new UnitGenerator(unitTypeArray[0], unitTypeArray[1]);
    }
}