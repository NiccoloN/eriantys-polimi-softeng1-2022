package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.Move.Move;

import java.io.IOException;

public interface GameMode {

    void playGame() throws IOException, InterruptedException;

    void setPerformedMove(Move move);
}
