package backup;

public class Move {

    private int turnId;
    private int pieceId;
    private int matchId;
    private int startPosX;
    private int startPosY;
    private int endPosX;
    private int endPosY;

    public Move(int turnId, int pieceId, int matchId, int startPosX, int startPosY, int endPosX, int endPosY){
        this.turnId = turnId;
        this.pieceId = pieceId;
        this.matchId = matchId;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.endPosX = endPosX;
        this.endPosY = endPosY;
    }

    public int getTurnId() {
        return turnId;
    }

    public void setTurnId(int turnId) {
        this.turnId = turnId;
    }

    public int getPieceId() {
        return pieceId;
    }

    public void setPieceId(int pieceId) {
        this.pieceId = pieceId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getStartPosX() {
        return startPosX;
    }

    public void setStartPosX(int startPosX) {
        this.startPosX = startPosX;
    }

    public int getStartPosY() {
        return startPosY;
    }

    public void setStartPosY(int startPosY) {
        this.startPosY = startPosY;
    }

    public int getEndPosX() {
        return endPosX;
    }

    public void setEndPosX(int endPosX) {
        this.endPosX = endPosX;
    }

    public int getEndPosY() {
        return endPosY;
    }

    public void setEndPosY(int endPosY) {
        this.endPosY = endPosY;
    }
}
