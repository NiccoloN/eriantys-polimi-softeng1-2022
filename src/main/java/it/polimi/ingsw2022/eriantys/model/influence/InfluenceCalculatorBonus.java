package it.polimi.ingsw2022.eriantys.model.influence;

import it.polimi.ingsw2022.eriantys.model.players.Player;

import java.util.ArrayList;

/**
 * This class represents the skill that gives an additional two influence points.
 * @author Emanuele Musto
 */
public class InfluenceCalculatorBonus extends InfluenceCalculatorBasic{

    InfluenceCalculatorBonus(){ super(); }

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
