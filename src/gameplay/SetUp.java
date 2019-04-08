package gameplay;

import database.Database;

import java.util.ArrayList;

import static database.Variables.db;

/**
 * Class used for setting up the UnitGenerator
 * The importUnitTypes method needs to be run before the UnitGenerator can be used
 */

public class SetUp {

    protected static UnitGenerator unitGenerator;

    protected static ArrayList<Integer> unitTypeList;

    /**
     * The constructor imports a list of unit types from the database
     * @see Database
     */

    public SetUp(){

        if(db == null){
            db = new Database();
        }

        unitTypeList = db.fetchUnitTypeList();
    }

    /**
     * Imports the variables needed to create units from the database and pipes into the UnitGenerator
     * @see Database
     * @see UnitGenerator
     */
    public void importUnitTypes(){

        ProtoUnitType[] unitTypeArray = new ProtoUnitType[unitTypeList.size()];

        for (int i = 0; i < unitTypeList.size(); i++) {

           unitTypeArray[i] = db.importUnitType(unitTypeList.get(i));
        }

        unitGenerator = new UnitGenerator(unitTypeArray[0], unitTypeArray[1], unitTypeArray[2], unitTypeArray[3]);
    }
}