package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

/**
 * This class represents a generic Move done by a player
 * @author Emanuele Musto
 */
public interface Move {

    String apply(Game game);

    Update getUpdate(Game game);
}