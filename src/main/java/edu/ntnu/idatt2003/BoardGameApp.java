package edu.ntnu.idatt2003;

import entity.*;

public class BoardGameApp {
  private BoardGame game;

  public BoardGameApp() {
    this.game = new BoardGame();
  }

  public void start() {
    // Opprett brettet og terningen i spillet
    this.game.createBoard();
    this.game.createDice(2);

    Board board = this.game.getBoard();


    // Legg til spillere, hver spiller får sitt startfelt og brett
    Player player1 = new Player("Mikke", board);
    Player player2 = new Player("Donald", board);
    Player player3 = new Player("Langbein", board);
    Player player4 = new Player("Dolly", board);

    // Add players to the game
    game.addPlayer(player1);
    game.addPlayer(player2);
    game.addPlayer(player3);
    game.addPlayer(player4);

    game.players();

    int roundNumber = 1;
    while (!game.isFinished()) {
      System.out.println("Round number " + roundNumber++);
      game.playOneRound();
      if (!game.isFinished()) {
        game.showPlayerStatus();
      }
      System.out.println();
    }
    System.out.println("And the winner is: " + game.getWinner().getName());
  }

  public static void main(String[] args) {
    BoardGameApp app = new BoardGameApp();
    app.start();
  }
}
