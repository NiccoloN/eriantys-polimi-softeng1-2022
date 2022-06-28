package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.players.Mage;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import it.polimi.ingsw2022.eriantys.server.model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

  private Board board;
  private List<Player> players;
  private Team blackTeam;
  private Team whiteTeam;

  @BeforeEach
  void setUp() {

    final List<Player> players = new ArrayList<>();
    Team blackTeam = new Team("\\u001b[30m", "black");
    Team whiteTeam = new Team("\\u001b[37m", "white");
    players.add(new Player("player1", blackTeam, Mage.MAGE_1));
    players.add(new Player("player2", whiteTeam, Mage.MAGE_2));
    this.players = players;
    this.blackTeam = blackTeam;
    this.whiteTeam = whiteTeam;
  }

  @Test
  void Board() {
    Team redTeam = new Team("\\u001b[31m", "red");
    Player toRemovePlayer = players.get(0);
    players.remove(toRemovePlayer);
    assertThrows(RuntimeException.class, () -> new Board(players));
    players.add(toRemovePlayer);
    players.add(new Player("player3", blackTeam, Mage.MAGE_3));
    players.add(new Player("player4", whiteTeam, Mage.MAGE_4));
    players.add(new Player("player5", redTeam, Mage.MAGE_1));
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
