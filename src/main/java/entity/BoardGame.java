package entity;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {

  private Board board;
  private List<Player> players;
  private Dice dice;
  private Player currentPlayer;

  public BoardGame() {
    players = new ArrayList<>();
  }

  public BoardGame(Board board, List<Player> players, Dice dice) {
    this.board = board;
    this.players = players;
    this.dice = dice;
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard(int numberOfTiles) {
    board = new Board();
    for (int i = 1; i <= numberOfTiles; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }
  }

  public void createDice() {
    dice = new Dice(2, 6);
  }

  public void play() {
    boolean gameOver = false;

    while (!gameOver) {
      for (Player player : players) {
        currentPlayer = player;
        int diceValue = dice.rollDice();
        System.out.println(player.getName() + " rolled " + diceValue);

        player.move(diceValue);

        if (player.getCurrentTile().getTileId() == 100) {
          System.out.println(player.getName() + " has won the game!");
          gameOver = true;
          break;
        }
      }
    }
  }

  public Player getWinner() {
    for (Player player : players) {
      if (player.getCurrentTile().getTileId() == 100) {
        return player;
      }
    }
    return null;
  }

}
