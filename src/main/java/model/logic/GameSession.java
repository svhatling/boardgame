package model.logic;

public class GameSession {
  private GameType selectedGame;
  private int numberOfPlayers;

  public GameSession(GameType selectedGame) {
    this.selectedGame = selectedGame;
  }

  public GameType getSelectedGame() {
    return selectedGame;
  }

  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }

  public void setNumberOfPlayers(int numberOfPlayers) {
    this.numberOfPlayers = numberOfPlayers;
  }

}
