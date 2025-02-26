package edu.ntnu.idatt2003;

import entity.*;

public class BoardGameApp {
  public static void main(String[] args) {
    // Opprett brettet
    Board board = new Board();

    // Lag et nytt spill
    BoardGame game = new BoardGame();

    // Opprett brettet og terningen i spillet
    game.createBoard();
    game.createDice();

    // Legg til spillere, hver spiller får sitt startfelt og brett
    game.addPlayer(new Player("Arne", new Tile(1), board));
    game.addPlayer(new Player("Ivar", new Tile(1), board));
    game.addPlayer(new Player("Majid", new Tile(1), board));
    game.addPlayer(new Player("Atle", new Tile(1), board));

    // Start spillet
    game.play();
  }
}
