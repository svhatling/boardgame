package view.ui;

import model.entity.Board;
import model.entity.BoardGame;
import model.entity.Player;
import model.factory.BoardGameFactory;

public class BoardGameApp {
  private BoardGame game;

  public BoardGameApp() {
    this.game = BoardGameFactory.createLadderGameWithDice(2);
  }

  public void start() {
    Board board = this.game.getBoard();


    // Add players, each player gets a start tile and board
    Player player1 = new Player("Mikke", board, "Mouse");
    Player player2 = new Player("Donald", board, "Duck");
    Player player3 = new Player("Langbein", board, "Dog");
    Player player4 = new Player("Dolly", board, "Goose");

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

        System.out.println(player.getName() + " rolled " + diceRoll);
        int previousTile = player.getCurrentTile().getTileId();
        player.move(diceRoll);
        int newTile = player.getCurrentTile().getTileId();

        System.out.println(player.getName() + " moved from tile " + previousTile + " to tile " + newTile);

        if (newTile == board.getTiles().size()) {
          winner = player;
          break;
        }
      }
      if (winner == null) {
        game.showPlayerStatus();
      }
      System.out.println();
    }
    System.out.println("Winner is " + winner.getName() + "!!<3<3");
  }

  public static void main(String[] args) {
    BoardGameApp app = new BoardGameApp();
    app.start();
  }
}