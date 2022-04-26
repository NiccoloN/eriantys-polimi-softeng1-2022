package it.polimi.ingsw2022.eriantys.model.board;

import it.polimi.ingsw2022.eriantys.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.model.players.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompoundIslandTileTest {

    private CompoundIslandTile compoundIsland;

    @BeforeEach
    void setup() {
        compoundIsland = new CompoundIslandTile();
    }

    @Test
    void mergeWithIsland() {
        assertThrows(RuntimeException.class, () -> compoundIsland.mergeWithIsland(compoundIsland));
        final CompoundIslandTile newCompoundIsland = new CompoundIslandTile();
        compoundIsland.mergeWithIsland(newCompoundIsland);
    }

    @Test
    void addStudent() {
        final ColoredPawn student = new ColoredPawn(PawnColor.BLUE);
        compoundIsland.addStudent(student);
        assertThrows(RuntimeException.class, () -> compoundIsland.addStudent(student));
    }

    @Test
    void setTeam() {
        compoundIsland.setTeam(Team.BLACK);
        compoundIsland.deny();
        assertThrows(RuntimeException.class, () -> compoundIsland.setTeam(Team.GRAY));
    }
}
