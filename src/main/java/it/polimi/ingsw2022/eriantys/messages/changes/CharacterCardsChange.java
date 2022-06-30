package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a change of the character cards. Whenever they change, the clients will be updated with this change.
 */
public class CharacterCardsChange implements Change, Serializable {
    
    private final List<CharacterCard> characterCards;
    
    public CharacterCardsChange() {
        
        characterCards = new ArrayList<>(3);
    }
    
    /**
     * Adds the character card to the change. The added character cards will be updated in the view.
     *
     * @param card the card to update.
     */
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
