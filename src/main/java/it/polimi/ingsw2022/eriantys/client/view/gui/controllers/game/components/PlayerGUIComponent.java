package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.ImageFactory;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.security.InvalidParameterException;

public class PlayerGUIComponent {

    private final Label nameLabel;
    private final Label coinLabel;
    private final ImageView lastHelper;

    public PlayerGUIComponent(Group player, String username, int index) {

        ((ImageView) player.getChildren().get(0)).setImage(ImageFactory.magesImages.get(index + 1));
        ImageView coinImage = ((ImageView) player.getChildren().get(2));
        coinImage.setImage(ImageFactory.coinImage);

        nameLabel = (Label) player.getChildren().get(1);
        coinLabel = (Label) player.getChildren().get(3);
        lastHelper = (ImageView) player.getChildren().get(4);
        setName(username);

        if(EriantysClient.getInstance().getGameSettings().gameMode == GameMode.BASIC) {
            coinImage.setVisible(false);
            coinLabel.setVisible(false);
        }
    }

    public void setName(String name) {

        if(name.length() > EriantysServer.MAX_USERNAME_LENGTH) throw new InvalidParameterException("Name too long");
        nameLabel.setText(name);
    }

    public void setCoins(int coins) {

        if(coins < 0 || coins > 99) throw new InvalidParameterException("Coins must be between 0 and 99");
        coinLabel.setText("x" + coins);
    }

    public void setLastHelper(int helperIndex) {

        lastHelper.setImage(ImageFactory.helpersImages.get(helperIndex));
        lastHelper.setVisible(true);
    }
}
