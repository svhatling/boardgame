package model.entity;

/**
 * This class represents an action that sends a player back to a specified tile on the board.
 * It implements the TileAction interface.
 */
public class LadderAction implements TileAction {

  private final int destinationTileId;
  private final String message;

  /**
   * Constructor for LadderAction.
   *
   * @param destinationTileId the ID of the tile to which the player will be sent
   * @param message a message to be displayed when the action is performed
   */
  public LadderAction(int destinationTileId, String message) {
    if (destinationTileId < 1) {
      throw new IllegalArgumentException("Destination tile ID cannot be negative or zero.");
    }
    this.destinationTileId = destinationTileId; //Keeps original ID
    this.message = message;
  }

  /**
   * Performs the ladder action by sending the player to the specified tile.
   * The player is limited to a maximum tile ID of 90.
   */
  @Override
  public void perform(Player player) {
    int finalTile = Math.min(destinationTileId, 90); // Limits the player to reach max 90
    player.setCurrentTile(player.getBoard().getTile(finalTile));
    System.out.println(player.getName() + " " + message);
  }
}