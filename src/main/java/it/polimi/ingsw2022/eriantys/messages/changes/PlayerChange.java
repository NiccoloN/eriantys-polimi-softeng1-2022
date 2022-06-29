package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player.PlayerStatusCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import javafx.application.Platform;

import java.io.Serializable;

/**
 * This class represents a change of the player. Whenever they change (change in coins or username),
 * the clients will be updated with this change.
 */
public class PlayerChange implements Change, Serializable {
    
    private final Player player;
    
    public PlayerChange(Player player) {
        
        this.player = player;
    }
    
    public String getUsername() {
        
        return player.getUsername();
    }
    
    public int getCoins() {
        
        return player.getCoins();
    }
    
    @Override
    public void apply(GameScene scene) {
        
        PlayerStatusCLIComponent cliPlayer = scene.getPlayer(player.getUsername());
        
        cliPlayer.setCoins(player.getCoins());
    }
    
    @Override
    public void apply(GameController controller) {
        
        Platform.runLater(() -> controller.getPlayerGUIComponent(player.getUsername()).setCoins(player.getCoins()));
    }
}
