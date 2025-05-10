package model.entity;

public class Player {
  private String name;
  private Tile currentTile;
  private final Board board;
  private String piece;
  private int score;

  // Konstruktør
  public Player(String name, Board board, String piece) {
    this.name = name;
    this.board = board;
    this.piece = piece;
    this.currentTile = board.getTile(1);
    this.score = 0;
  }

  // Metode for å hente navn
  public String getName() {
    return name;
  }

  public String getPiece() {return piece;}

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
    int targetTileId = Math.max(1, Math.min(currentTile.getTileId() + roll, 90));
    Tile targetTile = board.getTile(targetTileId);
    setCurrentTile(targetTile);
    targetTile.landPlayer(this);
  }

  public int getScore() {return score;}

  public void setScore(int score) {
    this.score = score;
  }

  public void incrementScore() {this.score++;}

  public boolean isCurrent() {
    return false;
  }
}
