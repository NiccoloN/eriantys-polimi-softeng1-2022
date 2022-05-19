package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

/**
 * This class represents the choice of one cloud by a player
 * @author Emanuele Musto
 */
public class ChooseCloud extends Move {

    int cloudIndex;

    public ChooseCloud(int cloudIndex) {
        super(MoveType.CHOOSE_CLOUD);
        this.cloudIndex = cloudIndex;
    }

    @Override
    public void apply(Game game, String username) {

    }

    @Override
    public Update getUpdate(Game game, String playerUsername) {
        return null;
    }
}
