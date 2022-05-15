package it.polimi.ingsw2022.eriantys.messages.Move;

/**
 * This class represents the movement of mother nature by specifying the destination island's index
 * @author Emanuele Musto
 */
public class MoveMotherNature extends Move{

    int islandIndex;

    public MoveMotherNature(MoveType moveType, int islandIndex){
        this.moveType = moveType;
        this.islandIndex = islandIndex;
    }
}
