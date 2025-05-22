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
  public static BoardGame create(String gameType, String configPath, int numberOfDice) {
    BoardGame game = new BoardGame();
    game.createBoard(gameType, configPath);
    game.createDice(numberOfDice);
    return game;
  }

  /**
   * Create a Snakes & Ladders game with the given number of dice.
   * Default easy configuration is used.
   *
   * @param numberOfDice how many dice the game should use
   * @return a BoardGame set up for Snakes & Ladders
   */
  public static BoardGame createSnakesAndLadders(int numberOfDice) {
    return create(
        "snakesandladders",
        "config/snakes_and_ladders/sl_easy_config.json",
        numberOfDice
    );
  }

  /**
   * Create a quiz game with the given number of dice.
   * Default general knowledge configuration is used.
   *
   * @param numberOfDice how many dice the game should use
   * @return a BoardGame set up for quiz
   */
  public static BoardGame createQuizGame(int numberOfDice) {
    return create("quiz", "config/quiz/questions.json", numberOfDice);
  }
}
