package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;

/**
 * This class represents the choice of a player to stop using a character card
 * @author Emanuele Musto
 */
public class Abort extends Move {
    
    @Override
    public boolean isValid(Game game, MoveRequest request) {
        
        return true;
    }
    
    @Override
    public void apply(Game game) {}
    
    @Override
    public Update getUpdate(Game game) {
        
        return null;
    }
}
