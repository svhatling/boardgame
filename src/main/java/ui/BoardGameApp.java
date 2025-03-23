package ui;

import entity.*;

public class BoardGameApp {
  public static void main(String[] args) {
    // Lag et nytt spill
    BoardGame game = new BoardGame();


    // Opprett brettet og terningen i spillet
    game.createBoard();
    game.createDice();

    Board board = game.getBoard();

    // Legg til spillere, hver spiller får sitt startfelt og brett
    Player player1 = new Player("Mikke", board.getTile(1), board);
    Player player2 = new Player("Donald", board.getTile(1), board);
    Player player3 = new Player("Langbein", board.getTile(1), board);
    Player player4 = new Player("Dolly", board.getTile(1), board);

    // Add players to the game
    game.addPlayer(player1);
    game.addPlayer(player2);
    game.addPlayer(player3);
    game.addPlayer(player4);

    // Start spillet
    game.play();
  }
}
