package it.polimi.ingsw2022.eriantys.model;

import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.players.Player;

public interface InfluenceCalculator {
    int calculateInfluence(Player player, CompoundIslandTile island);
}
