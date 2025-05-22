package model.entity;

public class BackToStartAction implements TileAction {
  private final int destinationTileId;
  private final String message;

  public BackToStartAction(int destinationTileId, String message) {
    this.destinationTileId = destinationTileId;
    this.message = message;
  }

  @Override
  public void perform(Player player) {
    player.setCurrentTile(player.getBoard().getTile(destinationTileId));
    player.sendMessage(message);
  }
}
