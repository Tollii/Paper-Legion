package dragAndDrop;

public class PieceSetup {
    private int pieceId;
    private int matchId;
    private int playerId;
    private int positionX;
    private int positionY;
    private int unit_type_id;
    private double current_health;

    public PieceSetup(int pieceId, int matchId, int playerId, int positionX, int positionY, int unit_type_id, double current_health){
        this.pieceId = pieceId;
        this.matchId = matchId;
        this.playerId = playerId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.unit_type_id = unit_type_id;
        this.current_health = current_health;

    }

    public int getPieceId() {
        return pieceId;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getUnit_type_id(){
        return unit_type_id;
    }

    public double getCurrent_health(){
        return current_health;
    }
}
