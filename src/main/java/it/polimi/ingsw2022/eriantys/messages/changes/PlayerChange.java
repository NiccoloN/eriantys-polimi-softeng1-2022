package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player.PlayerStatusCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.Serializable;

public class PlayerChange implements Change, Serializable {

    private final Player player;

    public PlayerChange(Player player) {

        this.player = player;
    }

    public String getUsername() {

        return player.username;
    }

    public int getCoins() {

        return player.getCoins();
    }

    @Override
    public void apply(GameScene scene) {

        PlayerStatusCLIComponent cliPlayer = scene.getPlayer(player.username);

        cliPlayer.setCoins(player.getCoins());
    }
}
