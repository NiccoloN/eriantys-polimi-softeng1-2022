package it.polimi.ingsw2022.eriantys.client.view;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.GameInitChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

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
     * @param playerUsernames the username of each player in the game
     * @param playerTeams the team of each player in the game
     * @param gameMode the mode of the game
     */
    void startGame(String[] playerUsernames, Team[] playerTeams, Mode gameMode);

    /**
     * Applies a given change to the game this view is showing
     * @param change the change to apply
     */
    void applyChange(GameInitChange change);

    /**
     * Applies a given change to the game this view is showing
     * @param change the change to apply
     */
    void applyChange(IslandChange change);

    /**
     * Applies a given change to the game this view is showing
     * @param change the change to apply
     */
    void applyChange(CloudChange change);

    /**
     * Applies a given change to the game this view is showing
     * @param change the change to apply
     */
    void applyChange(SchoolDashboardChange change);

    /**
     * Applies a given change to the game this view is showing
     * @param change the change to apply
     */
    void applyChange(StudentsBagChange change);

    /**
     * Applies a given change to the game this view is showing
     * @param change the change to apply
     */
    void applyChange(HelperCardsChange change);

    /**
     * Ask the client for a certain move
     * @param previousMessage the message containing a move request
     */
    void askMoveType(MoveRequestMessage previousMessage);
}
