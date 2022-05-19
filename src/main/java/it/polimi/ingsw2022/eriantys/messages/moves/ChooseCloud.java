package it.polimi.ingsw2022.eriantys.messages.moves;

/**
 * This class represents the choice of one cloud by a player
 * @author Emanuele Musto
 */
public class ChooseCloud extends Move {

    int cloudIndex;

    public ChooseCloud(int cloudIndex) {
        this.moveType = MoveType.CHOOSE_CLOUD;
        this.cloudIndex = cloudIndex;
    }

}
