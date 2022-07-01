package it.polimi.ingsw2022.eriantys.server.model.players;

import it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeamTest {
    
    private Team blackTeam;
    private Team redTeam;
    
    @BeforeEach
    void setUp() {
        
        blackTeam = new Team(AnsiCodes.BLACK_BRIGHT, "black");
        redTeam = new Team(AnsiCodes.RED, "red");
    }
    
    @Test
    void addPlayer_getSize() {
        
        assertEquals(0, blackTeam.getSize());
        assertEquals(0, redTeam.getSize());
        Player player = new Player("p1", blackTeam);
        assertEquals(1, blackTeam.getSize());
        assertThrows(RuntimeException.class, () -> blackTeam.addPlayer(player));
        new Player("p2", redTeam);
        new Player("p3", redTeam);
        assertEquals(2, redTeam.getSize());
        assertThrows(RuntimeException.class, () -> new Player("p4", redTeam));
    }
}
