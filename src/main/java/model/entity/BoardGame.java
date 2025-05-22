package model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.entity.BackToStartAction;
import model.entity.LadderAction;
import model.util.BoardConfigLoader;
import model.util.BoardConfigLoader.TileConfig;
import model.exception.InvalidGameTypeException;
import model.exception.BoardNotInitializedException;


/**
 * The BoardGame class represents a board game with a board, players, and dice.
 * It allows for the creation of different gametypes (e.g., Snakes and Ladders, Quiz),
 * adding players, rolling dice, and checking for a winner.
 */
public class BoardGame {

  private Board board;
  private List<Player> players;
  private Dice dice;
  private Player currentplayer;

  /**
   * Constructor for the BoardGame class.
   * Initializes the board and players list.
   */
  public BoardGame() {
    this.board = new Board();
    this.players = new ArrayList<>();
    this.currentplayer = null;
  }

  /**
   * Adds a player to the game.
   *
   * @param player the player to be added
   */
  public void addPlayer(Player player) {
    players.add(player);
  }

  /**
   * Creates the board for the game based on the specified game type.
   */
  public void createBoard(String gameType) {
    createBoard(gameType, null);
  }

  /**
   * Creates the board for the game based on the specified game type and configuration path.
   *
   * @param gameType  the type of game (e.g., "snakesandladders", "quiz")
   * @param configPath the path to the configuration file
   */
  public void createBoard(String gameType, String configPath) {
    board = new Board();

    switch (gameType.toLowerCase()) {
      case "snakesandladders":
        for (int i = 1; i <= 90; i++) {
          board.addTile(new Tile(i));
        }
        if (configPath != null) {
          Map<Integer, TileConfig> config = BoardConfigLoader.loadConfig(configPath);
          for (Map.Entry<Integer, TileConfig> entry : config.entrySet()) {
            int from = entry.getKey();
            TileConfig cfg = entry.getValue();
            int to = cfg.to;
            if (to == 1) {
              board.getTile(from).setTileAction(new BackToStartAction(to, cfg.message));
            } else {
              board.getTile(from).setTileAction(new LadderAction(to, cfg.message));
            }
          }
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

  /**
   * Creates the dice based on the dice class.
   *
   * @param numDice number of dice
   */
  public void createDice(int numDice) {
    dice = new Dice(numDice, 6);
  }

  /**
   * Returns the winner of the game.
   *
   * @return the winning player, or null if no winner yet
   */
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

  /**
   * Getter method for the board.
   *
   * @return the board of the game
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Getter method for the current player.
   *
   * @return the current player
   */
  public Player getCurrentplayer() {
    return currentplayer;
  }

  /**
   * Getter method for the players.
   *
   * @return the list of players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Getter method for the dice.
   *
   * @return the dice
   */
  public Dice getDice() {
    return this.dice;
  }

  /**
   * Setter method for the current player.
   *
   * @param player the player to set as current
   */
  public void setCurrentPlayer(Player player) {
    this.currentplayer = player;
  }
}
