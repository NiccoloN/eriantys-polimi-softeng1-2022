package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;

/**
 * This class represents the movement of mother nature by specifying the destination island's index
 * @author Emanuele Musto
 */
public class MoveMotherNature extends Move {

    int islandIndex;
    int oldIslandIndex;

    public MoveMotherNature(int islandIndex) {

        this.islandIndex = islandIndex;
    }

    @Override
    public boolean isValid(Game game) {

        oldIslandIndex = game.getBoard().getIslandIndex(game.getBoard().getMotherNatureIsland());
        if(oldIslandIndex == islandIndex) {

            errorMessage = "Mother nature must move of at least 1 step";
            return false;
        }

        int maxSteps = game.getCurrentPlayer().getCurrentHelper().movement;
        int steps = 0;
        while((oldIslandIndex + steps) % game.getBoard().getNumberOfIslands() != islandIndex) {

            steps++;
            if(steps > maxSteps) {

                errorMessage = "Cannot move mother nature of more than " + maxSteps + " steps";
                return false;
            }
        }

        return true;
    }

    @Override
    public void apply(Game game) {

        oldIslandIndex = game.getBoard().getIslandIndex(game.getBoard().getMotherNatureIsland());
        int steps = islandIndex - oldIslandIndex;
        game.getBoard().moveMotherNature(steps);
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
