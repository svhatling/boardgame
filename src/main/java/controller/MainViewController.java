package controller;

import model.logic.GameType;

/**
 * MainViewController is responsible for handling the main view of the game.
 * It allows the user to select a game type and manages the game state.
 */
public class MainViewController {

  /**
   * Constructor for MainViewController.
   *
   * @param gameType the type of game to be played
   */
  public void selectGame(GameType gameType) {
    System.out.println("Game selected: " + gameType);
  }
}
