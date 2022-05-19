package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

/**
 * This class represents the movement of mother nature by specifying the destination island's index
 * @author Emanuele Musto
 */
public class MoveMotherNature extends Move {

    int islandIndex;

    public MoveMotherNature(int islandIndex) {
        super(MoveType.MOVE_MOTHER_NATURE);
        this.islandIndex = islandIndex;
    }

    @Override
    public void apply(Game game, String username) {

    }

    @Override
    public Update getUpdate(Game game) {
        return null;
    }
}
