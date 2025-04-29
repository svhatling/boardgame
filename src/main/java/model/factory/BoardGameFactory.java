package model.factory;

import model.entity.BoardGame;

/**
 * Simple factory for creating different types of BoardGame.
 */
public class BoardGameFactory {

  /**
   * Create a new BoardGame of the given type with the specified number of dice.
   *
   * @param gameType     the game variant, e.g. "snakesandladders" or "ludo"
   * @param numberOfDice how many dice the game should use
   * @return a fully initialized BoardGame
   */
  public static BoardGame create(String gameType, int numberOfDice) {
    BoardGame game = new BoardGame();
    game.createBoard(gameType);    // throws IllegalGameTypeException for unknown type
    game.createDice(numberOfDice);
    return game;
  }

  /**
   * Create a Snakes & Ladders game with the given number of dice.
   *
   * @param numberOfDice how many dice the game should use
   * @return a BoardGame set up for Snakes & Ladders
   */
  public static BoardGame createSnakesAndLadders(int numberOfDice) {
    return create("snakesandladders", numberOfDice);
  }

  /**
   * Create a Ludo game with the given number of dice.
   *
   * @param numberOfDice how many dice the game should use
   * @return a BoardGame set up for Ludo
   */
  public static BoardGame createLudoGame(int numberOfDice) {
    return create("ludo", numberOfDice);
  }
}
