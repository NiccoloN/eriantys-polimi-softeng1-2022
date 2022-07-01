package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardTest {
    
    private Board board;
    private List<Player> players;
    private Team blackTeam;
    private Team whiteTeam;
    
    @BeforeEach
    void setUp() {
        
        final List<Player> players = new ArrayList<>();
        Team blackTeam = new Team(AnsiCodes.BLACK_BRIGHT, "black");
        Team whiteTeam = new Team(AnsiCodes.WHITE_BRIGHT, "white");
        players.add(new Player("player1", blackTeam));
        players.add(new Player("player2", whiteTeam));
        this.players = players;
        this.blackTeam = blackTeam;
        this.whiteTeam = whiteTeam;
    }
    
    @Test
    void Board() {
        
        Team redTeam = new Team(AnsiCodes.RED, "red");
        Player toRemovePlayer = players.get(0);
        players.remove(toRemovePlayer);
        assertThrows(RuntimeException.class, () -> new Board(players));
        players.add(toRemovePlayer);
        players.add(new Player("player3", blackTeam));
        players.add(new Player("player4", whiteTeam));
        players.add(new Player("player5", redTeam));
        assertThrows(RuntimeException.class, () -> new Board(players));
    }
    
    @Test
    void mergeIslands() {
        
        board = new Board(players);
        assertThrows(RuntimeException.class, () -> board.mergeIslands(1, 1));
        assertThrows(RuntimeException.class, () -> board.mergeIslands(3, 1));
        assertThrows(RuntimeException.class, () -> board.mergeIslands(1, 5));
        assertThrows(RuntimeException.class, () -> board.mergeIslands(15, 5));
        assertThrows(RuntimeException.class, () -> board.mergeIslands(1, 50));
        board.mergeIslands(0, 11);
        board.mergeIslands(1, 2);
    }
    
    @Test
    void moveMotherNature() {
        
        board = new Board(players);
        assertEquals(3, board.moveMotherNature(3));
    }
}
