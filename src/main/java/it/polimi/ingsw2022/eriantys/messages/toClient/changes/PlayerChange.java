package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
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
    public void apply(View view) {

        view.applyChange(this);
    }
}
