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

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard() {
    board = new Board();
    for (int i = 1; i <= 100; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }

    Tile startTile = board.getTile(5);
    Tile endTile = board.getTile(20);
    startTile.setLandAction(new LadderAction(20, "Stige til felt 20"));
  }

  public void createDice() {
    dice = new Dice(1);
  }

  public void play() {
    boolean gameOver = false;

    while (!gameOver) {
      for (Player player : players) {
        currentPlayer = player;
        int diceValue = dice.roll();
        System.out.println(player.getName() + " trillet " + diceValue);

        player.move(diceValue);

        if (player.getCurrentTile().getTileId() == 100) {
          System.out.println(player.getName() + " har vunnet spillet!");
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
