package dragAndDrop;

import Database.Database;

import java.sql.ResultSet;
import java.util.concurrent.Callable;

public class Matchmaking extends Thread {
    static int player_id = 2;
    private Database con = new Database();
    private static boolean gameStarted=false;
    private int match_id;

    public void run(){
        System.out.println("Thread Running!");
        //Click to search for games and join if available
        match_id = con.matchMaking_search(player_id);

        //if none available create own game
        if(match_id<0){
            match_id = con.createGame(player_id);
        }

        ////wait for other players to join
        while(!gameStarted){
            try {
                Thread.currentThread().sleep(2000); //Polling only every 2 seconds.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gameStarted = con.pollGameStarted(match_id);
            if(gameStarted){

                //when player join enter game
                //switch scenes.
                enterGame(match_id, player_id);
                System.out.println("Game started");
            }
        }
    }

    public int getMatch_id(){
        return match_id;
    }




    public static void main(String[]args) throws Exception {
        Thread thread = new Matchmaking();
        System.out.println("Before thread start");
        thread.start();
        System.out.println("After thread start");


    }
    public void enterGame(int match_id, int player_id){
        System.out.println("switch scenes");
    }

    public void abortGame(int player_id){
        Database database = new Database();
        database.abortMatch(player_id);
    }
}
