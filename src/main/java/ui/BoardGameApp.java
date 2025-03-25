package ui;

import entity.*;

public class BoardGameApp implements BoardGameObserver {
  private BoardGame game;

  public BoardGameApp() {
    this.game = new BoardGame();
    // Registrer appen som en observer
    this.game.registerObserver(this);
  }

  @Override
  public void gameStateChanged(String message) {
    // Håndter tilstandsoppdateringer fra BoardGame
    System.out.println("Game Update: " + message);
  }

  public void start() {
    // Opprett brettet og terningen i spillet
    this.game.createBoard("snakesandladders");
    this.game.createDice(2);

    Board board = this.game.getBoard();

    // Legg til spillere
    Player player1 = new Player("Mikke", board);
    Player player2 = new Player("Donald", board);
    Player player3 = new Player("Langbein", board);
    Player player4 = new Player("Dolly", board);

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
        game.playerMoved(player, player.getCurrentTile().getTileId());

        if (player.getCurrentTile().getTileId() == board.getTiles().size()) {
          winner = player;
          game.declareWinner(winner);
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

