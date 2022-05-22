package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;

/**
 * This class represents the choice of one cloud by a player
 * @author Emanuele Musto
 */
public class ChooseCloud implements Move, Serializable {

    int cloudIndex;

    public ChooseCloud(int cloudIndex) {

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
