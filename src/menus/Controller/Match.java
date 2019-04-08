package menus.Controller;

/**
 * This class is a holder object for Matches with 1/2 players, and
 * where the game is not started. It uses a method in Database class,
 * to gather and information about matches, and store it in a array that holds this class.
 * It is also used with an ObservableList to list the information in TableView.
 * @see MatchSetupController
 * @see database.Database
 * @see javax.swing.text.TableView
 * @see javafx.collections.ObservableList
 */
public class Match{
    private int match_id;
    private String player;
    private boolean passwordProtected;
    private String password;

    /**
     * This class is a holder object for Matches with 1/2 players, and
     * where the game is not started. It uses a method in Database class,
     * to gather and information about matches, and store it in a array that holds this class.
     * It is also used with an ObservableList to list the information in TablieView.
     */
    public Match(int match_id, String player,boolean passwordProtected, String password){
        this.match_id= match_id;
        this.player = player;
        this.passwordProtected = passwordProtected;
        this.password = password;
    }

    public int getMatch_id() {
        return match_id;
    }

    public String getPlayer() {
        return player;
    }

    public boolean getPasswordProtected(){
        return passwordProtected;
    }

    public String getPassword(){
        return password;
    }

    public String toString(){
        return match_id + " : " + player + " : " + password;
    }
}