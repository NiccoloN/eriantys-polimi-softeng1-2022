package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.IOException;
import java.util.List;

public class StartingGameMessage extends ToClientMessage {

    private final List<Player> players;
    private final Mode gameMode;
    private final Update update;

    public StartingGameMessage(List<Player> players, Mode gameMode, Update update) {

        this.players = players;
        this.gameMode = gameMode;
        this.update = update;
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.startGame(players, gameMode);
        client.applyUpdate(update);
    }
}
