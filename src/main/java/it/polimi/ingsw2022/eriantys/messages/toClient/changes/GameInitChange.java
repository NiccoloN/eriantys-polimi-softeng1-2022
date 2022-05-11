package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameInitChange implements Change, Serializable {

    private final List<CharacterCard> characterCards;
    private final List<HelperCard> helperCards;

    public GameInitChange() {

        characterCards = new ArrayList<>(3);
        helperCards = new ArrayList<>(10);
    }

    public CharacterCard[] getCharacterCards() {

        return characterCards.toArray(new CharacterCard[0]);
    }

    public void addCharacterCard(CharacterCard card) {

        characterCards.add(card);
    }

    public HelperCard[] getHelperCards() {

        return helperCards.toArray(new HelperCard[0]);
    }

    public void addHelperCard(HelperCard card) {

        helperCards.add(card);
    }

    @Override
    public void apply(View view) {

        view.applyChange(this);
    }
}
