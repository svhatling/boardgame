package model.logic;

/**
 * Enum representing the types of games available.
 * Each game type has a display name for user-friendly representation.
 */
public enum GameType {
  SNAKES_AND_LADDERS("Snakes & Ladders"),
  QUIZ("Quiz");

  private final String displayName;

  /**
   * Constructor for GameType enum.
   *
   * @param displayName the display name of the game type
   */
  GameType(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Returns the display name of the game type.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }
}
