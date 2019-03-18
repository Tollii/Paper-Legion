package dragAndDrop;

import Database.Database;

import java.sql.ResultSet;

public class Matchmaking {
    Database con = new Database();

    public Matchmaking(){
        //Click find match button,
        int match_id = con.matchMaking_search();
        System.out.println(match_id);
        //If available join game.
      //  con.joinGame();

        //search for available games
        //if none available create own game
        //wait for other players to join
        //when player join enter game
    }

    public static void main(String[]args){
        Matchmaking a = new Matchmaking();
    }


}
