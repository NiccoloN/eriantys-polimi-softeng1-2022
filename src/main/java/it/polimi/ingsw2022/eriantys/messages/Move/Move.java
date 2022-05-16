package it.polimi.ingsw2022.eriantys.messages.Move;

import java.io.Serializable;

/**
 * This class represents a generic Move done by a player
 * @author Emanuele Musto
 */
public abstract class Move implements Serializable {
    public MoveType moveType;
}
