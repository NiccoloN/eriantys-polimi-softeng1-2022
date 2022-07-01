package it.polimi.ingsw2022.eriantys.server.model.influence;

import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfluenceCalculatorBonusTest extends InfluenceCalculatorBasicTest {

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
    }

    @Test
    @Override
    void fillMapTest() {

        InfluenceCalculatorBonus influenceCalculatorBonus = new InfluenceCalculatorBonus();
        influenceCalculatorBonus.fillMap(players, currentPlayer);
        for(Player player : players) {
            if(player.equals(currentPlayer))
                assertEquals(influenceCalculatorBonus.playersInfluence.get(player) , 2);
            else
                assertEquals(influenceCalculatorBonus.playersInfluence.get(player) , 0);
        }
        assertEquals(influenceCalculatorBonus.playersInfluence.size() , 2);
    }

}