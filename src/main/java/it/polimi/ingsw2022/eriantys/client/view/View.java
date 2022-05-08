package it.polimi.ingsw2022.eriantys.client.view;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Change;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;

import java.util.concurrent.TimeoutException;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public interface View {

    void start() throws TimeoutException;

    void askUsername(Message requestMessage);

    void askGameSettings(Message requestMessage);

    void showUpdatedLobby(String[] playerUsernames, GameSettings gameSettings);

    void startGame();

    void applyChange(IslandChange change);
}
