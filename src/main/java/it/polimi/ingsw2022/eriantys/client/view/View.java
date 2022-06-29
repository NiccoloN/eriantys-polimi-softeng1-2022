package it.polimi.ingsw2022.eriantys.client.view;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;
import java.util.List;

/**
 * This interface represents the view of the game. It is implemented by both EriantysGUI and EriantysCLI
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 * @see EriantysGUI
 * @see EriantysCLI
 */
public interface View {
    
    /**
     * Makes this view ask the user for a username
     * @param requestMessage the message requesting a username
     */
    void askUsername(Message requestMessage) throws IOException;
    
    /**
     * Makes this view ask the user for game settings
     * @param requestMessage the message requesting game settings
     */
    void askGameSettings(Message requestMessage) throws IOException;
    
    /**
     * Makes this view show the lobby waiting room, with the updated info
     * @param playerUsernames the usernames of the players currently connected to the lobby
     * @param gameSettings    the game settings of the lobby
     */
    void showUpdatedLobby(String[] playerUsernames, GameSettings gameSettings) throws IOException;
    
    /**
     * Makes this view start the game, showing the game scenario
     * @param players  the players of the game
     * @param gameMode the mode of the game
     */
    void startGame(List<Player> players, GameMode gameMode) throws IOException;
    
    /**
     * Applies a given update to the game this view is showing
     * @param update the update to apply
     */
    void applyUpdate(Update update);
    
    /**
     * Ask the client for a certain move
     * @param requestMessage the message containing a move request
     */
    void requestMove(MoveRequestMessage requestMessage);
    
    /**
     * Ends the game and shows winner team
     * @param team winner team
     */
    void endGame(Team team);
}
