package it.polimi.ingsw2022.eriantys.messages.Move;

import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

/**
 * This class represents the movement of a student. It can be to the dining table or to an island.
 * If it's towards an island it specifies that island index.
 * @author Emanuele Musto
 */
public class MoveStudent extends Move{

    public final boolean toDining, toIsland;
    public final int islandIndex;
    public final PawnColor studentColor;


    public MoveStudent(MoveType movetype, boolean toDining, boolean toIsland, int islandIndex, PawnColor studentColor){
        this.moveType = movetype;
        this.toDining = toDining;
        this.toIsland = toIsland;
        this.islandIndex = islandIndex;
        this.studentColor = studentColor;
    }
}
