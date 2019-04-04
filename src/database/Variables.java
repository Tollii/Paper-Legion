package database;

import Runnables.RunnableInterface;

public class Variables {
    public static Database db;
    public static int user_id;
    public static int match_id;
    public static boolean yourTurn;
    public static boolean opponentReady = false;
    public static int turn = 1;
    public static int player1;
    public static int player2;
    public static int opponent_id;

    //Threads
    public static Thread searchGameThread;
    public static Thread waitTurnThread;
    public static Thread waitPlacementThread;
    public static RunnableInterface searchGameRunnable;
    public static RunnableInterface waitTurnRunnable;
    public static RunnableInterface waitPlacementRunnable;

}
