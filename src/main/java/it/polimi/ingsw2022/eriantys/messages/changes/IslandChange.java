package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.board.IslandTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

/**
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class IslandChange implements Change, Serializable {

    private final int compoundIslandIndex;
    private final CompoundIslandTile island;

    public IslandChange(int compoundIslandIndex, CompoundIslandTile island) {

        this.compoundIslandIndex = compoundIslandIndex;
        this.island = island;
    }

    @Override
    public void apply(GameScene scene) {

        int oldIndex = 0;
        for(int n = 0; n < scene.getNumberOfIslands(); n++) {

            if(scene.getIsland(n).getIndex() == compoundIslandIndex) {

                for(int i = 0; i < island.getNumberOfTiles(); i++, n++) {

                    IslandCLIComponent cliIsland = scene.getIsland((n) % scene.getNumberOfIslands());
                    oldIndex = cliIsland.getIndex();

                    IslandTile islandTile = island.getTile(i);

                    for(PawnColor color : PawnColor.values()) cliIsland.setStudents(color, islandTile.countStudents(color));
                    cliIsland.setMother(islandTile.hasMotherNature());
                    cliIsland.setTower(islandTile.hasTower());
                    cliIsland.setIndex(compoundIslandIndex);

                    if(island.getTeam().isPresent()) cliIsland.setTeamColor(island.getTeam().get().ansiColor);
                }
                n--;
            }

            else if(scene.getIsland(n).getIndex() > compoundIslandIndex && oldIndex > compoundIslandIndex) {

                IslandCLIComponent cliIsland = scene.getIsland(n);
                cliIsland.setIndex(cliIsland.getIndex() - (oldIndex - compoundIslandIndex));
            }
        }
    }
}
