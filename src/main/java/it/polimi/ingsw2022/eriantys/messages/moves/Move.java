package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;

/**
 * This class represents a generic Move done by a player
 * @author Emanuele Musto
 */
public abstract class Move implements Serializable {

    protected String errorMessage;

    public abstract boolean isValid(Game game, MoveRequest request);

    public String getErrorMessage() {

        return errorMessage;
    }

    public abstract void apply(Game game);

    public abstract Update getUpdate(Game game);
}