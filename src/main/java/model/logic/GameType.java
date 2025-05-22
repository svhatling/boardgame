package model.logic;

public enum GameType {
  SNAKES_AND_LADDERS("Snakes & Ladders"),
  QUIZ("Quiz");

  private final String displayName;

  GameType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
