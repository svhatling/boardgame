package model.entity;

/**
 * This class represents a player in the game.
 */
public class Player {
  private final String name;
  private Tile currentTile;
  private final Board board;
  private final String piece;
  private int score;

  /**
   * Constructor for Player.
   *
   * @param name  the name of the player
   * @param board the board on which the player is playing
   * @param piece the piece representing the player
   */
  public Player(String name, Board board, String piece) {
    this.name = name;
    this.board = board;
    this.piece = piece;
    this.currentTile = board.getTile(1);
    this.score = 0;
  }

  /**
   * Getter for the player's name.
   *
   * @return the name of the player
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for the player's piece.
   *
   * @return the piece representing the player
   */
  public String getPiece() {return piece;}

  /**
   * Getter for the player's current tile.
   *
   * @return the current tile of the player
   */
  public Tile getCurrentTile() {
    return currentTile;
  }

  /**
   * Setter for the player's current tile.
   *
   * @param newTile the new tile to set as the current tile
   */
  public void setCurrentTile(Tile newTile) {
    this.currentTile = newTile;
  }

  /**
   * Getter for the board.
   *
   * @return the board on which the player is playing
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Moves the player to a new tile based on the roll of a die.
   *
   * @param roll the number rolled on the die
   */
  public void move(int roll) {
    int targetTileId = Math.max(1, Math.min(currentTile.getTileId() + roll, 90));
    Tile targetTile = board.getTile(targetTileId);
    setCurrentTile(targetTile);
    targetTile.landPlayer(this);
  }

  /**
   * Getter for the player's score.
   *
   * @return the score of the player
   */
  public int getScore() {return score;}

  /**
   * Setter for the player's score.
   *
   * @param score the new score to set
   */
  public void setScore(int score) {
    this.score = score;
  }

  /**
   * Increments the player's score by 1.
   */
  public void incrementScore() {this.score++;}

  /**
   * Prints a message to the console.
   *
   * @param message the message to print
   */
  public void sendMessage(String message) {
    System.out.println(message);
  }
}
