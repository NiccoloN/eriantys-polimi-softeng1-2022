package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HelperCardsChange implements Change, Serializable {

    private List<HelperCard> helperCards;

    public HelperCardsChange() {
        this.helperCards = new ArrayList<>();
    }

    public void addHelperCard(HelperCard helperCard) {
        this.helperCards.add(helperCard);
    }

    public void addHelperCards(List<HelperCard> helperCards) {
        this.helperCards = new ArrayList<>(helperCards);
    }

    @Override
    public void apply(GameScene scene) {

        scene.setHelpers(helperCards);
    }
}
