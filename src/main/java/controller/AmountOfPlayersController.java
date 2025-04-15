package controller;

public class AmountOfPlayersController {
  private int numberOfPlayers = 2; // Default

  public void setNumberOfPlayers(int number) {
    this.numberOfPlayers = number;
    System.out.println("Number of players set to: " + number);
  }

  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }
}
