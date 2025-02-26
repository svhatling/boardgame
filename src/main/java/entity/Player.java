package entity;

public class Player {
  private String name;
  private Tile currentTile;
  private Board board;

  // Legg til konstruktør for å sette navn, startfelt og board
  public Player(String name, Tile startTile, Board board) {
    this.name = name;
    this.currentTile = startTile;
    this.board = board;
  }

  // Metode for å hente spillerens navn
  public String getName() {
    return name;
  }

  public void placeOnTile(Tile tile) {
    this.currentTile = tile;
  }

  public Tile getCurrentTile() {
    return this.currentTile;
  }

  public Board getBoard() {
    return this.board;
  }

  public void move(int roll, Board board) {
    int currentTileId = this.currentTile.getTileId();
    int targetTileId = currentTileId + roll;

    // Finn det nye målfeltet
    Tile targetTile = board.getTile(targetTileId);

    // Hvis det er en TileAction på målfeltet, utfør handlingen (for eksempel stige)
    if (targetTile.getTileAction() != null) {
      targetTile.getTileAction().perform(this);  // Kun send Player til perform-metoden
    } else {
      this.placeOnTile(targetTile);
    }
  }
}
