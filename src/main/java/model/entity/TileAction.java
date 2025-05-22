package model.entity;

/**
 * This interface represents an action that can be performed on a tile in the game.
 */
public interface TileAction {
  void perform(Player player);

}
