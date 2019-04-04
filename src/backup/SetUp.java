//package backup;
//
//import database.Database;
//
//import java.util.ArrayList;
//
//import static database.Variables.db;
//
//public class SetUp {
//
//    protected static UnitGenerator unitGenerator;
//
//    protected static ArrayList<Integer> unitTypeList;
//
//    public SetUp(){
//
//        if(db == null){
//            db = new Database();
//        }
//
//        unitTypeList = db.fetchUnitTypeList();
//    }
//
//    public void importUnitTypes(){
//
//        ProtoUnitType[] unitTypeArray = new ProtoUnitType[unitTypeList.size()];
//
//        for (int i = 0; i < unitTypeList.size(); i++) {
//
//           unitTypeArray[i] = db.importUnitType(unitTypeList.get(i));
//        }
//
//        unitGenerator = new UnitGenerator(unitTypeArray[0], unitTypeArray[1]);
//    }
//}