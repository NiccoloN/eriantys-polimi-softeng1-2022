package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.HelperCardCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.GameController;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public void addHelperCard(HelperCard helperCard) {
        this.helperCards.add(helperCard);
    }

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

        controller.getHelpersGUIComponent().setRemainingHelpers(helperCards);
    }
}
