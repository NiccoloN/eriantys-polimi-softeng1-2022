package it.polimi.ingsw2022.eriantys.messages.moves;

/**
 * This class represents the choice of a Character by a player
 * @author Emanuele Musto
 */
public class ChooseCharacterCard extends Move {

    int characterCardIndex;

    public ChooseCharacterCard(int characterCardIndex) {
        this.moveType = MoveType.CHOOSE_CHARACTER_CARD;
        this. characterCardIndex = characterCardIndex;
    }
}
