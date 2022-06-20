package it.polimi.ingsw2022.eriantys.server.model.influence;

import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.util.ArrayList;

/**
 * This class represents an influence calculator that calculates influences just like the basic,
 * except for adding 2 more points to the current player
 * @author Emanuele Musto
 * @see InfluenceCalculatorBasic
 */
public class InfluenceCalculatorBonus extends InfluenceCalculatorBasic {

    public InfluenceCalculatorBonus(){ super(); }

    /**
     * Initializes the key as the players, and the influence as 0, except for the current player that gets the bonus
     * @param players the list of players
     * @param currentPlayer the acting player in this turn that gets 2 additional points
     */
    @Override
    protected void fillMap(ArrayList<Player> players, Player currentPlayer) {
        for(Player player : players){
            if(player.equals(currentPlayer)) { playersInfluence.put(player,2); }
            else { playersInfluence.put(player,0); }
        }
    }
}
