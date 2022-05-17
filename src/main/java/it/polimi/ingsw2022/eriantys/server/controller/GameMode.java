package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.Move.Move;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;

import java.io.IOException;

public interface GameMode {

    void playGame() throws IOException, InterruptedException;

    void setPerformedMoveMessage(PerformedMoveMessage moveMessage);
}
