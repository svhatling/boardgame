package entity;

import java.util.*;

public class BoardGame {
  private Board board;
  private List<Player> players;
  private Dice dice;
  private boolean gameOver;

  public BoardGame() {
    players = new ArrayList<>();
    gameOver = false;
    board = new Board();
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard() {
    board = new Board();
    for (int i = 1; i <= 90; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }

    // Stiger opp
    addLadder(2, 45, "climbing up the ladder!");
    addLadder(8, 29, "climbing up the ladder!");
    addLadder(34, 53, "climbing up the ladder!");
    addLadder(40, 58, "climbing up the ladder!");
    addLadder(55, 74, "climbing up the ladder!");
    addLadder(65, 86, "climbing up the ladder!");
    addLadder(68, 89, "climbing up the ladder!");

    // Stiger ned
    addLadder(88, 46, "falling down the ladder!");
    addLadder(84, 63, "falling down the ladder!");
    addLadder(82, 14, "falling down the ladder!");
    addLadder(71, 31, "falling down the ladder!");
    addLadder(62, 25, "falling down the ladder!");
    addLadder(27, 6, "falling down the ladder!");
  }

  private void addLadder(int start, int end, String description) {
    Tile startTile = board.getTile(start);
    startTile.setTileAction(new LadderAction(end, description));
  }

  public void createDice() {
    dice = new Dice(1, 6);
  }

  public Board getBoard() {
    return board;
  }

  public void play() {
    System.out.println("The following players are playing the game: ");
    for (Player player : players) {
      System.out.println("Name: " + player.getName());
    }
    System.out.println();

    int round = 1;
    while (!gameOver) {
      System.out.println("Round number " + round);
      for (Player player : players) {
        int diceValue = dice.rollDice();
        System.out.println(player.getName() + " is currently on tile " + player.getCurrentTile().getTileId());
        System.out.println(player.getName() + " rolled " + diceValue);

        player.move(diceValue, board);

        System.out.println(player.getName() + " is now on tile " + player.getCurrentTile().getTileId());

        if (player.getCurrentTile().getTileId() >= 90) {
          System.out.println("And the winner is: " + player.getName());
          gameOver = true;
          break;
        }
      }
      System.out.println();
      round++;
    }
  }
}