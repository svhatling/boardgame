package model.entity;

public class Player {
  private String name;
  private Tile currentTile;
  private final Board board;
  private String piece;
  private int score;
  private boolean isCurrent;

  // Constructor
  public Player(String name, Board board, String piece) {
    this.name = name;
    this.board = board;
    this.piece = piece;
    this.currentTile = board.getTile(1);
    this.score = 0;
  }

  // Method for getting name
  public String getName() {
    return name;
  }

  //Method for getting piece
  public String getPiece() {return piece;}

  // Method for getting current tile
  public Tile getCurrentTile() {
    return currentTile;
  }

  // Method for setting current tile
  public void setCurrentTile(Tile newTile) {
    this.currentTile = newTile;
  }

  // Method for getting board
  public Board getBoard() {
    return board;
  }

  // Method for moving piece to correct tile
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

  // TileAction sends message to the user
  public void sendMessage(String message) {
    System.out.println(message);
  }
}
