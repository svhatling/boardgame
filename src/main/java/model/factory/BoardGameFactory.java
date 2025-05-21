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
   *
   * @param numberOfDice how many dice the game should use
   * @return a BoardGame set up for Snakes & Ladders
   */
  public static BoardGame createSnakesAndLaddersEasy(int numberOfDice) {
    return create(
        "snakesandladders",
        "config/snakes_and_ladders/sl_easy_config.json",
        numberOfDice
    );
  }

  public static BoardGame createSnakesAndLaddersHard(int numberOfDice) {
    return create(
        "snakesandladders",
        "config/snakes_and_ladders/sl_hard_config.json",
        numberOfDice
    );
  }

  public static BoardGame createQuizEasy(int numberOfDice) {
    return create(
        "quiz",
        "config/quiz/quiz_easy_config.json",
        numberOfDice
    );
  }

  public static BoardGame createQuizHard(int numberOfDice) {
    return create(
        "quiz",
        "config/quiz/quiz_hard_config.json",
        numberOfDice
    );
  }
}
