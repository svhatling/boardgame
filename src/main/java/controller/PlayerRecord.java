package controller;

/**
 * Class that represents a player record with a name and a piece.
 */
public class PlayerRecord {
  public final String name;
  public final String piece;

  /**
   * Constructor for PlayerRecord.
   *
   * @param name  the name of the player
   * @param piece the piece of the player
   */
  public PlayerRecord(String name, String piece) {
    this.name = name;
    this.piece = piece;
  }

}
