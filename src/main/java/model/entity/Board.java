package model.entity;

import java.util.HashMap;
import java.util.Map;
import model.exception.BoardNotInitializedException;

/**
 * Represents a game board that contains tiles. The board is initialized with a set of tiles, and
 * each tile can be accessed by its ID.
 */
public class Board {

  private final Map<Integer, Tile> tiles;

  /**
   * Initializes a new Board instance with an empty set of tiles.
   */
  public Board() {
    tiles = new HashMap<>();
  }

  /**
   * Initializes a new Board instance with a given set of tiles.
   *
   * @param tile - tile that is added to the board.
   */
  public void addTile(Tile tile) {
    if (tiles.containsKey(tile.getTileId())) {
      throw new IllegalArgumentException("Tile with ID " + tile.getTileId() + " already exists.");
    }
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Retrieves a tile by its ID.
   *
   * @param tileId - the ID of the tile to retrieve.
   * @return the Tile object associated with the given ID.
   * @throws BoardNotInitializedException if the board is not initialized or the tile ID is
   *                                      invalid.
   */
  public Tile getTile(int tileId) {
    if (!tiles.containsKey(tileId)) {
      throw new BoardNotInitializedException(
          "Tile with ID " + tileId + " not found. Map size: " + tiles.size());
    }
    return tiles.get(tileId);
  }

  /**
   * Retrieves all tiles on the board.
   *
   * @return a map of all tiles, where the key is the tile ID and the value is the Tile object.
   */
  public Map<Integer, Tile> getTiles() {
    return tiles;
  }
}
