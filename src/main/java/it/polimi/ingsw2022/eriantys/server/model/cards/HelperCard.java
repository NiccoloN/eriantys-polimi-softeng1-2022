package it.polimi.ingsw2022.eriantys.server.model.cards;

import it.polimi.ingsw2022.eriantys.server.model.players.Mage;

import java.io.Serializable;

/**
 * Card that's used to determine the player order in a turn and the movement of mother nature
 * @author Francesco Melegati Maccari
 */
public class HelperCard extends Card {
    public final int priority;
    public final int movement;
    public final Mage mage;

    HelperCard(int index, int priority, int movement, Mage mage) {
        super(index);
        this.priority = priority;
        this.movement = movement;
        this.mage = mage;
    }
}
