package it.polimi.ingsw2022.eriantys.server.model.players;

import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.cards.CardFactory;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {

        Team.WHITE.reset();
        player = new Player("", Team.WHITE, Mage.MAGE_1);
    }

    @Test
    void addHelperCard() throws IOException {

        assertEquals(0, player.getNumberOfHelpers());
        HelperCard card1 = CardFactory.createHelperCard(1, player.mage);
        player.addHelperCard(card1);
        assertEquals(1, player.getNumberOfHelpers());
        assertEquals(card1, player.getHelperCard(0));
        assertThrows(RuntimeException.class, () -> player.addHelperCard(card1));
        assertThrows(RuntimeException.class, () -> player.addHelperCard(CardFactory.createHelperCard(1, player.mage)));
        HelperCard card2 = CardFactory.createHelperCard(2, player.mage);
        player.addHelperCard(card2);
        assertEquals(2, player.getNumberOfHelpers());
        assertEquals(card2, player.getHelperCard(1));
    }

    @Test
    void playHelperCard() throws IOException {

        assertNull(player.getCurrentHelper());
        HelperCard card = CardFactory.createHelperCard(1, player.mage);
        player.addHelperCard(card);
        player.playHelperCard(1);
        assertEquals(card, player.getCurrentHelper());
        assertThrows(NoSuchElementException.class, () -> player.playHelperCard(1));
        assertThrows(NoSuchElementException.class, () -> player.playHelperCard(2));
    }

    @Test
    void getHelperCard() throws IOException {

        HelperCard card1 = CardFactory.createHelperCard(1, player.mage);
        HelperCard card2 = CardFactory.createHelperCard(2, player.mage);
        player.addHelperCard(card1);
        player.addHelperCard(card2);
        assertEquals(card1, player.getHelperCard(0));
        assertEquals(card1, player.getHelperCard(0));
        assertEquals(card2, player.getHelperCard(1));
        assertThrows(IndexOutOfBoundsException.class, () -> player.getHelperCard(2));
    }

    @Test
    void getNumberOfHelpers() throws IOException {

        assertEquals(0, player.getNumberOfHelpers());
        player.addHelperCard(CardFactory.createHelperCard(2, player.mage));
        assertEquals(1, player.getNumberOfHelpers());
        player.addHelperCard(CardFactory.createHelperCard(3, player.mage));
        assertEquals(2, player.getNumberOfHelpers());
        player.playHelperCard(2);
        assertEquals(1, player.getNumberOfHelpers());
    }

    @Test
    void getCurrentHelper() throws IOException {

        assertNull(player.getCurrentHelper());
        player.addHelperCard(CardFactory.createHelperCard(2, player.mage));
        HelperCard card = CardFactory.createHelperCard(3, player.mage);
        player.addHelperCard(card);
        assertNull(player.getCurrentHelper());
        player.playHelperCard(3);
        assertEquals(card, player.getCurrentHelper());
    }

    @Test
    void getCoins_addCoin() {

        assertEquals(1, player.getCoins());
        player.addCoin();
        assertEquals(2, player.getCoins());
        player.addCoin();
        assertEquals(3, player.getCoins());
        player.payCoins(2);
        assertEquals(1, player.getCoins());
    }

    @Test
    void payCoins() {

        player.payCoins(1);
        assertEquals(0, player.getCoins());
        player.addCoin();
        assertThrows(RuntimeException.class, () -> player.payCoins(3));
    }

    @Test
    void getSchool_setSchool() {

        assertNull(player.getSchool());
        SchoolDashboard school = new SchoolDashboard(player, 8);
        player.setSchool(school);
        assertEquals(school, player.getSchool());
        assertThrows(RuntimeException.class, () -> player.setSchool(new SchoolDashboard(player, 8)));
    }
}