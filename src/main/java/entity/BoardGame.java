package entity;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {

  private Board board;
  private List<Player> players;
  private Dice dice;
  private Player currentplayer;

  public BoardGame() {
    this.board = new Board();
    this.players = new ArrayList<>();
    this.currentplayer = null;
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard(String gameType) {
    board = new Board();
    switch (gameType.toLowerCase()) {
      case "snakesandladders":
        for (int i = 1; i <= 90; i++) {
          board.addTile(new Tile(i));
        }
        break;
      case "ludo":
        for (int i = 1; i <= 40; i++) {
          board.addTile(new Tile(i));
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }

  public void createDice(int numDice) {
    dice = new Dice(numDice, 6);
  }

  public Player getWinner() {
    for (Player player : players) {
      if (player.getCurrentTile().getTileId() == board.getTiles().size()) {
        return player;
      }
    }
    return null;
  }

  public Board getBoard() {
    return board;
  }

  public void showPlayerStatus() {
    for (Player player : players) {
      System.out.println(player.getName() + " is on tile " + player.getCurrentTile().getTileId());
    }
    System.out.println();
  }

  public Player getCurrentplayer() {
    return currentplayer;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Dice getDice() {
    return this.dice;
  }


  public void setCurrentPlayer(Player player) {
    this.currentplayer = player;
  }

}