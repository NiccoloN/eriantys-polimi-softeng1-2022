package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 */
public class GameSettings implements Serializable {

    public final int numberOfPlayers;
    public final Mode gameMode;

    public GameSettings(int numberOfPlayers, Mode gameMode) {

        this.numberOfPlayers = numberOfPlayers;
        this.gameMode = gameMode;
    }

    public boolean isValid() {

        boolean valid = true;
        if (numberOfPlayers < Game.MIN_NUMBER_OF_PLAYERS || numberOfPlayers > Game.MAX_NUMBER_OF_PLAYERS) valid = false;
        if (Arrays.stream(Mode.values()).noneMatch((mode) -> mode == gameMode)) valid = false;
        return valid;
    }
}
