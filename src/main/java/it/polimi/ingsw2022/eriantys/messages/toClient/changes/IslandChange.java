package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

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

    public final int islandTileIndex;
    private Integer compoundIslandIndex;
    private Boolean hasTower;
    private Boolean hasMotherNature;

    private Map<PawnColor, Integer> students;

    public IslandChange(int islandTileIndex) {

        this.islandTileIndex = islandTileIndex;
        students = new HashMap<>(1);
    }

    public IslandChange(int islandTileIndex, CompoundIslandTile island) {

        this(islandTileIndex);

        for (PawnColor color : PawnColor.values()) {
            setStudent(color, island.countStudents(color));
        }
        this.hasTower = island.countTowers() == 1;
        this.hasMotherNature = islandTileIndex == 0;
    }

    public Integer getCompoundIslandIndex() {

        return compoundIslandIndex;
    }

    public void setCompoundIslandIndex(Integer compoundIslandIndex) {
        this.compoundIslandIndex = compoundIslandIndex;
    }

    public Optional<Boolean> hasTower() {

        return Optional.of(hasTower);
    }

    public void setHasTower(Boolean hasTower) {
        this.hasTower = hasTower;
    }

    public Optional<Boolean> hasMotherNature() {

        return Optional.of(hasMotherNature);
    }

    public void setHasMotherNature(Boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public OptionalInt getStudents(PawnColor color) {

        return OptionalInt.of(students.get(color));
    }

    public void setStudent(PawnColor color, int numberOfStudents) {
        students.put(color, numberOfStudents);
    }

    @Override
    public void apply(View view) {

        view.applyChange(this);
    }
}
