package it.polimi.ingsw2022.eriantys.server.model.players;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Team blackTeam;
    private Team redTeam;

    @BeforeEach
    void setUp() {
        blackTeam = new Team("\u001b[30m", "black");
        redTeam = new Team("\\u001b[31m", "red");
    }

    @Test
    void addPlayer_getSize() {
        assertEquals(0, blackTeam.getSize());
        assertEquals(0, redTeam.getSize());
        Player player = new Player("p1", blackTeam, Mage.MAGE_1);
        assertEquals(1, blackTeam.getSize());
        assertThrows(RuntimeException.class, () -> blackTeam.addPlayer(player));
        new Player("p2", redTeam, Mage.MAGE_2);
        new Player("p3", redTeam, Mage.MAGE_2);
        assertEquals(2, redTeam.getSize());
        assertThrows(RuntimeException.class, () -> new Player("p4", redTeam, Mage.MAGE_3));
    }
}