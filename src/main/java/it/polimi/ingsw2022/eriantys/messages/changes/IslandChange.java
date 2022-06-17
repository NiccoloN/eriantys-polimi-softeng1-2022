package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.board.IslandTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class IslandChange implements Change, Serializable {

    private final List<IslandTile> islandTiles;

    public IslandChange(List<IslandTile> islandTiles) {

        this.islandTiles = islandTiles;
    }

    @Override
    public void apply(GameScene scene) {

        for(int n = 0; n < islandTiles.size(); n++) setCliIslandTile(scene.getIsland(n), islandTiles.get(n));
    }

    private void setCliIslandTile(IslandCLIComponent cliIsland, IslandTile islandTile) {

        for(PawnColor color : PawnColor.values()) cliIsland.setStudents(color, islandTile.countStudents(color));
        cliIsland.setMother(islandTile.hasMotherNature());
        cliIsland.setTower(islandTile.hasTower());
        cliIsland.setIndex(islandTile.getIndex());
        if(islandTile.getTeam().isPresent()) cliIsland.setTeamColor(islandTile.getTeam().get().ansiColor);
    }
}
