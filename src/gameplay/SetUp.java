package gameplay;

import database.Database;

import java.util.ArrayList;

import static database.Database.importUnitType;
import static database.Variables.db;

public class SetUp {

    static UnitGenerator unitGenerator;

    static ArrayList<Integer> unitTypeList;

    public SetUp(){

        if(db == null){
            db = new Database();
        }

        unitTypeList = db.fetchUnitTypeList();
    }

    public void importUnitTypes(){

        ProtoUnitType[] unitTypeArray = new ProtoUnitType[unitTypeList.size()];

        for (int i = 0; i < unitTypeList.size(); i++) {

           unitTypeArray[i] = importUnitType(unitTypeList.get(i));
        }

        unitGenerator = new UnitGenerator(unitTypeArray[0], unitTypeArray[1], unitTypeArray[2], unitTypeArray[3]);
    }
}