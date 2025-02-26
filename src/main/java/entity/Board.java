package entity;

import java.util.HashMap;
import java.util.Map;

public class Board {
  private Map<Integer, Tile> tiles;

  public Board() {
    this.tiles = new HashMap<>();
  }

  public void addTile(Tile tile) {
    // Add debug print to verify tiles are being added correctly
    System.out.println("Adding tile with ID: " + tile.getTileId());
    tiles.put(tile.getTileId(), tile);
  }

  public Tile getTile(int tileId) {
    // Check if the tile exists before returning it
    if (!tiles.containsKey(tileId)) {
      System.out.println("Tile with ID " + tileId + " not found in board map. Map size: " + tiles.size());
      return null;
    }
    return tiles.get(tileId);
  }

  public Map<Integer, Tile> getTiles() {
    return tiles;
  }
}
