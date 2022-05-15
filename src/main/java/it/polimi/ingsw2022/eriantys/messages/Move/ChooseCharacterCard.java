package it.polimi.ingsw2022.eriantys.messages.Move;

/**
 * This class represents the choice of a Character by a player
 * @author Emanuele Musto
 */
public class ChooseCharacterCard extends Move{

    int characterCardIndex;

    public ChooseCharacterCard(MoveType moveType, int characterCardIndex){
        this.moveType = moveType;
        this. characterCardIndex = characterCardIndex;
    }
}
