package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.board.IslandTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.util.Map;


public class IslandChange implements Change {

    private final int islandTileIndex;
    private  Integer compoundIslandIndex;
    private  Boolean hasTower;
    private  Boolean hasMotherNature;

    private Map<PawnColor, Integer> students;

    public IslandChange(int islandTileIndex) {
        this.islandTileIndex = islandTileIndex;
    }

    public IslandChange(int islandTileIndex, IslandTile island) {
        this.islandTileIndex = islandTileIndex;
        for (PawnColor color : PawnColor.values()) {
            setStudent(color, island.countStudents(color));
        }
        this.hasTower = island.hasTower();
        this.hasMotherNature = islandTileIndex == 0;
    }

    public void setCompoundIslandIndex(Integer compoundIslandIndex) {
        this.compoundIslandIndex = compoundIslandIndex;
    }

    public void setHasTower(Boolean hasTower) {
        this.hasTower = hasTower;
    }

    public void setHasMotherNature(Boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public void setStudent(PawnColor color, int numberOfStudents) {
        students.put(color, numberOfStudents);
    }

    @Override
    public void apply(View view) {

    }
}
