package model.entity;

/**
 * This class represents an action that sends a player back to a specified tile on the board.
 * It implements the TileAction interface.
 */
public class BackToStartAction implements TileAction {
  private final int destinationTileId;
  private final String message;

  /**
   * Constructor for BackToStartAction.
   *
   * @param destinationTileId the ID of the tile to which the player will be sent back
   * @param message           the message to be displayed to the player
   */
  public BackToStartAction(int destinationTileId, String message) {
    this.destinationTileId = destinationTileId;
    this.message = message;
  }

  /**
   * Gets the ID of the destination tile.
   *
   * @param player the player to which the action is performed
   */
  @Override
  public void perform(Player player) {
    player.setCurrentTile(player.getBoard().getTile(destinationTileId));
    player.sendMessage(message);
  }
}
