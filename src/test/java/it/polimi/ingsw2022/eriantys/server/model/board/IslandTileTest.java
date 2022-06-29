package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTileTest {
    
    private IslandTile tile;
    
    @BeforeEach
    void setUp() {
        
        tile = new IslandTile();
    }
    
    @Test
    void countStudents() {
        
        assertEquals(0, tile.countStudents());
        assertEquals(0, tile.countStudents(PawnColor.RED));
        tile.addStudent(new ColoredPawn(PawnColor.RED));
        assertEquals(1, tile.countStudents());
        assertEquals(1, tile.countStudents(PawnColor.RED));
        tile.addStudent(new ColoredPawn(PawnColor.YELLOW));
        tile.addStudent(new ColoredPawn(PawnColor.BLUE));
        tile.addStudent(new ColoredPawn(PawnColor.RED));
        tile.addStudent(new ColoredPawn(PawnColor.RED));
        assertEquals(5, tile.countStudents());
        assertEquals(3, tile.countStudents(PawnColor.RED));
        assertEquals(1, tile.countStudents(PawnColor.YELLOW));
        assertEquals(1, tile.countStudents(PawnColor.BLUE));
    }
    
    @Test
    void containsStudent() {
        
        ColoredPawn student = new ColoredPawn(PawnColor.RED);
        assertFalse(tile.containsStudent(student));
        tile.addStudent(student);
        assertTrue(tile.containsStudent(student));
    }
    
    @Test
    void addStudent() {
        
        ColoredPawn student = new ColoredPawn(PawnColor.RED);
        assertEquals(0, tile.countStudents());
        assertFalse(tile.containsStudent(student));
        tile.addStudent(student);
        assertEquals(1, tile.countStudents());
        assertTrue(tile.containsStudent(student));
        assertThrows(RuntimeException.class, () -> tile.addStudent(student));
        ColoredPawn student2 = new ColoredPawn(PawnColor.YELLOW);
        tile.addStudent(student2);
        assertEquals(2, tile.countStudents());
        assertTrue(tile.containsStudent(student2));
        assertTrue(tile.containsStudent(student));
    }
}
