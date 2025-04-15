package controller;

public class MainViewController {
  private String selectedGameType;

  public void selectGame(String gameType) {
    this.selectedGameType = gameType;
    System.out.println("Game selected: " + gameType);

    // Later we can add transition to amountOfPlayersView here
  }

  public String getSelectedGameType() {
    return selectedGameType;
  }
}
