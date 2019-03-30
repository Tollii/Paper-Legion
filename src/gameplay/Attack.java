package gameplay;

public class Attack {

    ////VARIABLES////
    private final int turnId;
    private final int matchID;
    private final int attackingPlayerId;
    private final int attackerPieceID;
    private final int reciverPieceID;
    private final int damage;


    ////CONSTRUCTOR////
    public Attack(int turnId, int matchID, int attackingPlayerId, int attackerPieceID, int reciverPieceID, int damage){

        this.turnId = turnId;
        this.matchID = matchID;
        this.attackingPlayerId = attackingPlayerId;
        this.attackerPieceID = attackerPieceID;
        this.reciverPieceID = reciverPieceID;
        this.damage = damage;
    }


    public int getTurnId() {
        return turnId;
    }

    public int getMatchID() {
        return matchID;
    }

    public int getAttackingPlayerId() {
        return attackingPlayerId;
    }

    public int getAttackerPieceID() {
        return attackerPieceID;
    }

    public int getReciverPieceID() {
        return reciverPieceID;
    }

    public int getDamage() {
        return damage;
    }
}
