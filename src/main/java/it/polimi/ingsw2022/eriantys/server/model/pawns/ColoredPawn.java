package it.polimi.ingsw2022.eriantys.server.model.pawns;

import java.io.Serializable;

/**
 * This class represents a colored pawn
 * @author Emanuele Musto
 */
public class ColoredPawn implements Serializable {
    
    public final PawnColor color;
    
    public ColoredPawn(PawnColor color) {
        
        this.color = color;
    }
}
