package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;

/**
 * This class represents the choice of a Character by a player
 * @author Emanuele Musto
 */
public class ChooseCharacterCard implements Move, Serializable {

    int characterCardIndex;

    public ChooseCharacterCard(int characterCardIndex) {

        this. characterCardIndex = characterCardIndex;
    }

    @Override
    public void apply(Game game, String playerUsername) {

    }

    @Override
    public Update getUpdate(Game game, String playerUsername) {
        return null;
    }
}
