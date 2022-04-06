package it.polimi.ingsw2022.eriantys.model.cards;

import it.polimi.ingsw2022.eriantys.model.players.Mage;


/**
 * Card that's used to determine the turn order
 * @author Francesco Melegati Maccari
 */
public class HelperCard extends Card {
    public final int priority;
    public final int movement;
    public final Mage mage;

    public HelperCard(int index, int priority, int movement, Mage mage) {
        super(index);
        this.priority = priority;
        this.movement = movement;
        this.mage = mage;
    }
}
