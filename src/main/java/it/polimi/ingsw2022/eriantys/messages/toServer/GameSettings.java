package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

/**
 * This class represents the game settings chosen by the first player connecting.
 * It contains the desired number of players and the game mode, or the choice of loading ad existing game.
 *
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 */
public class GameSettings implements Serializable {
    
    public final boolean loadGame;
    public final int numberOfPlayers;
    public final GameMode gameMode;
    
    public GameSettings() {
        
        loadGame = true;
        numberOfPlayers = 0;
        gameMode = null;
    }
    
    public GameSettings(int numberOfPlayers, GameMode gameMode) {
        
        loadGame = false;
        this.numberOfPlayers = numberOfPlayers;
        this.gameMode = gameMode;
    }
    
    public boolean isValid() {
        
        File saveFile = new File(EriantysServer.SAVE_FILE_PATH);
        if (loadGame && saveFile.exists()) return true;
        
        boolean valid = numberOfPlayers >= Game.MIN_NUMBER_OF_PLAYERS && numberOfPlayers <= Game.MAX_NUMBER_OF_PLAYERS;
        if (Arrays.stream(GameMode.values()).noneMatch((mode) -> mode == gameMode)) valid = false;
        return valid;
    }
}
