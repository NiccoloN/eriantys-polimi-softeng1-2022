package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.components;

import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import javafx.scene.Group;
import javafx.scene.control.Label;

import java.security.InvalidParameterException;

public class PlayerGUIComponent {

    private final Label nameLabel;
    private final Label coinLabel;

    public PlayerGUIComponent(Group player, String username) {

        nameLabel = (Label) player.getChildren().get(0);
        coinLabel = (Label) player.getChildren().get(2);
        setName(username);
    }

    public void setName(String name) {

        if(name.length() > EriantysServer.MAX_USERNAME_LENGTH) throw new InvalidParameterException("Name too long");
        nameLabel.setText(name);
    }

    public void setCoins(int coins) {

        if(coins < 0 || coins > 99) throw new InvalidParameterException("Coins must be between 0 and 99");
        coinLabel.setText("x" + coins);
    }
}