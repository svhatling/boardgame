package controller;

import model.logic.GameType;

/**
 * MainViewController is responsible for handling the main view of the game.
 * It allows the user to select a game type and manages the game state.
 */
public class MainViewController {
  private GameType selectedGameType;

  /**
   * Constructor for MainViewController.
   */
  public void selectGame(GameType gameType) {
    this.selectedGameType = gameType;
    System.out.println("Game selected: " + gameType);
  }
}
