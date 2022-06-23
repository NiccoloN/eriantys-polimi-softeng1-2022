package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.components.IslandGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.GameController;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.board.IslandTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class IslandChange implements Change, Serializable {

    private final List<CompoundIslandTile> islands;
    private final List<IslandTile> islandTiles;

    public IslandChange(List<CompoundIslandTile> islands, List<IslandTile> islandTiles) {

        this.islands = islands;
        this.islandTiles = islandTiles;
    }

    @Override
    public void apply(GameScene scene) {

        scene.setCompoundIslands(islands);
        for(int n = 0; n < islandTiles.size(); n++) setCliIslandTile(scene.getIsland(n), islandTiles.get(n));
    }

    private void setCliIslandTile(IslandCLIComponent cliIsland, IslandTile islandTile) {

        for(PawnColor color : PawnColor.values()) cliIsland.setStudents(color, islandTile.countStudents(color));
        cliIsland.setMother(islandTile.hasMotherNature());
        cliIsland.setTower(islandTile.hasTower());
        cliIsland.setIndex(islandTile.getIndex());
        cliIsland.setDenyTiles(islandTile.getNumberOfDenyTiles());
        if(islandTile.getTeam().isPresent()) cliIsland.setTeamColor(islandTile.getTeam().get().ansiColor);
    }

    @Override
    public void apply(GameController controller) {

        for(int n = 0; n< islandTiles.size(); n++)
            setGuiIslandTile(controller.getIslandGUIComponentOfIndex(n), islandTiles.get(n));
    }

    private void setGuiIslandTile(IslandGUIComponent guiIsland, IslandTile islandTile) {

        for(PawnColor color : PawnColor.values()) guiIsland.setStudents(color, islandTile.countStudents(color));
        guiIsland.setMotherNature(islandTile.hasMotherNature());
        if(islandTile.getTeam().isPresent()) guiIsland.setTower(islandTile.hasTower(), islandTile.getTeam().get());
        guiIsland.setIslandIndex(islandTile.getIndex());
        guiIsland.setDenyTiles(islandTile.getNumberOfDenyTiles());
    }
}
