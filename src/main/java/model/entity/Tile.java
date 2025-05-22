package model.entity;

import model.exception.InvalidTileException;

/**
 * Represents a tile on the game board.
 * Each tile has an ID and can have an action associated with it.
 */
public class Tile {
  private final int tileId;
  private TileAction tileAction;

  /**
   * Constructor for Tile.
   *
   * @param tileId the ID of the tile
   * @throws InvalidTileException if the tile ID is negative
   */
  public Tile(int tileId) {
    if (tileId < 1) {
      throw new InvalidTileException("Tile ID cannot be negative.");
    }
    this.tileId = tileId;
  }

  /**
   * Gets the ID of the tile.
   *
   * @return the tile ID
   */
  public int getTileId() {
    return tileId;
  }

  /**
   * Setter for the tile action.
   */
  public void setTileAction(TileAction action) {
    this.tileAction = action;
  }

  /**
   * Uses the tile action interface to perform the action associated with the tile.
   *
   * @param player the player that is landing on the tile
   */
  public void landPlayer(Player player) {
    if (tileAction != null) {
      tileAction.perform(player);
    }
  }
}