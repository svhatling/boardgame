package model.entity;

import model.exception.BoardNotInitializedException;
import java.util.HashMap;
import java.util.Map;

public class Board {
  private Map<Integer, Tile> tiles;

  public Board() {
    tiles = new HashMap<>();
  }

  public void addTile(Tile tile) {
    if (tiles.containsKey(tile.getTileId())) {
      throw new IllegalArgumentException("Tile with ID " + tile.getTileId() + " already exists.");
    }
    tiles.put(tile.getTileId(), tile);
  }

  public Tile getTile(int tileId) {
    if (!tiles.containsKey(tileId)) {
      throw new BoardNotInitializedException("Tile with ID " + tileId + " not found. Map size: " + tiles.size());
    }
    return tiles.get(tileId);
  }

  public Map<Integer, Tile> getTiles() {
    return tiles;
  }
}
