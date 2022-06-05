package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;

public class GameEndedMessage extends ToClientMessage{

    public Team winningTeam;

    public GameEndedMessage(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    @Override
    public void manageAndReply() throws IOException {
        EriantysClient.getInstance().endGame(winningTeam);
    }
}
