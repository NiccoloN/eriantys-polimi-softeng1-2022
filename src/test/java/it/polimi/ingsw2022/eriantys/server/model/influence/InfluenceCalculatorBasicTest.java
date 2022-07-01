package it.polimi.ingsw2022.eriantys.server.model.influence;

import it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfluenceCalculatorBasicTest {

    protected ArrayList<Player> players;
    protected CompoundIslandTile island;
    protected Player currentPlayer;
    protected Team blackTeam;
    protected Team whiteTeam;

    @BeforeEach
    void setUp() {

        final ArrayList<Player> players = new ArrayList<>();
        Team blackTeam = new Team(AnsiCodes.BLACK_BRIGHT, "black");
        Team whiteTeam = new Team(AnsiCodes.WHITE_BRIGHT, "white");
        players.add(new Player("player1", blackTeam));
        players.add(new Player("player2", whiteTeam));
        this.players = players;
        this.blackTeam = blackTeam;
        this.whiteTeam = whiteTeam;
        island = new CompoundIslandTile(1);
        currentPlayer = players.get(0);
    }

    @Test
    void fillMapTest() {

        InfluenceCalculatorBasic influenceCalculatorBasic = new InfluenceCalculatorBasic();
        influenceCalculatorBasic.fillMap(players, currentPlayer);
        for(Player player : players) {
            assertEquals(influenceCalculatorBasic.playersInfluence.get(player) , 0);
        }
        assertEquals(influenceCalculatorBasic.playersInfluence.size() , 2);
    }

    @Test
    void towerInfluenceTest() {

        InfluenceCalculatorBasic influenceCalculatorBasic = new InfluenceCalculatorBasic();
        influenceCalculatorBasic.fillMap(players, currentPlayer);
        influenceCalculatorBasic.towerInfluence(currentPlayer, island);
        for(Player player : players) {
            assertEquals(influenceCalculatorBasic.playersInfluence.get(player), 0);
        }
        island.setTeam(blackTeam);
        influenceCalculatorBasic.towerInfluence(currentPlayer, island);
        assertEquals(influenceCalculatorBasic.playersInfluence.get(currentPlayer), 1);
    }

    @Test
    void studentsInfluenceTest() {

        InfluenceCalculatorBasic influenceCalculatorBasic = new InfluenceCalculatorBasic();
        influenceCalculatorBasic.fillMap(players, currentPlayer);
        currentPlayer.setSchool(new SchoolDashboard(currentPlayer, 8));
        influenceCalculatorBasic.studentsInfluence(currentPlayer, island, currentPlayer);
        for(Player player : players) {
            assertEquals(influenceCalculatorBasic.playersInfluence.get(player), 0);
        }
        currentPlayer.getSchool().addProfessor(new ColoredPawn(PawnColor.RED));
        for(int i = 0; i < 3; i++) island.addStudent(new ColoredPawn(PawnColor.RED));

        influenceCalculatorBasic.studentsInfluence(currentPlayer, island, currentPlayer);
        assertEquals(influenceCalculatorBasic.playersInfluence.get(currentPlayer), 3);
    }

    @Test
    void getDominatorTest() {

        InfluenceCalculatorBasic influenceCalculatorBasic = new InfluenceCalculatorBasic();
        influenceCalculatorBasic.playersInfluence.put(players.get(0), 3);
        influenceCalculatorBasic.playersInfluence.put(players.get(1), 2);

        assertTrue(influenceCalculatorBasic.getDominator().isPresent());
        assertEquals(influenceCalculatorBasic.getDominator().get(), blackTeam);

        influenceCalculatorBasic.playersInfluence.put(players.get(0), 2);
        influenceCalculatorBasic.playersInfluence.put(players.get(1), 2);

        assertFalse(influenceCalculatorBasic.getDominator().isPresent());
    }

    @Test
    void calculateInfluence() {

        InfluenceCalculatorBasic influenceCalculatorBasic = new InfluenceCalculatorBasic();
        ArrayList<Player> test = new ArrayList<>();
        assertThrows(InvalidParameterException.class, () -> influenceCalculatorBasic.calculateInfluence(test, island, currentPlayer));

        players.get(0).setSchool(new SchoolDashboard(players.get(0), 8));
        players.get(1).setSchool(new SchoolDashboard(players.get(1), 8));

        players.get(0).getSchool().addProfessor(new ColoredPawn(PawnColor.RED));
        players.get(0).getSchool().addProfessor(new ColoredPawn(PawnColor.PINK));

        players.get(1).getSchool().addProfessor(new ColoredPawn(PawnColor.BLUE));
        players.get(1).getSchool().addProfessor(new ColoredPawn(PawnColor.YELLOW));

        for(int i = 0; i < 2; i++) island.addStudent(new ColoredPawn(PawnColor.RED));
        for(int i = 0; i < 1; i++) island.addStudent(new ColoredPawn(PawnColor.PINK));

        assertTrue(influenceCalculatorBasic.calculateInfluence(players, island, currentPlayer).isPresent());
        assertEquals(influenceCalculatorBasic.calculateInfluence(players, island, currentPlayer).get(), blackTeam);

        for(int i = 0; i < 2; i++) island.addStudent(new ColoredPawn(PawnColor.BLUE));
        for(int i = 0; i < 1; i++) island.addStudent(new ColoredPawn(PawnColor.YELLOW));

        assertFalse(influenceCalculatorBasic.calculateInfluence(players, island, currentPlayer).isPresent());

        island.addStudent(new ColoredPawn(PawnColor.YELLOW));

        assertTrue(influenceCalculatorBasic.calculateInfluence(players, island, currentPlayer).isPresent());
        assertEquals(influenceCalculatorBasic.calculateInfluence(players, island, currentPlayer).get(), whiteTeam);
    }
}