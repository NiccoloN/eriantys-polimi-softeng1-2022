package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.HelperCardCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a change of the helper cards. Whenever they change, the clients will be updated with this change.
 */
public class HelperCardsChange implements Change, Serializable {

    private final String playerUsername;
    private HelperCard playedHelperCard;
    private List<HelperCard> helperCards;

    public HelperCardsChange(String playerUsername) {

        this.playerUsername = playerUsername;
        this.helperCards = new ArrayList<>();
    }

    public void setPlayedHelperCard(HelperCard playedHelperCard) {

        this.playedHelperCard = playedHelperCard;
    }

    /**
     * Adds a helper card to this change.
     * @param helperCard the changed helper card.
     */
    public void addHelperCard(HelperCard helperCard) {
        this.helperCards.add(helperCard);
    }

    /**
     * Adds a list of helper cards to this change.
     * @param helperCards the list of changed helper cards.
     */
    public void addHelperCards(List<HelperCard> helperCards) {
        this.helperCards = new ArrayList<>(helperCards);
    }

    @Override
    public void apply(GameScene scene) {

        if(EriantysClient.getInstance().getUsername().equals(playerUsername)) scene.setHelpers(helperCards);

        if(playedHelperCard != null)
            scene.getPlayer(playerUsername).setLastHelperCLIComponent(
                    new HelperCardCLIComponent(playedHelperCard.index, playedHelperCard.priority, playedHelperCard.movement));
    }

    @Override
    public void apply(GameController controller) {

        if(EriantysClient.getInstance().getUsername().equals(playerUsername))
            Platform.runLater(() -> controller.getHelpersGUIComponent().setRemainingHelpers(helperCards));

        if(playedHelperCard != null)
            Platform.runLater(() -> controller.getPlayerGUIComponent(playerUsername).setLastHelper(playedHelperCard.index));
    }
}
