package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.GameController;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import javafx.application.Platform;

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

    @Override
    public void apply(GameController controller) {

        Platform.runLater(() -> controller.setCharacters(characterCards));
    }
}
