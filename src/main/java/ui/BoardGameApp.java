package ui;

import csv.SaveToCSV;
import entity.*;
import java.util.ArrayList;
import java.util.List;

public class BoardGameApp {
  private BoardGame game;
  private static final String CSV_FILE_NAME = "players.csv";

  public BoardGameApp() {
    this.game = new BoardGame();
  }

  public void start() {
    // Opprett brettet og terningen i spillet
    this.game.createBoard("snakesandladders");
    this.game.createDice(2);

    Board board = this.game.getBoard();

    // Opprett spillerne og legg til i spillet
    Player player1 = new Player("Mikke", board, "Car");
    Player player2 = new Player("Donald", board, "Hat");
    Player player3 = new Player("Langbein", board, "Shoe");
    Player player4 = new Player("Dolly", board, "Dog");

    game.addPlayer(player1);
    game.addPlayer(player2);
    game.addPlayer(player3);
    game.addPlayer(player4);

    // Lagre spillernavn i CSV-fil
    savePlayersToCSV();

    System.out.println("The following players are playing:");
    for (Player player : game.getPlayers()) {
      System.out.println("Name: " + player.getName());
    }
    System.out.println();

    int roundNumber = 1;
    Player winner = null;
    while (winner == null) {
      System.out.println("Round " + roundNumber++);

      for (Player player : game.getPlayers()) {
        game.setCurrentPlayer(player);
        int diceRoll = game.getDice().rollDice();
        player.move(diceRoll);

        if (player.getCurrentTile().getTileId() == board.getTiles().size()) {
          winner = player;
          break;
        }
      }
      if (winner == null) {
        game.showPlayerStatus();
      }
      System.out.println();
    }
    System.out.println("Winner is " + winner.getName());
  }

  // Metode for å lagre spillere til CSV-fil
  private void savePlayersToCSV() {
    SaveToCSV csvSaver= new SaveToCSV(CSV_FILE_NAME);

    List<String> players = new ArrayList<>();

    for (Player player : game.getPlayers()) {
      players.add(player.getName() + "," + player.getPiece());
    }

    csvSaver.savePlayers(players);
  }

  public static void main(String[] args) {
    BoardGameApp app = new BoardGameApp();
    app.start();
  }
}

