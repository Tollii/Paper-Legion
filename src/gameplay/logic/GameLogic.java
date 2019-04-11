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


package gameplay.logic;


import gameplay.GameMain;
import gameplay.gameboard.Obstacle;
import gameplay.gameboard.Grid;
import gameplay.gameboard.Tile;
import gameplay.units.Unit;

import java.util.ArrayList;
import java.util.Random;

import static database.Variables.*;

/**
 * Class contains all calculation methods used during gameplay such as finding attackable tiles or checking if a move is legal.
 * These methods are testable and return back to GameMain.
 * @see GameMain
 */
public class GameLogic {


    /**
     * Checks to see if move is legal by checking if the move position player wants to move is one
     * of the tiles returns in getMovementPossibleTiles() method.
     * @param newPosY Takes in the y-position player wants to move piece relative to grid.
     * @param newPosX Takes in the x-position player wants to move piece relative to grid.
     * @param movementTargets Takes in a ArrayList with Tile objects
     * @return boolean
     * @see Grid
     * @see GameMain
     */
    public boolean checkForLegalMove(int newPosY, int newPosX, ArrayList<Tile> movementTargets) {

        for(Tile t : movementTargets){
            if (t.equals(grid.tileList[newPosY][newPosX])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if attack is legal by checking if the attack position player wants to attack is one
     * of the tiles returns in getAttackableTiles() method.
     * @param attackPosY Takes in a integer with the y-position relative to the Grid
     * @param attackPosX Takes in a integer with the x-position relative to the Grid.
     * @param attackTargets Takes in a ArrayList with tile objects.
     * @return boolean
     * @see Grid
     * @see GameMain
     */
    public boolean checkForLegalAttack(int attackPosY, int attackPosX, ArrayList<Tile> attackTargets) {
        for(Tile t : attackTargets){
            if(t.equals(grid.tileList[attackPosY][attackPosX])) {
                return true;
            }
        }
        return false;
    }


    /**
     * Gets all possible tiles a unit is within range to move to. Checks to see if move is legal and
     * that there is no enemy unit or obstacle on the tile that are returned in a ArrayList.
     * @return ArrayList with Tile objects
     */
    public ArrayList<Tile> getMovementPossibleTiles() {
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

    /**
     * Checks for opponents within attack range and returns them in a ArrayList with tiles that have opponents units in them.
     * @see GameMain
     * @see Unit
     * @return ArrayList with Tile objects
     */
    public ArrayList<Tile> getAttackableTiles() {
        ArrayList<Tile> attackTargets = new ArrayList<>();

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

    /**
     * Checks if either player or opponent has been eliminated
     * Returns 1 if user loses, 0 if opponent loses, else it returns -1
     * @return int
     * @see GameMain
     */
    public int checkForEliminationVictory() {
        int yourPieces = 0;
        int opponentsPieces = 0;
        //Goes through all units and counts how many are alive for each player.
        for (int i = 0; i < grid.tileList.length; i++) {
            for (int j = 0; j < grid.tileList[i].length; j++) {
                if (grid.tileList[i][j].getUnit() != null) {
                    if (grid.tileList[i][j].getUnit().getEnemy()) {
                        opponentsPieces++;
                    } else {
                        yourPieces++;
                    }
                }
            }
        }
        if (yourPieces == 0) {
            return 1;
        } else if (opponentsPieces == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Executes an attack on the enemy unit and removes the unit from the game if hp is less than 0
     * And sets the unit who has attack with a boolean with the method setHasAttackThisTurn=true
     * so that other methods that check through units for exporting attacks.
     * This method is usually called from another method, and never called alone.
     * @param attackPosY Takes in the Y grid coordinates as an int
     * @param attackPosX Takes in the X grid coordinates as an int
     * @see GameMain
     * @see Grid
     */
    public void executeAttack(int attackPosY, int attackPosX) {
        //damages enemy unit
        grid.tileList[attackPosY][attackPosX].getUnit().takeDamage(selectedUnit.getAttack());

        //removes unit if killed
        if (grid.tileList[attackPosY][attackPosX].getUnit().getHp() <= 0) {
            grid.tileList[attackPosY][attackPosX].removeUnit();
        }
        selectedUnit.setHasAttackedThisTurn(true); //stops unit from attacking again this turn
    }

    /**
     * Creates Obstacles to be placed within the middle region of the grid.
     * Obstacle bound are the size of the grid -4 rows. 2 rows at the top for player 1,
     * and 2 rows at the bottom for player 2.
     * Player 2 creates the obstacle in the method addObstacles() in GameMain.
     * This method uses a random generated number to place the obstacles.
     * Finally this method calls upon db.exportObstacles() to export the obstacle postion
     * to the database, so it will be synchronized for both players.
     * @return ArrayList, returns an ArrayList with Obstacle objects.
     * @see database.Database
     * @see GameMain
     * @see Obstacle
     */
    public ArrayList<Obstacle> createObstacles(){
        Random rand = new Random();
        int obstacleCount = 3 + rand.nextInt(5);
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        int xPos;
        int yPos;
        for (int i = 0; i < obstacleCount; i++) {
            xPos = rand.nextInt(boardSize);
            yPos = 2 + rand.nextInt(boardSize - (2 * 2));
            obstacles.add(new Obstacle(xPos, yPos, i));
        }
        db.exportObstacles(obstacles);
        return obstacles;
    }
}