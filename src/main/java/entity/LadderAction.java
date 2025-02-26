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
    // Hent boardet fra spilleren
    Board board = player.getBoard();  // Henter boardet fra spilleren

    // Hent destinationTile fra brettet ved hjelp av destinationTileId
    Tile currentTile = player.getCurrentTile();

    if (currentTile.getTileId() != destinationTileId) {
      System.out.println(player.getName() + " is currently on tile: " + currentTile.getTileId());
      System.out.println(player.getName() + " should move to tile: " + destinationTileId);

      Tile newTile = board.getTile(destinationTileId);
      if (newTile != null) {
        player.placeOnTile(newTile);
        System.out.println(player.getName() + " " + message);
      } else {
        System.out.println("Error: Destination tile " + destinationTileId + " does not exist!");
      }
    }
  }

  // Gettere for destinationTileId og message
  public int getDestinationTileId() {
    return destinationTileId;
  }

  public String getMessage() {
    return message;
  }
}
