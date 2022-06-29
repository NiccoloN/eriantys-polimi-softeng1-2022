package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;

import java.io.IOException;

/**
 * This interface represents the controller of the game
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 * @author Niccoloò Nicolosi
 */
public interface GameController {
    
    void playGame() throws IOException, InterruptedException;
    
    /**
     * Sends the updates when the game starts. It is basically ad update for the entire model.
     * @return the initial update needed at the beginning of the game.
     */
    Update[] createInitialUpdates();
    
    /**
     * This method is called by the server when a performed move arrives from a client.
     * Checks if the move is valid, then if true it applies to the game the effect of the move and sends an update to the
     * clients, if false sends an invalid move message with explanations.
     * @param performedMoveMessage the move performed by one player.
     * @throws IOException          when output stream throws an exception (while sending a message to client).
     * @throws InterruptedException when the thread waiting for a response by the client is interrupted.
     */
    void managePerformedMoveMessage(PerformedMoveMessage performedMoveMessage) throws IOException, InterruptedException;
    
    GamePhase getCurrentGamePhase();
    
    void setCurrentGamePhase(GamePhase currentGamePhase);
    
    boolean getEndGameNow();
    
    void setEndGameNow(boolean endGameNow);
}
