package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTileTest {
    
    private CloudTile tile;
    
    @BeforeEach
    void setUp() {
        
        tile = new CloudTile();
    }
    
    @Test
    void addStudent() {
        
        final ColoredPawn student = new ColoredPawn(PawnColor.PINK);
        assertTrue(tile.isEmpty());
        tile.addStudent(student);
        assertFalse(tile.isEmpty());
        assertThrows(RuntimeException.class, () -> tile.addStudent(student));
        tile.addStudent(new ColoredPawn(PawnColor.BLUE));
        tile.addStudent(new ColoredPawn(PawnColor.GREEN));
        tile.addStudent(new ColoredPawn(PawnColor.GREEN));
        assertThrows(RuntimeException.class, () -> tile.addStudent(new ColoredPawn(PawnColor.BLUE)));
    }
    
    @Test
    void withdrawStudents() {
        
        assertEquals(0, tile.withdrawStudents().size());
        assertTrue(tile.isEmpty());
        tile.addStudent(new ColoredPawn(PawnColor.BLUE));
        tile.addStudent(new ColoredPawn(PawnColor.GREEN));
        assertFalse(tile.isEmpty());
        assertEquals(2, tile.withdrawStudents().size());
        assertTrue(tile.isEmpty());
    }
    
    @Test
    void isEmpty() {
        
        assertTrue(tile.isEmpty());
        tile.addStudent(new ColoredPawn(PawnColor.BLUE));
        tile.addStudent(new ColoredPawn(PawnColor.GREEN));
        assertFalse(tile.isEmpty());
    }
}
