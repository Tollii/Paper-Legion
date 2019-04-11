package gameplay.logic;

/**
 * Holds information about a move that has been made during the players or the opponents turn.
 * Used to import or export movement information to/from the database.
 * @see database.Database
 */
public class Move {

    private final int turnId;
    private final int pieceId;
    private final int matchId;
    private final int startPosX;
    private final int startPosY;
    private final int endPosX;
    private final int endPosY;

    /**
     * @param turnId Int. Which turn the move took place
     * @param pieceId Int. The ID of the unit that made the move
     * @param matchId Int. The match the move took place in
     * @param startPosX Int.
     * @param startPosY Int.
     * @param endPosX Int.
     * @param endPosY Int.
     */
    public Move(int turnId, int pieceId, int matchId, int startPosX, int startPosY, int endPosX, int endPosY){
        this.turnId = turnId;
        this.pieceId = pieceId;
        this.matchId = matchId;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.endPosX = endPosX;
        this.endPosY = endPosY;
    }

    /**
     * Returns the TurnId when the move took place
     * @return Int.
     */
    public int getTurnId() {
        return turnId;
    }

    /**
     * Returns the PieceId of the piece that moved
     * @return Int.
     */
    public int getPieceId() {
        return pieceId;
    }

    /**
     * Returns the MatchId of which match the move took place in
     * @return Int.
     */
    public int getMatchId() {
        return matchId;
    }

    /**
     * Returns the StartPosX
     * @return Int.
     */
    public int getStartPosX() {
        return startPosX;
    }

    /**
     * Returns the StartPosY
     * @return Int.
     */
    public int getStartPosY() {
        return startPosY;
    }

    /**
     * Returns the EndPosX
     * @return Int.
     */
    public int getEndPosX() {
        return endPosX;
    }

    /**
     * Returns the EndPosY
     * @return Int.
     */
    public int getEndPosY() {
        return endPosY;
    }
}
