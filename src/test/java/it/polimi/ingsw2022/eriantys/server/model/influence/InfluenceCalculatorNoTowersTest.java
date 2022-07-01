package it.polimi.ingsw2022.eriantys.server.model.influence;

import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfluenceCalculatorNoTowersTest extends InfluenceCalculatorBasicTest {

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
    }

    @Test
    @Override
    void towerInfluenceTest() {

        InfluenceCalculatorNoTowers influenceCalculator = new InfluenceCalculatorNoTowers();
        influenceCalculator.fillMap(players, currentPlayer);
        influenceCalculator.towerInfluence(currentPlayer, island);
        for(Player player : players) {
            assertEquals(influenceCalculator.playersInfluence.get(player), 0);
        }
        island.setTeam(blackTeam);
        influenceCalculator.towerInfluence(currentPlayer, island);
        for(Player player : players) {
            assertEquals(influenceCalculator.playersInfluence.get(player), 0);
        }

        island.setTeam(whiteTeam);
        influenceCalculator.towerInfluence(currentPlayer, island);
        for(Player player : players) {
            assertEquals(influenceCalculator.playersInfluence.get(player), 0);
        }
    }
}