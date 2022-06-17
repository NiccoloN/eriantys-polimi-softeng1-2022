package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

public class Abort extends Move{

    @Override
    public boolean isValid(Game game) {
        return true;
    }

    @Override
    public void apply(Game game) { game.setAbortMessageReceived(true); }

    @Override
    public Update getUpdate(Game game) {
        return null;
    }
}
