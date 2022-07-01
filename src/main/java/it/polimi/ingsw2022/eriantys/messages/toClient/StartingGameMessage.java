package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.IOException;
import java.util.List;

/**
 * This class represents a message sent from the server when the right number of players joined the lobby
 * and the game is ready to start. The message contains the final list of players, the game mode,
 * and the initial update containing every information from the model useful to display on the view.
 */
public class StartingGameMessage extends Message {
    
    private final List<Player> players;
    private final GameMode gameMode;
    private final Update update;
    
    public StartingGameMessage(List<Player> players, GameMode gameMode, Update update) {
        
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
