package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.components;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.security.InvalidParameterException;

public class PlayerGUIComponent {

    private final Label nameLabel;
    private final Label coinLabel;

    public PlayerGUIComponent(Group player, String username, int index) {

        ((ImageView) player.getChildren().get(0)).setImage(ImageFactory.magesImages.get(index + 1));
        ((ImageView) player.getChildren().get(2)).setImage(ImageFactory.coinImage);

        nameLabel = (Label) player.getChildren().get(1);
        coinLabel = (Label) player.getChildren().get(3);
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
