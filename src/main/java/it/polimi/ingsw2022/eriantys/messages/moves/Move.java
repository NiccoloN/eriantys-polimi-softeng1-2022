package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.Serializable;

/**
 * This class represents a generic Move done by a player
 * @author Emanuele Musto
 */
public abstract class Move implements Serializable {
    public final MoveType moveType;
    protected Player currentPlayer;

    public Move(MoveType moveType){
        this.moveType = moveType;
    }

    public abstract void apply(Game game, String playerUsername);

    public abstract Update getUpdate(Game game);
}