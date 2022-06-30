package it.polimi.ingsw2022.eriantys.server.model.influence;

import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This interface represents an influence calculator. The purpose of an influence calculator is to find the player
 * with the greatest influence on a given island, following a specific calculation strategy
 *
 * @author Emanuele Musto
 */
public interface InfluenceCalculator {
    
    /**
     * Calculates the influence of every given player on the given island
     *
     * @param players       the players in the game
     * @param island        the given island on which calculations are done
     * @param currentPlayer the player currently playing
     * @return an optional of the team containing the player with the most influence. Empty if there is a tie
     * @throws java.security.InvalidParameterException when the array of players is empty
     */
    Optional<Team> calculateInfluence(ArrayList<Player> players, CompoundIslandTile island, Player currentPlayer);
}
