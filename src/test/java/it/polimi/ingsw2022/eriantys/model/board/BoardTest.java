package it.polimi.ingsw2022.eriantys.model.board;

import it.polimi.ingsw2022.eriantys.model.players.Mage;
import it.polimi.ingsw2022.eriantys.model.players.Player;
import it.polimi.ingsw2022.eriantys.model.players.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

  private Board board;

  @BeforeEach
  void setUp() {
      final List<Player> players = new ArrayList<>();
      players.add(new Player(Team.BLACK, Mage.MAGE_1));
      players.add(new Player(Team.WHITE, Mage.MAGE_2));
      board = new Board(players, 0);
    }

    @Test
    void Board() {
      final List<Player> players = new ArrayList<>();
      players.add(new Player(Team.BLACK, Mage.MAGE_1));
      assertThrows(RuntimeException.class, () -> new Board(players, 0));
      players.add(new Player(Team.WHITE, Mage.MAGE_2));
      new Board(players, 0);
      assertThrows(RuntimeException.class, () -> new Board(players, 12));
      players.add(new Player(Team.WHITE, Mage.MAGE_3));
      players.add(new Player(Team.WHITE, Mage.MAGE_4));
      players.add(new Player(Team.WHITE, Mage.MAGE_2));
      assertThrows(RuntimeException.class, () -> new Board(players, 0));
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