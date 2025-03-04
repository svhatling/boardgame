package entity;

public class LadderAction implements TileAction {

  private int destinationTileId;
  private String message;

  // Konstruktør for LadderAction
  public LadderAction(int destinationTileId, String message) {
    this.destinationTileId = destinationTileId;
    this.message = message;
  }

  // Implementering av perform-metoden fra TileAction
  @Override
  public void perform(Player player) {
    player.setCurrentTile(player.getBoard().getTile(destinationTileId));
    System.out.println(player.getName() + " " + message);
  }
}

