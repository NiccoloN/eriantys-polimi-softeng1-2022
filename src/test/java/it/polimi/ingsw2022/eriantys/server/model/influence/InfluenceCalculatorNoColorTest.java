package it.polimi.ingsw2022.eriantys.server.model.influence;

import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class InfluenceCalculatorNoColorTest extends InfluenceCalculatorBasicTest {

    private PawnColor ignoredColor;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        ignoredColor = PawnColor.YELLOW;
    }

    @Test
    @Override
    void studentsInfluenceTest() {

        InfluenceCalculatorNoColor influenceCalculator = new InfluenceCalculatorNoColor(ignoredColor);
        influenceCalculator.fillMap(players, currentPlayer);
        currentPlayer.setSchool(new SchoolDashboard(currentPlayer, 8));
        influenceCalculator.studentsInfluence(currentPlayer, island, currentPlayer);
        for(Player player : players) {
            assertEquals(influenceCalculator.playersInfluence.get(player), 0);
        }
        currentPlayer.getSchool().addProfessor(new ColoredPawn(PawnColor.YELLOW));
        for(int i = 0; i < 3; i++) island.addStudent(new ColoredPawn(PawnColor.YELLOW));

        influenceCalculator.studentsInfluence(currentPlayer, island, currentPlayer);
        assertEquals(influenceCalculator.playersInfluence.get(currentPlayer), 0);

        currentPlayer.getSchool().addProfessor(new ColoredPawn(PawnColor.RED));
        island.addStudent(new ColoredPawn(PawnColor.RED));

        influenceCalculator.studentsInfluence(currentPlayer, island, currentPlayer);
        assertEquals(influenceCalculator.playersInfluence.get(currentPlayer), 1);
    }
}