package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HelperCardsChange implements Change, Serializable {

    private final List<HelperCard> helperCards;

    public HelperCardsChange() {
        this.helperCards = new ArrayList<>();
    }

    public HelperCard[] getHelperCards() {
        return helperCards.toArray(new HelperCard[0]);
    }

    public void addHelperCard(HelperCard helperCard) {
        this.helperCards.add(helperCard);
    }

    @Override
    public void apply(View view) {
    view.applyChange(this);
    }
}
