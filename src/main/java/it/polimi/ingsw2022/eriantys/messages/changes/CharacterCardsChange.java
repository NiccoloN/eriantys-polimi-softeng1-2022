package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CharacterCardsChange implements Change, Serializable {

    private final List<CharacterCard> characterCards;

    public CharacterCardsChange() {

        characterCards = new ArrayList<>(3);
    }

    public void addCharacterCard(CharacterCard card) {

        characterCards.add(card);
    }

    @Override
    public void apply(GameScene scene) {

        scene.setCharacters(characterCards);
    }
}
