package factory;
import entity.BoardGame;

public class BoardGameFactory {

  public static BoardGame createLadderGameWithDice(int numberOfDice) {
    BoardGame game = new BoardGame();
    game.createBoard("snakesandladders");
    game.createDice(numberOfDice);
    return game;
  }
}
