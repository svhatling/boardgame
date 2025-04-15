package model.entity;

import model.exception.InvalidTileException;

public class Tile {
  private final int tileId;
  private TileAction tileAction;

  public Tile(int tileId) {
    if (tileId < 1) {
      throw new InvalidTileException("Tile ID cannot be negative.");
    }
    this.tileId = tileId;
  }

  public int getTileId() {
    return tileId;
  }


  public void setTileAction(TileAction action) {
    this.tileAction = action;
  }

  public void landPlayer(Player player) {
    if (tileAction != null) {
      tileAction.perform(player);
    }
  }
}