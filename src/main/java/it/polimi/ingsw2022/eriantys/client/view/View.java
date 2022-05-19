package it.polimi.ingsw2022.eriantys.client.view;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.Message;

import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;

import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.util.concurrent.TimeoutException;

/**
 * This interface represents the view of the game. It is implemented by both EriantysGUI and EriantysCLI
 * @see EriantysGUI
 * @see EriantysCLI
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public interface View {

    /**
     * Starts this view
     * @param showLog whether this view should show logs
     * @throws TimeoutException if this view stops responding
     */
    void start(boolean showLog) throws TimeoutException;

    /**
     * Makes this view ask the user for a username
     * @param requestMessage the message requesting a username
     */
    void askUsername(Message requestMessage);

    /**
     * Makes this view ask the user for game settings
     * @param requestMessage the message requesting game settings
     */
    void askGameSettings(Message requestMessage);

    /**
     * Makes this view show the lobby waiting room, with the updated info
     * @param playerUsernames the usernames of the players currently connected to the lobby
     * @param gameSettings the game settings of the lobby
     */
    void showUpdatedLobby(String[] playerUsernames, GameSettings gameSettings);

    /**
     * Makes this view start the game, showing the game scenario
     * @param players the players of the game
     * @param gameMode the mode of the game
     */
    void startGame(Player[] players, Mode gameMode);

    /**
     * Applies a given update to the game this view is showing
     * @param update the update to apply
     */
    void applyUpdate(Update update);

    /**
     * Ask the client for a certain move
     * @param previousMessage the message containing a move request
     */
    void askMove(MoveRequestMessage previousMessage);

}
