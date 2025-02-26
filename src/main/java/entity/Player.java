package entity;

public class Player {
  private String name;
  private Tile currentTile;
  private Board board;

  // Konstruktør
  public Player(String name, Tile startTile, Board board) {
    this.name = name;
    this.currentTile = startTile;
    this.board = board;
  }

  // Metode for å hente navn
  public String getName() {
    return name;
  }

  // Oppdaterer spillerens posisjon
  public void placeOnTile(Tile tile) {
    this.currentTile = tile;
  }

  // Henter nåværende tile
  public Tile getCurrentTile() {
    return currentTile;
  }

  // Henter brett
  public Board getBoard() {
    return this.board;
  }

  // Oppdaterer nåværende tile riktig
  public void setCurrentTile(Tile newTile) {
    this.currentTile = newTile;
  }

  public void move(int roll, Board board) {
    int currentTileId = this.currentTile.getTileId();
    int targetTileId = currentTileId + roll;

    if (targetTileId > 90) {
      targetTileId = 90; // Begrens til siste tile
    }

    Tile targetTile = board.getTile(targetTileId);

    // Flytt spilleren først, utfør så eventuell action
    this.placeOnTile(targetTile);

    // Hvis det er en TileAction, utfør den
    if (targetTile.getTileAction() != null) {
      targetTile.getTileAction().perform(this);
    }
  }
}
