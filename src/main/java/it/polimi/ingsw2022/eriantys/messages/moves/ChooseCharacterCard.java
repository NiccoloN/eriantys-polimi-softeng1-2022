package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

/**
 * This class represents the choice of a Character by a player
 * @author Emanuele Musto
 */
public class ChooseCharacterCard extends Move {

    int characterCardIndex;

    public ChooseCharacterCard(int characterCardIndex) {
        super(MoveType.CHOOSE_CHARACTER_CARD);
        this. characterCardIndex = characterCardIndex;
    }

    @Override
    public void apply(Game game, String username) {

    }

    @Override
    public Update getUpdate(Game game) {
        return null;
    }
}
