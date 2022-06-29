package it.polimi.ingsw2022.eriantys.server.model.cards;

import java.io.Serializable;

/**
 * Generic card, with index as id
 * @author Francesco Melegati Maccari
 */
public class Card implements Serializable {
    
    public final int index;
    
    Card(int index) {
        
        this.index = index;
    }
}
