package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Mage;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SchoolDashboardTest {

    private SchoolDashboard schoolDashboard;

    @BeforeEach
    void setUp() {
        Team.WHITE.reset();
        Team.BLACK.reset();
        int numberOfTowers = 8;
        Player player = new Player("player1", Team.BLACK, Mage.MAGE_1);
        this.schoolDashboard = new SchoolDashboard(player, numberOfTowers);
    }

    @Test
    void addToEntrance() {
        final ColoredPawn student = new ColoredPawn(PawnColor.BLUE);
        schoolDashboard.addToEntrance(student);
        assertThrows(RuntimeException.class, () -> schoolDashboard.addToEntrance(student));
        schoolDashboard.addToEntrance(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToEntrance(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToEntrance(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToEntrance(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToEntrance(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToEntrance(new ColoredPawn(PawnColor.BLUE));
        assertThrows(
                RuntimeException.class,
                () -> schoolDashboard.addToEntrance(new ColoredPawn(PawnColor.BLUE))
        );
    }

    @Test
    void removeFromEntrance() {
        final ColoredPawn student = new ColoredPawn(PawnColor.BLUE);
        assertThrows(NoSuchElementException.class, () -> schoolDashboard.removeFromEntrance(student));
        schoolDashboard.addToEntrance(student);
        final ColoredPawn studentToRemove = schoolDashboard.getEntranceStudents().get(0);
        assertEquals(student, studentToRemove);
        schoolDashboard.removeFromEntrance(studentToRemove);
        assertEquals(0, schoolDashboard.getEntranceStudents().size());
    }

    @Test
    void addToTable() {
        final ColoredPawn student = new ColoredPawn(PawnColor.BLUE);
        schoolDashboard.addToTable(student);
        assertThrows(RuntimeException.class, () -> schoolDashboard.addToTable(student));
        schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE));
        assertEquals(1, schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE)));
        assertEquals(0, schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE)));
        schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE));
        schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE));
        assertThrows(RuntimeException.class, () -> schoolDashboard.addToTable(new ColoredPawn(PawnColor.BLUE)));
    }

    @Test
    void removeFromTable() {
        final ColoredPawn student = new ColoredPawn(PawnColor.BLUE);
        assertThrows(RuntimeException.class, () -> schoolDashboard.removeFromTable(PawnColor.BLUE));
        schoolDashboard.addToTable(student);
        assertEquals(student, schoolDashboard.removeFromTable(PawnColor.BLUE));
        assertThrows(RuntimeException.class, () -> schoolDashboard.removeFromTable(PawnColor.RED));
    }

    @Test
    void containsProfessor() {
        assertFalse(schoolDashboard.containsProfessor(PawnColor.BLUE));
        schoolDashboard.addProfessor(new ColoredPawn(PawnColor.RED));
        assertTrue(schoolDashboard.containsProfessor(PawnColor.RED));
    }

    @Test
    void addProfessor() {
        final ColoredPawn professor = new ColoredPawn(PawnColor.BLUE);
        assertFalse(schoolDashboard.containsProfessor(PawnColor.BLUE));
        schoolDashboard.addProfessor(professor);
        assertTrue(schoolDashboard.containsProfessor(PawnColor.BLUE));
        assertThrows(RuntimeException.class, () -> schoolDashboard.addProfessor(professor));
    }

    @Test
    void removeProfessor() {
        assertThrows(NoSuchElementException.class, () -> schoolDashboard.removeProfessor(PawnColor.BLUE));
        final ColoredPawn professor = new ColoredPawn(PawnColor.BLUE);
        schoolDashboard.addProfessor(professor);
        assertEquals(professor, schoolDashboard.removeProfessor(PawnColor.BLUE));
        schoolDashboard.addProfessor(professor);
    }

    @Test
    void addTower() {
        assertThrows(RuntimeException.class, () -> schoolDashboard.addTower());
        schoolDashboard.removeTower();
        schoolDashboard.addTower();
        assertThrows(RuntimeException.class, () -> schoolDashboard.addTower());
    }

    @Test
    void removeTower() {
        schoolDashboard.removeTower();
        schoolDashboard.removeTower();
        schoolDashboard.removeTower();
        schoolDashboard.removeTower();
        schoolDashboard.removeTower();
        schoolDashboard.removeTower();
        schoolDashboard.removeTower();
        schoolDashboard.removeTower();
        assertThrows(RuntimeException.class, () -> schoolDashboard.removeTower());
    }
}