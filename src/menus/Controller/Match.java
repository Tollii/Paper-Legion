package menus.Controller;

public class Match{
    private int match_id;
    private String player;
    private boolean password;

    public Match(int match_id, String player, boolean password){
        this.match_id= match_id;
        this.player = player;
        this.password = password;
    }

    public int getMatch_id() {
        return match_id;
    }

    public String getPlayer() {
        return player;
    }

    public boolean getPassword(){
        return password;
    }
}