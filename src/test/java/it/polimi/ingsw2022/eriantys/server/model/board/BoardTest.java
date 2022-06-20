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

  @BeforeEach
  void setUp() {

    //TODO
    /*Team.WHITE.reset();
    Team.BLACK.reset();
      final List<Player> players = new ArrayList<>();
      players.add(new Player("player1", Team.BLACK, Mage.MAGE_1));
      players.add(new Player("player2", Team.WHITE, Mage.MAGE_2));
      this.board = new Board(players);*/
  }

  @Test
  void Board() {

    /*final List<Player> players = new ArrayList<>();
    players.add(new Player("player3", Team.BLACK, Mage.MAGE_3));
    assertThrows(RuntimeException.class, () -> new Board(players));
    players.add(new Player("player4", Team.WHITE, Mage.MAGE_4));
    new Board(players);
    assertThrows(RuntimeException.class, () -> players.add(new Player("player5", Team.BLACK, Mage.MAGE_1)));*/
  }

  @Test
  void mergeIslands() {
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
    assertEquals(3, board.moveMotherNature(3));
  }
}
