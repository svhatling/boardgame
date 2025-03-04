package entity;

public class Player {
  private String name;
  private Tile currentTile;
  private final Board board;

  // Konstruktør
  public Player(String name, Board board) {
    this.name = name;
    this.board = board;
    this.currentTile = board.getTile(1);
  }

  // Metode for å hente navn
  public String getName() {
    return name;
  }

  // Henter nåværende tile
  public Tile getCurrentTile() {
    return currentTile;
  }

  // Oppdaterer nåværende tile riktig
  public void setCurrentTile(Tile newTile) {
    this.currentTile = newTile;
  }

  public Board getBoard() {
    return board;
  }

  public void move(int roll) {
    int targetTileId = Math.min(currentTile.getTileId() + roll, 90);
    Tile targetTile = board.getTile(targetTileId);
    setCurrentTile(targetTile);
    targetTile.landPlayer(this);
  }
}
