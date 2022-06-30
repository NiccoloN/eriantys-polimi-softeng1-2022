package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;

/**
 * This class represents a generic Move done by a player during the game.
 *
 * @author Emanuele Musto
 */
public abstract class Move implements Serializable {
    
    protected String errorMessage;
    
    /**
     * Checks if this move made by the player is valid and withing the game rules.
     * It also checks if the move made matches with the request send by the server.
     *
     * @param game    current state of the model
     * @param request message sent by the server that this move must match with
     * @return true if this move made by the player is valid, false otherwise.
     */
    public abstract boolean isValid(Game game, MoveRequest request);
    
    /**
     * @return the error message to display on the view when this move is not valid.
     */
    public String getErrorMessage() {
        
        return errorMessage;
    }
    
    /**
     * Applies this move to the model. This method is used by the controller if this move is valid.
     *
     * @param game the model.
     */
    public abstract void apply(Game game);
    
    /**
     * Prepares the specific update needed for this move. The controller uses this method after applying this move to the model.
     *
     * @param game the model to take the changes from.
     * @return the update containing all the needed changes.
     */
    public abstract Update getUpdate(Game game);
}