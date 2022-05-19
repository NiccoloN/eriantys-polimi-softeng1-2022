package it.polimi.ingsw2022.eriantys.messages.moves;

/**
 * This class represents the choice of a helper card by a player
 * @author Emanuele Musto
 */
public class ChooseHelperCard extends Move {

    public final int helperCardIndex;

    public ChooseHelperCard(int helperCardIndex) {

        this.moveType = MoveType.CHOOSE_HELPER_CARD;
        this.helperCardIndex = helperCardIndex;
    }
}
