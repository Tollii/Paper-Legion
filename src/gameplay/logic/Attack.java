package gameplay.logic;

import gameplay.units.Unit;

public class Attack {

    /**
     * Holds information about an attack that has been made during the players or the opponents turn.
     * Used to import or export attack information to/from the database.
     * @see database.Database
     */
    ////VARIABLES////
    private final int turnId;
    private final int matchId;
    private final int attackingPlayerId;
    private final int attackerPieceID;
    private final int receiverPieceID;
    private final int damage;

    /**
     * @param turnId Int. Which turn the attack took place
     * @param matchID Int. The match the move took place
     * @param attackingPlayerId Int. The attacking player
     * @param attackerPieceID Int. The piece that executed the attack
     * @param receiverPieceID Int. The piece receiving the damage
     * @param damage Int. The amount of damage done. This is the raw damage before any defence multipliers.
     */
    ////CONSTRUCTOR////
    public Attack(int turnId, int matchID, int attackingPlayerId, int attackerPieceID, int receiverPieceID, int damage){

        this.turnId = turnId;
        this.matchId = matchID;
        this.attackingPlayerId = attackingPlayerId;
        this.attackerPieceID = attackerPieceID;
        this.receiverPieceID = receiverPieceID;
        this.damage = damage;
    }

    /**
     * Returns the TurnId when the attack took place
     * @return Int.
     */
    public int getTurnId() {
        return turnId;
    }

    /**
     * Returns the MatchId of which match the attack took place in
     * @return Int.
     */
    public int getMatchId() {
        return matchId;
    }

    /**
     * Returns the PlayerId of the attacking player
     * @return Int.
     */
    public int getAttackingPlayerId() {
        return attackingPlayerId;
    }

    /**
     * Returns the PieceId of the attacking piece
     * @return Int.
     */
    public int getAttackerPieceID() {
        return attackerPieceID;
    }

    /**
     * Returns the PieceId that is being attacked
     * @return Int.
     */
    public int getReceiverPieceID() {
        return receiverPieceID;
    }

    /**
     * Returns the amount of damage being sent. This is before any defence multiplier.
     * @return Int.
     * @see Unit
     */
    public int getDamage() {
        return damage;
    }
}
