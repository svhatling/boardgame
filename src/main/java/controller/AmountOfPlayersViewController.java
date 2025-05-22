package controller;


/**
 * Controller for dealing with the number of player selections.
 */
public class AmountOfPlayersViewController {

  private int numberOfPlayers = 2;

  /**
   * Constructs a new AmountOfPlayersController.
   *
   */
  public AmountOfPlayersViewController() {
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


