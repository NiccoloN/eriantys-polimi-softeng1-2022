package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.Serializable;

/**
 * This class represents a generic Move done by a player
 * @author Emanuele Musto
 */
public interface Move {

    void apply(Game game, String playerUsername);

    Update getUpdate(Game game, String playerUsername);
}