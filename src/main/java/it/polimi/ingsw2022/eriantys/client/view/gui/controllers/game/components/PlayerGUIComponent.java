package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.Images;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.security.InvalidParameterException;

/**
 * This class represents a player gui component. A player gui component is associated to a javafx Group that represents a player,
 * to the username and to the index of the player it represents. This component manages various graphic elements to represent the
 * current state of the player and attaches listeners to them in order to detect inputs and react accordingly
 * @author Niccolò Nicolosi
 */
public class PlayerGUIComponent {
    
    private final Label nameLabel;
    private final Label coinLabel;
    private final ImageView lastHelper;
    
    /**
     * Constructs a player gui component
     * @param playerGroup the javafx group associated to this component
     * @param username    the username of the player associated to this component
     * @param index       the index of the player associated to this component
     */
    public PlayerGUIComponent(Group playerGroup, String username, int index) {
        
        ((ImageView) playerGroup.getChildren().get(0)).setImage(Images.magesImages.get(index + 1));
        ImageView coinImage = ((ImageView) playerGroup.getChildren().get(2));
        coinImage.setImage(Images.coinImage);
        
        nameLabel = (Label) playerGroup.getChildren().get(1);
        coinLabel = (Label) playerGroup.getChildren().get(3);
        lastHelper = (ImageView) playerGroup.getChildren().get(4);
        setUsername(username);
        
        if (EriantysClient.getInstance().getGameSettings().gameMode == GameMode.BASIC) {
            coinImage.setVisible(false);
            coinLabel.setVisible(false);
        }
    }
    
    /**
     * Sets the number of coins to visualize on this component
     * @param coins the number of coins to visualize
     */
    public void setCoins(int coins) {
        
        if (coins < 0 || coins > 99) throw new InvalidParameterException("Coins must be between 0 and 99");
        coinLabel.setText("x" + coins);
    }
    
    /**
     * Sets the helper card to visualize as last-played on this component
     * @param helperIndex the index of the helper card to visualize
     */
    public void setLastHelper(int helperIndex) {
        
        lastHelper.setImage(Images.helpersImages.get(helperIndex));
        lastHelper.setVisible(true);
    }
    
    private void setUsername(String name) {
        
        if (name.length() > EriantysServer.MAX_USERNAME_LENGTH) throw new InvalidParameterException("Name too long");
        nameLabel.setText(name);
    }
}
