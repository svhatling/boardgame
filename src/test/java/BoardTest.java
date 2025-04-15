import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import model.entity.Board;
import model.entity.Tile;
import model.exception.BoardNotInitializedException;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class BoardTest {
  @Test
  public void testAddTile() {
    Board board = new Board();
    Tile tile = new Tile(1);
    board.addTile(tile);
    assertEquals(1, board.getTiles().size());
    assertEquals(tile, board.getTile(1));
  }

  @Test
  public void testAddTileDuplicateThrowsException() {
    Board board = new Board();
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(1);
    board.addTile(tile1);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> board.addTile(tile2));
    assertEquals("Tile with ID 1 already exists.", exception.getMessage());
  }

  @Test
  public void testGetTile() {
    Board board = new Board();
    Tile tile = new Tile(1);
    board.addTile(tile);
    assertEquals(tile, board.getTile(1));
  }

  @Test
  public void testGetTiles() {
    Board board = new Board();
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(2);
    board.addTile(tile1);
    board.addTile(tile2);
    Map<Integer, Tile> tiles = board.getTiles();
    assertEquals(2, tiles.size());
    assertEquals(tile1, tiles.get(1));
    assertEquals(tile2, tiles.get(2));
  }

  @Test
  public void testGetTileNotFoundThrowsException() {
    Board board = new Board();
    Exception exception = assertThrows(BoardNotInitializedException.class, () -> board.getTile(1));
    assertEquals("Tile with ID 1 not found. Map size: 0", exception.getMessage());
  }

}
