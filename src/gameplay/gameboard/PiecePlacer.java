package gameplay.gameboard;

import gameplay.units.Unit;
import gameplay.units.UnitType;

/**
 * This class is used as a placeholder for information stored in database about
 * the opponents units and is used in Database import methods to set up all pieces on
 * the board (Grid)
 * @see database.Database
 * @see Unit
 * @see UnitType
 * @see Grid
 */
public class PiecePlacer {
    private final int pieceId;
    private final int matchId;
    private final int playerId;
    private final int positionX;
    private final int positionY;
    private final int unit_type_id;


    /**
     * Sets the pieceId, matchId, playerId, position X, position Y, and unit_type_id for each piece.
     * @param pieceId Sets the pieceId for the Piece.
     * @param  matchId Sets the matchId for the piece.
     * @param playerId Sets the playerId for the piece.
     * @param positionX Sets the piece's x-position in accordance with Grid position.
     * @param positionY Sets the piece's y-position in accordance with Grid position.
     * @param unit_type_id Sets the unit type id for the piece.
     * @see Unit
     * @see UnitType
     * @see Grid
     */
    public PiecePlacer(int pieceId, int matchId, int playerId, int positionX, int positionY, int unit_type_id){

        this.pieceId = pieceId;
        this.matchId = matchId;
        this.playerId = playerId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.unit_type_id = unit_type_id;
    }

    /**
     * Returns a integer with the pieceID.
     * @return int
     */
    public int getPieceId() {
        return pieceId;
    }

    /**
     * Returns a integer with the matchID.
     * @return int
     */
    public int getMatchId() {
        return matchId;
    }

    /**
     * Returns a integer with the playerID associated with the piece.
     * @return int
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Returns the piece X-position in accordance with Grid coordinates.
     * @return int
     * @see Grid
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Returns the piece Y-position in accordance with Grid coordinates.
     * @return int
     * @see Grid
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Returns a integer with the piece's unit_type_id .
     * @return int
     * @see Grid
     */
    public int getUnit_type_id(){ return unit_type_id; }

}
