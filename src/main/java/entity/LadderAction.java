package entity;

public class LadderAction implements TileAction {

  private int destinationTileId;
  private String message;

  public LadderAction(int destinationTileId, String message) {
    if (destinationTileId < 1) {
      throw new IllegalArgumentException("Destination tile ID cannot be negative or zero.");
    }
    this.destinationTileId = destinationTileId; //Keeps original ID
    this.message = message;
  }

  @Override
  public void perform(Player player) {
    int finalTile = Math.min(destinationTileId, 90); // Limits the player to reach max 90
    player.setCurrentTile(player.getBoard().getTile(finalTile));
    System.out.println(player.getName() + " " + message);
  }
}