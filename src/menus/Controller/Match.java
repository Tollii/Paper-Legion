package menus.Controller;

public class Match{
    private int match_id;
    private String player;
    private boolean passwordProtected;
    private String password;

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