package it.polimi.ingsw2022.eriantys.server.model.cards;

/**
 * This classe represents a helper card
 * @author Francesco Melegati Maccari
 */
public class HelperCard extends Card {
    
    public final int priority;
    public final int movement;
    
    HelperCard(int index, int priority, int movement) {
        
        super(index);
        this.priority = priority;
        this.movement = movement;
    }
}
