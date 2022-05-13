package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.board.IslandTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class IslandChange implements Change, Serializable {

    public final int compoundIslandIndex;
    private final CompoundIslandTile island;

    public IslandChange(int compoundIslandIndex, CompoundIslandTile island) {

        this.compoundIslandIndex = compoundIslandIndex;
        this.island = island;
    }

    public int getSize() {

        return island.getSize();
    }

    public IslandTile getIslandTile(int index) {

        return island.getTile(index);
    }

    public Optional<Team> getTeam() {

        return island.getTeam();
    }

    @Override
    public void apply(View view) {

        view.applyChange(this);
    }
}
