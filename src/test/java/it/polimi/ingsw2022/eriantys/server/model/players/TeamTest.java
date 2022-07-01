package it.polimi.ingsw2022.eriantys.server.model.players;

import it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void testTeam() {

        assertEquals(blackTeam.getTeamName(), "black");

        Player test1 = new Player("player1", blackTeam);

        assertEquals(blackTeam.getSize(), 1);

        Player test2 = new Player("player2", blackTeam);

        assertThrows(RuntimeException.class, () -> blackTeam.addPlayer(new Player("player3", redTeam)));

        assertEquals(blackTeam.getSize(), 2);
        assertEquals(blackTeam.getPlayers(), List.of(test1, test2));

        assertEquals(blackTeam.getLeader(), test1);
        assertEquals(blackTeam.ansiColor, AnsiCodes.BLACK_BRIGHT);

        blackTeam.reset();
        assertEquals(blackTeam.getSize(), 0);
    }
}
