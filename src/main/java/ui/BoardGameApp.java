package ui;

import entity.*;
import factory.BoardGameFactory;

public class BoardGameApp {
  private BoardGame game;

  public BoardGameApp() {
    this.game = BoardGameFactory.createLadderGameWithDice(2);
  }

  public void start() {
    Board board = this.game.getBoard();


    // Add players, each player gets a start tile and board
    Player player1 = new Player("Mikke", board);
    Player player2 = new Player("Donald", board);
    Player player3 = new Player("Langbein", board);
    Player player4 = new Player("Dolly", board);

    // Add players to the game
    game.addPlayer(player1);
    game.addPlayer(player2);
    game.addPlayer(player3);
    game.addPlayer(player4);

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

  public static void main(String[] args) {
    BoardGameApp app = new BoardGameApp();
    app.start();
  }
}