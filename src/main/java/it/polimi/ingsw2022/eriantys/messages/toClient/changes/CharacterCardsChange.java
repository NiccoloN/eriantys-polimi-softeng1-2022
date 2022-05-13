package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CharacterCardsChange implements Change, Serializable {

    private final List<CharacterCard> characterCards;

    public CharacterCardsChange() {

        characterCards = new ArrayList<>(3);
    }

    public CharacterCard[] getCharacterCards() {

        return characterCards.toArray(new CharacterCard[0]);
    }

    public void addCharacterCard(CharacterCard card) {

        characterCards.add(card);
    }

    @Override
    public void apply(View view) {

        view.applyChange(this);
    }
}
