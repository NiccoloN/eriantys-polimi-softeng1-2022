package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HelperCardsChange implements Change, Serializable {

    private List<HelperCard> helperCards;

    public void HelperCardsChange(List<HelperCard> helperCards) {
        this.helperCards = helperCards;
    }

    public void HelperCardsChange() {
        this.helperCards = new ArrayList<>();
    }

    public void addHelperCards(List<HelperCard> helperCards) {
        this.helperCards.addAll(helperCards);
    }

    @Override
    public void apply(View view) {
    view.applyChange(this);
    }
}
