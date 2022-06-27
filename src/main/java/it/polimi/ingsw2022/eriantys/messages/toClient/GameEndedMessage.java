package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;

/**
 * This class represents a message to let the client know that the game has come to an end
 * (according to the rules of the game), and to display the winner.
 */
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
