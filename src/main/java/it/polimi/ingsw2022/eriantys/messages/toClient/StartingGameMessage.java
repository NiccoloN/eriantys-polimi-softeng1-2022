package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;

public class StartingGameMessage extends ToClientMessage {

    private final String[] playerUsernames;
    private final Team[] playerTeams;
    private final Mode gameMode;
    private final Update update;

    public StartingGameMessage(String[] playerUsernames, Team[] playerTeams, Mode gameMode, Update update) {

        this.playerUsernames = playerUsernames;
        this.playerTeams = playerTeams;
        this.gameMode = gameMode;
        this.update = update;
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.startGame(playerUsernames, playerTeams, gameMode);
        client.applyUpdate(update);
    }
}
