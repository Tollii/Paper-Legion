package dragAndDrop;

public class PieceSetup {
    private int pieceId;
    private int matchId;
    private int playerId;
    private int positionX;
    private int positionY;

    public PieceSetup(int pieceId, int matchId, int playerId, int positionX, int positionY){
        this.pieceId = pieceId;
        this.matchId = matchId;
        this.playerId = playerId;
        this.positionX = positionX;
        this.positionY = positionY;
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
}
