package it.polimi.ingsw2022.eriantys.messages.Move;

/**
 * This class represents the choice of a helper card by a player
 * @author Emanuele Musto
 */
public class ChooseHelperCard extends Move{

    public final int helperCardIndex;

    public ChooseHelperCard(MoveType moveType, int helperCardIndex){

        this.helperCardIndex = helperCardIndex;
        this.moveType = moveType;
    }
}
