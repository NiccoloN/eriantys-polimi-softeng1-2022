package it.polimi.ingsw2022.eriantys.model.influence;

import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.players.Player;

/**
 * This class is used when the towers must not be included in the influence because of one character's skill
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
