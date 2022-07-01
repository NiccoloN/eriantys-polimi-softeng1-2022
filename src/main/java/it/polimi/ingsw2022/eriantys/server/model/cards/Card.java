package it.polimi.ingsw2022.eriantys.server.model.cards;

import java.io.Serializable;

/**
 * This class represents a generic card having an index
 * @author Francesco Melegati Maccari
 */
public class Card implements Serializable {
    
    public final int index;
    
    Card(int index) {
        
        this.index = index;
    }
}
