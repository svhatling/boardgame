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
    System.out.println("Player is currently on tile: " + player.getCurrentTile().getTileId());

    // Hent destinationTile fra brettet ved hjelp av destinationTileId
    Tile destinationTile = board.getTile(destinationTileId);

    // Debugging: Sjekk om destinationTileId er riktig
    System.out.println("Destination tile ID: " + destinationTileId);

    // Flytt spilleren til destinationTile
    player.placeOnTile(destinationTile);

    // Skriver ut meldingen om at spilleren har klatret opp stigen
    System.out.println(message);
  }

  // Gettere for destinationTileId og message
  public int getDestinationTileId() {
    return destinationTileId;
  }

  public String getMessage() {
    return message;
  }
}
