package model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.util.BoardConfigLoader;
import model.util.BoardConfigLoader.TileConfig;
import model.exception.InvalidGameTypeException;
import model.exception.BoardNotInitializedException;


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
        Map<Integer, TileConfig> config = BoardConfigLoader.loadConfig("board_config.json");

        for (Map.Entry<Integer, TileConfig> entry : config.entrySet()) {
          int from = entry.getKey();
          TileConfig cfg = entry.getValue();
          board.getTile(from).setTileAction(new LadderAction(cfg.to, cfg.message));
        }
        break;

      case "quiz":
        for (int i = 1; i <= 90; i++) {
          board.addTile(new Tile(i));
        }
        break;

      default:
        throw new InvalidGameTypeException("Invalid game type: " + gameType);
    }
  }

  public void createDice(int numDice) {
    dice = new Dice(numDice, 6);
  }

  public Player getWinner() {
    if (board == null || board.getTiles() == null || board.getTiles().isEmpty()) {
      throw new BoardNotInitializedException("Board must be created before checking for a winner.");
    }

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