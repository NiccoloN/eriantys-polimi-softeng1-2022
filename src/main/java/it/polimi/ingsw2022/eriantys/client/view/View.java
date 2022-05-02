package it.polimi.ingsw2022.eriantys.client.view;

import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;

import java.util.concurrent.TimeoutException;

public interface View {

    void start() throws TimeoutException;

    String getUsername();
    GameSettings getGameSettings();
}
