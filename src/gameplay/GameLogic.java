//  ██████╗ ██╗███╗   ██╗ █████╗ ██████╗ ██╗   ██╗    ██╗    ██╗ █████╗ ██████╗ ███████╗ █████╗ ██████╗ ███████╗  //
//  ██╔══██╗██║████╗  ██║██╔══██╗██╔══██╗╚██╗ ██╔╝    ██║    ██║██╔══██╗██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝  //
//  ██████╔╝██║██╔██╗ ██║███████║██████╔╝ ╚████╔╝     ██║ █╗ ██║███████║██████╔╝█████╗  ███████║██████╔╝█████╗    //
//  ██╔══██╗██║██║╚██╗██║██╔══██║██╔══██╗  ╚██╔╝      ██║███╗██║██╔══██║██╔══██╗██╔══╝  ██╔══██║██╔══██╗██╔══╝    //
//  ██████╔╝██║██║ ╚████║██║  ██║██║  ██║   ██║       ╚███╔███╔╝██║  ██║██║  ██║██║     ██║  ██║██║  ██║███████╗  //
//  ╚═════╝ ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝        ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝  //
//                                                                                                                //

//   ######## ########    ###    ##     ##       ##   ########     //
//      ##    ##         ## ##   ###   ###     ####   ##    ##     //
//      ##    ##        ##   ##  #### ####       ##       ##       //
//      ##    ######   ##     ## ## ### ##       ##      ##        //
//      ##    ##       ######### ##     ##       ##     ##         //
//      ##    ##       ##     ## ##     ##       ##     ##         //
//      ##    ######## ##     ## ##     ##     ######   ##         //


package gameplay;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import menus.Main;

import java.util.ArrayList;

import static database.Variables.*;
import static gameplay.GameMain.grid;

public class GameLogic {

    public boolean move(int newPosY, int newPosX, ArrayList<Tile> movementTargets) {
        for (int i = 0; i < movementTargets.size(); i++) { //goes through all movement targets and finds the one clicked on
            if (movementTargets.get(i).equals(grid.tileList[newPosY][newPosX])) {
                return true;
            }
        }
        return false;
    }

    public boolean attack(int attackPosX, int attackPosY, ArrayList<Tile> attackTargets) {
        for (int i = 0; i < attackTargets.size(); i++) {
            if(attackTargets.get(i).equals(grid.tileList[attackPosY][attackPosX])) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Tile> getMovementPossibleTiles(Unit selectedUnit, int selectedPosY, int selectedPosX) {
        ArrayList<Tile> movementTargets = new ArrayList<>();

        //goes throug all tiles and adds those which isn't occupied and are within movement distance
        for (int i = 0; i < grid.tileList.length; i++) {
            for (int j = 0; j < grid.tileList[i].length; j++) {
                if (grid.tileList[i][j].getUnit() == null && grid.tileList[i][j].getObstacle() == null) { //checks if there is a unit or obstacle on the tile
                    if (Math.abs(selectedPosY - i) + Math.abs(selectedPosX - j) <= selectedUnit.getMovementRange()) { //checks if tile within movement range
                        movementTargets.add(grid.tileList[i][j]);
                    }
                }
            }
        }
        return movementTargets;
    }

    public ArrayList<Tile> getAttackableTiles(Unit selectedUnit, int selectedPosY, int selectedPosX) {
        ArrayList<Tile> attackTargets = new ArrayList<Tile>();

        for (int i = 0; i < grid.tileList.length; i++) {
            for (int j = 0; j < grid.tileList[i].length; j++) {
                if (grid.tileList[i][j].getUnit() != null) { //checks if there is a unit on the tile
                    if (grid.tileList[i][j].getUnit().getEnemy()) { //checks if unit is enemy
                        if (selectedUnit.getMaxAttackRange() < 2) { //if attackrange = 1, use other method, so that it is possible to attack diagonally
                            if (Math.abs(selectedPosY - i) <= selectedUnit.getMaxAttackRange() && Math.abs(selectedPosX - j) <= selectedUnit.getMaxAttackRange()) { //checks if tile is one of the directly nearby tiles
                                attackTargets.add(grid.tileList[i][j]);
                            }
                        } else { //if attackrange > 1, uses this method
                            if (Math.abs(selectedPosY - i) + Math.abs(selectedPosX - j) <= selectedUnit.getMaxAttackRange()) { //checks if tile within max attack range
                                if (Math.abs(selectedPosY - i) + Math.abs(selectedPosX - j) >= selectedUnit.getMinAttackRange()) { //checks if tile above min attack range
                                    attackTargets.add(grid.tileList[i][j]);
                                }
                            }
                        }
                    }
                }
            }
        }
        return attackTargets;
    }
}