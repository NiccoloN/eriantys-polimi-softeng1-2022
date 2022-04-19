package it.polimi.ingsw2022.eriantys.model.influence;

import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.players.Player;
import it.polimi.ingsw2022.eriantys.model.players.Team;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This interface is used as a Strategy for various types of influence calculators.
 * @author Emanuele Musto
 */

public interface InfluenceCalculator {
    Optional<Team> calculateInfluence(ArrayList<Player> players, CompoundIslandTile island, Player currentPlayer);
}
