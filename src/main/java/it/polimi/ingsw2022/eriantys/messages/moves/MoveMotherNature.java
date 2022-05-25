package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;

/**
 * This class represents the movement of mother nature by specifying the destination island's index
 * @author Emanuele Musto
 */
public class MoveMotherNature implements Move, Serializable {

    int islandIndex;
    int oldIslandIndex;

    public MoveMotherNature(int islandIndex) {

        this.islandIndex = islandIndex;
    }

    @Override
    public String apply(Game game) {

        oldIslandIndex = game.getBoard().getIslandIndex(game.getBoard().getMotherNatureIsland());
        int steps = islandIndex - oldIslandIndex;
        System.out.println("Steps: " + steps);
        game.getBoard().moveMotherNature(steps);
        return null;
    }

    @Override
    public Update getUpdate(Game game) {
        Update update = new Update();
        IslandChange oldIslandChange = new IslandChange(oldIslandIndex, game.getBoard().getIsland(oldIslandIndex));
        IslandChange newIslandChange = new IslandChange(islandIndex, game.getBoard().getIsland(islandIndex));

        update.addChange(oldIslandChange);
        update.addChange(newIslandChange);
        System.out.println("Crafted island update");

        return update;
    }
}
