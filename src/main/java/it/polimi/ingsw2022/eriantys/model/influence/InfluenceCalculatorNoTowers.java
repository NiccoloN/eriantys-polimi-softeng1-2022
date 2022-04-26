package it.polimi.ingsw2022.eriantys.model.influence;

import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.players.Player;

/**
 * This class represents an influence calculator that calculates influences like a BasicInfluenceCalculator,
 * though without considering the contribution of towers
 * @author Emanuele Musto
 */
public class InfluenceCalculatorNoTowers extends InfluenceCalculatorBasic{
    /**
     * Doesn't consider the towers' influence
     * @param player one of the players
     * @param island the given compound island
     */
    @Override
    protected void towerInfluence(Player player, CompoundIslandTile island) {
    }
}
