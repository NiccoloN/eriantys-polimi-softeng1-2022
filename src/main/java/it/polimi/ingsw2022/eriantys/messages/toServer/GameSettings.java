package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.server.controller.Mode;

import java.io.Serializable;

public class GameSettings implements Serializable {

    public final int numberOfPlayers;
    public final Mode gameMode;

    public GameSettings(int numberOfPlayers, Mode gameMode) {

        this.numberOfPlayers = numberOfPlayers;
        this.gameMode = gameMode;
    }
}
