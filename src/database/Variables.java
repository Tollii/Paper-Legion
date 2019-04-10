package database;

import runnables.RunnableInterface;
import gameplay.Grid;
import gameplay.Obstacle;
import gameplay.Unit;
import gameplay.UnitGenerator;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Contains all important variables that needs to be accessed throughout the programs lifespan.
 * These are static so they can be accessed and modified everywhere.
 */

public class Variables {
    public static Database db;

    //Screen info
    public static double screenWidth;
    public static double screenHeight;

    //Match info
    public static int user_id;
    public static int match_id;
    public static boolean yourTurn;
    public static boolean opponentReady = false;
    public static int turn = 1;
    public static int player1;
    public static int player2;
    public static int opponent_id;

    //Resource(placement phase)
    public static final int startingResources = 1000;
    public static int currentResources;

    //Threads
    public static Thread searchGameThread;
    public static Thread waitTurnThread;
    public static Thread waitPlacementThread;
    public static RunnableInterface searchGameRunnable;
    public static RunnableInterface waitTurnRunnable;
    public static RunnableInterface waitPlacementRunnable;


    //GameMain Variables;
    public static final int boardSize = 7;      //sets the number of tiles en each direction of the grid
    public static int tileSize;   //sets size of each tile on the grid
    public static Grid grid = null;
    public static ArrayList<Obstacle> obstacles;               //list of obstacles
    public static Unit selectedUnit;
    public static int selectedPosX;
    public static int selectedPosY;

    //Tile selection variables
    public static final Paint standardTileColor = Color.WHITE;
    public static final Paint standardStrokeColor = Color.BLACK;
    public static final StrokeType standardStrokePlacement = StrokeType.INSIDE;
    public static final Paint selectionOutlineColor = Color.rgb(56, 31, 217);
    public static final int standardStrokeWidth = 1;
    public static final int selectedStrokeWidth = 3;

    public static boolean testing = false;

    //Utilities
    public static ArrayList<Integer> unitTypeList;
    public static UnitGenerator unitGenerator;

}
