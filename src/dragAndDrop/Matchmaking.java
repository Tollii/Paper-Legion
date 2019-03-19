package dragAndDrop;

import Database.Database;

import java.sql.ResultSet;

public class Matchmaking implements Runnable {
    static int player_id = 2;
    private Database con = new Database();
    private boolean gameStarted=false;
    private int match_id;

    public Matchmaking(){
    }

    public int getMatch_id(){
        return match_id;
    }

    @Override
    public void run() {
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
                System.out.println("Game started");
            }
        }
    }


    public static void main(String[]args){
        Matchmaking playGame = new Matchmaking();
        playGame.run();



    }
}
