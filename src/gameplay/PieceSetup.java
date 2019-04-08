package gameplay;


/**
 * This class is used as a placeholder for information stored in database about
 * the opponents units and is used in Database import methods to set up all pieces on
 * the board (Grid)
 * @see database.Database
 * @see Unit
 * @see UnitType
 * @see Grid
 */
public class PieceSetup {
    private int pieceId;
    private int matchId;
    private int playerId;
    private int positionX;
    private int positionY;
    private int unit_type_id;

    /**
     * Sets the pieceId, matchId, playerId, position X, position Y, and unit_type_id for each piece.
     * @param pieceId
     * @see Unit
     * @see UnitType
     */
    public PieceSetup(int pieceId, int matchId, int playerId, int positionX, int positionY, int unit_type_id){

        this.pieceId = pieceId;
        this.matchId = matchId;
        this.playerId = playerId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.unit_type_id = unit_type_id;
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

    public int getUnit_type_id(){ return unit_type_id; }

}
