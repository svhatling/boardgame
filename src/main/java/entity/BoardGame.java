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
    addLadder(2, 45, "klatrer opp stigen!");
    addLadder(8, 29, "klatrer opp stigen!");
    addLadder(34, 53, "klatrer opp stigen!");
    addLadder(40, 58, "klatrer opp stigen!");
    addLadder(55, 74, "klatrer opp stigen!");
    addLadder(65, 86, "klatrer opp stigen!");
    addLadder(68, 89, "klatrer opp stigen!");

    // Stiger ned
    addLadder(88, 46, "faller ned stigen!");
    addLadder(84, 63, "faller ned stigen!");
    addLadder(82, 14, "faller ned stigen!");
    addLadder(71, 31, "faller ned stigen!");
    addLadder(62, 25, "faller ned stigen!");
    addLadder(27, 6, "faller ned stigen!");
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
    int round = 1;
    while (!gameOver) {
      System.out.println("Round number " + round);
      for (Player player : players) {
        int diceValue = dice.rollDice();
        System.out.println(player.getName() + " trillet " + diceValue);
        player.move(diceValue, board);
        System.out.println(player.getName() + " er på felt " + player.getCurrentTile().getTileId());
        if (player.getCurrentTile().getTileId() >= 90) {
          System.out.println("And the winner is: " + player.getName());
          gameOver = true;
          break;
        }
      }
      round++;
    }
  }
}