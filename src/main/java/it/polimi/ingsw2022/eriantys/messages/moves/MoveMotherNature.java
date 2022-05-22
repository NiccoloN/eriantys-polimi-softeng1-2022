package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.HelperCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

/**
 * This class represents the movement of mother nature by specifying the destination island's index
 * @author Emanuele Musto
 */
public class MoveMotherNature extends Move {

    int islandIndex;
    int oldIslandIndex;

    public MoveMotherNature(int islandIndex) {
        super(MoveType.MOVE_MOTHER_NATURE);
        this.islandIndex = islandIndex;
    }

    @Override
    public void apply(Game game, String username) {
        oldIslandIndex = game.getBoard().getIslandIndex(game.getBoard().getMotherNatureIsland());
        int steps = islandIndex - oldIslandIndex;
        System.out.println("Steps: " + steps);
        game.getBoard().moveMotherNature(steps);
    }

    @Override
    public Update getUpdate(Game game, String playerUsername) {
        Update update = new Update();
        IslandChange oldIslandChange = new IslandChange(oldIslandIndex, game.getBoard().getIsland(oldIslandIndex));
        IslandChange newIslandChange = new IslandChange(islandIndex, game.getBoard().getIsland(islandIndex));

        update.addChange(oldIslandChange);
        update.addChange(newIslandChange);
        System.out.println("Crafted island update");

        return update;
    }
}
