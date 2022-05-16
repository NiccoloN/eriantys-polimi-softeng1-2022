package it.polimi.ingsw2022.eriantys.messages.Move;

/**
 * This class represents the movement of a student. It can be to the dining table or to an island.
 * If it's towards an island it specifies that island index.
 * @author Emanuele Musto
 */
public class MoveStudent extends Move{

    boolean toDining, toIsland;
    int islandIndex;

    public MoveStudent(MoveType movetype, boolean toDining, boolean toIsland, int islandIndex){
        this.moveType = movetype;
        this.toDining = toDining;
        this.toIsland = toIsland;
        this.islandIndex = islandIndex;
    }
}
