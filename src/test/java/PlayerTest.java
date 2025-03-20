package entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

  private Board board;
  private Player player;

  @BeforeEach
  void setUp() {
    board = new Board();
    for (int i = 1; i <= 90; i++) {
      board.addTile(new Tile(i));
    }
    player = new Player("TestPlayer", board);
  }

  @Test
  void testPlayerStartsOnTileOne() {
    assertEquals(1, player.getCurrentTile().getTileId());
  }

  @Test
  void testPlayerMovesCorrectly() {
    player.move(5);
    assertEquals(6, player.getCurrentTile().getTileId());
  }

  @Test
  void testPlayerCannotMoveBeyondTile90() {
    player.move(100);
    assertEquals(90, player.getCurrentTile().getTileId());
  }

  @Test
  void testPlayerWithNegativeMove() {
    player.move(-3);
    assertEquals(1, player.getCurrentTile().getTileId());
  }

  @Test
  void testPlayerSetCurrentTile() {
    player.setCurrentTile(board.getTile(10));
    assertEquals(10, player.getCurrentTile().getTileId());
  }
}
