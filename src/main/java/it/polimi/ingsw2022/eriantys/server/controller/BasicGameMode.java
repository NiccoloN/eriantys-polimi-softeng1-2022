package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.server.model.Game;

public class BasicGameMode implements GameMode {

    private Game game;

    public BasicGameMode(Game game) {
        this.game = game;
    }

    @Override
    public void playGame() {

    }
}
