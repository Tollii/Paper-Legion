package menus;

import menus.controller.MatchSetupController;

/**
 * This class is a holder object for Matches with 1/2 players, and
 * where the game is not started. It uses db.findAvailableGames() method in Database class,
 * to gather and information about matches, and store it in a array that holds this class.
 * It is also used with an ObservableList to list the information in TableView.
 * @see MatchSetupController
 * @see database.Database
 * @see javax.swing.text.TableView
 * @see javafx.collections.ObservableList
 */
public class Match{
    private final int match_id;
    private final String player;
    private final boolean passwordProtected;
    private final String password;

    /**
     * This class is a holder object for Matches with 1/2 players, and
     * where the game is not started. It uses a method in Database class,
     * to gather and information about matches, and store it in a array that holds this class.
     * It is also used with an ObservableList to list the information in TableView.
     * Uses db.findAvailableGames() method.
     * @param match_id Takes in a match id
     * @param player Takes the username of match creator.
     * @param passwordProtected Takes a boolean to show whether match is password protected or not.
     * @param password Takes in a String with the password for the match.
     * @see database.Database
     */
    public Match(int match_id, String player,boolean passwordProtected, String password){
        this.match_id= match_id;
        this.player = player;
        this.passwordProtected = passwordProtected;
        this.password = password;
    }
    /**
     * Returns a integer with the match id.
     * @return int
     */
    public int getMatch_id() {
        return match_id;
    }

    /**
     * Returns a String with the username for player 1.
     * @return String
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Returns a boolean to show whether the match is password protected or not.
     * @return boolean Returns true if password protected, false if not.
     */
    public boolean getPasswordProtected(){
        return passwordProtected;
    }

    /**
     * Returns a String containing the password for the match.
     * @return String
     */
    public String getPassword(){
        return password;
    }

    /**
     * Returns all the variables for the Match in a ToString. Mainly used for test purposes.
     * @return String
     */
    public String toString(){
        return match_id + " : " + player + " : " + password;
    }
}