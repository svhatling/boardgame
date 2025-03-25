package entity;

import java.util.ArrayList;
import java.util.List;
import ui.BoardGameObserver;

public class BoardGame {

  private Board board;
  private List<Player> players;
  private Dice dice;
  private Player currentplayer;
  private List<BoardGameObserver> observers;

  public BoardGame() {
    this.board = new Board();
    this.players = new ArrayList<>();
    this.currentplayer = null;
    this.observers = new ArrayList<>();
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

  public void registerObserver(BoardGameObserver observer) {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  public void unregisterObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers(String message) {
    for (BoardGameObserver observer : observers) {
      observer.gameStateChanged(message);
    }
  }

  public void playerMoved(Player player, int newTileId) {
    player.setCurrentTile(new Tile(newTileId));
    notifyObservers("Player moved to tile " + newTileId);
  }

  public void declareWinner(Player winner) {
    notifyObservers("Winner is " + winner.getName());
  }


}