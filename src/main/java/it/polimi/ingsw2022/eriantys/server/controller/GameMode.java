package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;

import java.io.IOException;

public interface GameMode {

    void playGame() throws IOException, InterruptedException;

    /**
     * @return an array of initial updates, one for each player
     */
    Update[] createInitialUpdates();

    void managePerformedMoveMessage(PerformedMoveMessage moveMessage) throws IOException, InterruptedException;

    GamePhase getCurrentGamePhase();

    void setCurrentGamePhase(GamePhase currentGamePhase);

    boolean getEndGameNow();

    void setEndGameNow(boolean endGameNow);
}
