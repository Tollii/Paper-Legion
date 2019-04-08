package backup;

class Attack {

    ////VARIABLES////
    private final int turnId;
    private final int matchId;
    private final int attackingPlayerId;
    private final int attackerPieceID;
    private final int receiverPieceID;
    private final int damage;


    ////CONSTRUCTOR////
    public Attack(int turnId, int matchID, int attackingPlayerId, int attackerPieceID, int receiverPieceID, int damage){

        this.turnId = turnId;
        this.matchId = matchID;
        this.attackingPlayerId = attackingPlayerId;
        this.attackerPieceID = attackerPieceID;
        this.receiverPieceID = receiverPieceID;
        this.damage = damage;
    }


    public int getTurnId() {
        return turnId;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getAttackingPlayerId() {
        return attackingPlayerId;
    }

    public int getAttackerPieceID() {
        return attackerPieceID;
    }

    public int getReceiverPieceID() {
        return receiverPieceID;
    }

    public int getDamage() {
        return damage;
    }
}
