package controller;

import view.BoardGameObserver;

/**
 * Controller for dealing the number of players selection in the board game.
 */
public class AmountOfPlayersController {

  private final BoardGameObserver observer;
  private int numberOfPlayers = 2;

  /**
   * Constructs a new AmountOfPlayersController.
   *
   * @param observer the observer to be notified when the user starts the game
   */
  public AmountOfPlayersController(BoardGameObserver observer) {
    this.observer = observer;
  }

  public void setNumberOfPlayers(int number) {
    this.numberOfPlayers = number;
    System.out.println("Number of players set to: " + number);
  }

  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }

  /**
   * Notifies the observer that the game should start with the given number of players.
   *
   * @param numberOfPlayers the number of players selected to start the game
   */
  public void startGame(int numberOfPlayers) {
    observer.onPlayerCountChosen(numberOfPlayers);
  }
}


