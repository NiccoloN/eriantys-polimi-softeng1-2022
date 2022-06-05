package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.CloudChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;

/**
 * This class represents the choice of a Character by a player
 * @author Emanuele Musto
 */
public class ChooseCharacterCard extends Move {

    public final int characterCardIndex;

    public ChooseCharacterCard(int characterCardIndex) {

        this.characterCardIndex = characterCardIndex;
    }

    @Override
    public boolean isValid(Game game) {

        return game.getCurrentPlayer().getCoins() >= game.getCharacterOfIndex(characterCardIndex).getCost();
    }

    @Override
    public void apply(Game game) {
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        CharacterCardsChange change = new CharacterCardsChange();

        for(int i=0; i<game.getNumberOfCharacters(); i++){
            change.addCharacterCard(game.getCharacter(i));
        }

        update.addChange(change);

        return update;
    }
}
