package controller;

import model.logic.GameType;

public class MainViewController {
  private GameType selectedGameType;

  public void selectGame(GameType gameType) {
    this.selectedGameType = gameType;
    System.out.println("Game selected: " + gameType);
  }

  public GameType getSelectedGameType() {
    return selectedGameType;
  }
}
