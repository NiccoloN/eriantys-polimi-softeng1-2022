package it.polimi.ingsw2022.eriantys.server.model;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game;

    @BeforeEach
    void setUp() throws IOException {
        String[] usernames = {"Macca", "Ema"};
        game = new Game(usernames);
    }

    @Test
    void Game() {
        String[] notManyUsernames = {"Macca"};
        String[] tooManyUsernames = {"Macca", "Ema", "Nick", "Test1", "Test2"};
        assertThrows(RuntimeException.class, () ->new Game(notManyUsernames));
        assertThrows(RuntimeException.class, () ->new Game(tooManyUsernames));

        assertFalse(game.getStudentsBag().isEmpty());
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    void getTeam() {
        assertThrows(NoSuchElementException.class, () -> {
           game.getTeam("nonExistingTeam");
        });

        game.getTeam("BLACK");
    }

    @Test
    void checkAndUpdateProfessor() {
        Player winnerPlayer = game.getPlayers().get(0);
        assertFalse(winnerPlayer.getSchool().containsProfessor(PawnColor.PINK));
        winnerPlayer.getSchool().addToTable(new ColoredPawn(PawnColor.PINK));
        game.checkAndUpdateProfessor(PawnColor.PINK, false);
        assertTrue(winnerPlayer.getSchool().containsProfessor(PawnColor.PINK));
    }

    @Test
    void getProfessor() {
        game.getProfessor(PawnColor.PINK);
        assertThrows(NoSuchElementException.class, () -> game.getProfessor(PawnColor.PINK));
    }

    @Test
    void checkWinner() {
        Player winnerPlayer = game.getPlayers().get(0);
        Player otherPlayer = game.getPlayers().get(1);

        winnerPlayer.getSchool().removeTower();
        assertEquals(winnerPlayer.team, game.checkWinner());

        otherPlayer.getSchool().removeTower();
        winnerPlayer.getSchool().addProfessor(new ColoredPawn(PawnColor.PINK));
        assertEquals(winnerPlayer.team, game.checkWinner());
    }
}