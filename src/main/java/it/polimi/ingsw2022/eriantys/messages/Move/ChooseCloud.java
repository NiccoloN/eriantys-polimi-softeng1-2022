package it.polimi.ingsw2022.eriantys.messages.Move;

/**
 * This class represents the choice of one cloud by a player
 * @author Emanuele Musto
 */
public class ChooseCloud extends Move{

    int cloudIndex;

    public ChooseCloud(MoveType moveType, int cloudIndex){
        this.moveType = moveType;
        this.cloudIndex = cloudIndex;
    }

}
