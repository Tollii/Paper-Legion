package database;

import Runnables.RunnableInterface;
import gameplay.Grid;
import gameplay.Obstacle;
import gameplay.Unit;

import java.util.ArrayList;

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
    public static final int tileSize = (int)(screenWidth * 0.06);      //sets size of each tile on the grid
    public static Grid grid = new Grid(boardSize, boardSize);
    public static ArrayList<Obstacle> obstacles;                                  //list of obstacles
    public static Unit selectedUnit;
    public static int selectedPosX;
    public static int selectedPosY;

}
