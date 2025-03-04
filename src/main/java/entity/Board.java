package entity;

import java.util.HashMap;
import java.util.Map;

public class Board {
  private Map<Integer, Tile> tiles;

  public Board() {
    tiles = new HashMap<>();
  }

  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  public Tile getTile(int tileId) {
    return tiles.getOrDefault(tileId, new Tile(tileId));
  }

  public Map<Integer, Tile> getTiles() {
    return tiles;
  }
}
