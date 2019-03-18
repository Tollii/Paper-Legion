package dragAndDrop;

import Database.Database;

import java.sql.ResultSet;

public class Matchmaking {
    static int player_id = 2;
    Database con = new Database();

    public Matchmaking(){

        //Click to search for games and join if available
        int match_id = con.matchMaking_search(player_id);

        //if none available create own game
        if(match_id<0){
            con.createGame(player_id);
        }

        //wait for other players to join
        //when player join enter game
        //delete game on abort.
    }

    public static void main(String[]args){
        Matchmaking a = new Matchmaking();
    }


}
