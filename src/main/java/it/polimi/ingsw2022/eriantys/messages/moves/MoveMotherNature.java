package it.polimi.ingsw2022.eriantys.messages.moves;

/**
 * This class represents the movement of mother nature by specifying the destination island's index
 * @author Emanuele Musto
 */
public class MoveMotherNature extends Move {

    int islandIndex;

    public MoveMotherNature(int islandIndex) {
        this.moveType = MoveType.MOVE_MOTHER_NATURE;
        this.islandIndex = islandIndex;
    }
}
