package controller;

import view.BoardGameObserver;

/**
 * Controller for dealing with number of player selections.
 */
public class AmountOfPlayersViewController {

  private final BoardGameObserver observer;
  private int numberOfPlayers = 2;

  /**
   * Constructs a new AmountOfPlayersController.
   *
   * @param observer the observer that is notified when the user starts the game
   */
  public AmountOfPlayersViewController(BoardGameObserver observer) {
    this.observer = observer;
  }

  /**
   * Setter for the number of Players.
   * Sets the number of players for the game.
   *
   * @param number the number of players to set
   */
  public void setNumberOfPlayers(int number) {
    this.numberOfPlayers = number;
    System.out.println("Number of players set to: " + number);
  }

  /**
   * Getter for number of players.
   * Returns the number of players selected.
   *
   * @return the number of players
   */
  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }
}


