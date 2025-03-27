package entity;

import static org.junit.jupiter.api.Assertions.*;

import exception.InvalidTileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileTest {

  private Tile tile;
  private Player player;
  private Board board;
  private boolean actionPerformed;

  @BeforeEach
  void setUp() {
    board = new Board();
    board.addTile(new Tile(1));
    tile = new Tile(10);
    board.addTile(tile);
    player = new Player("TestPlayer", board, "testtest");
    actionPerformed = false;
  }

  @Test
  void testTileHasCorrectId() {
    assertEquals(10, tile.getTileId());
  }

  @Test
  void testPlayerLandsOnTileWithoutAction() {
    tile.landPlayer(player);
    assertEquals(1, player.getCurrentTile().getTileId());
  }

  @Test
  void testPlayerLandsOnTileWithAction() {
    TileAction testAction = p -> actionPerformed = true;
    tile.setTileAction(testAction);
    tile.landPlayer(player);
    assertTrue(actionPerformed);
  }

  @Test
  void testTileActionIsNull() {
    tile.setTileAction(null);
    tile.landPlayer(player);
    assertEquals(1, player.getCurrentTile().getTileId());
  }

  @Test
  void testNegativeTileIdNotAllowed() {
    Exception exception = assertThrows(InvalidTileException.class, () -> new Tile(-5));
    assertEquals("Tile ID cannot be negative.", exception.getMessage());
  }
}
