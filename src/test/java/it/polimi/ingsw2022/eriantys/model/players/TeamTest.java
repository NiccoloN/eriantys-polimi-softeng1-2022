package it.polimi.ingsw2022.eriantys.model.players;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @BeforeEach
    void setUp() {

         Team.WHITE.reset();
         Team.BLACK.reset();
         Team.GRAY.reset();
    }

    @Test
    void addPlayer_getSize() {

        assertEquals(0, Team.WHITE.getSize());
        assertEquals(0, Team.BLACK.getSize());
        assertEquals(0, Team.GRAY.getSize());
        Player player = new Player(Team.WHITE, Mage.MAGE_1);
        assertEquals(1, Team.WHITE.getSize());
        assertThrows(RuntimeException.class, () -> Team.WHITE.addPlayer(player));
        new Player(Team.WHITE, Mage.MAGE_2);
        assertEquals(2, Team.WHITE.getSize());
        assertThrows(RuntimeException.class, () -> new Player(Team.WHITE, Mage.MAGE_3));
    }
}