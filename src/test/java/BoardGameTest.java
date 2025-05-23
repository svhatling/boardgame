import static org.junit.jupiter.api.Assertions.*;

import model.exception.BoardNotInitializedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.entity.BoardGame;
import model.entity.Player;

class BoardGameTest {

  private BoardGame game;
  private Player player1, player2;

  @BeforeEach
  void setUp() {
    game = new BoardGame();
    game.createBoard("snakesandladders");
    game.createDice(2);
    player1 = new Player("Player1", game.getBoard(), "Car");
    player2 = new Player("Player2", game.getBoard(), "Hat");
    game.addPlayer(player1);
    game.addPlayer(player2);
  }

  @Test
  void testBoardIsCreated() {
    assertNotNull(game.getBoard(), "The board should be created.");
  }

  @Test
  void testPlayersAreAdded() {
    assertEquals(2, game.getPlayers().size(), "The game should have 2 players.");
  }

  @Test
  void testGameStartsWithoutWinner() {
    assertNull(game.getWinner(), "There should be no winner at the start of the game.");
  }

  @Test
  void testPlayerMovesAfterDiceRoll() {
    int initialTile = player1.getCurrentTile().getTileId();
    game.setCurrentPlayer(player1);
    int diceRoll = game.getDice().rollDice();
    player1.move(diceRoll);
    assertNotEquals(initialTile, player1.getCurrentTile().getTileId(), "Player should move after rolling the dice.");
  }

  @Test
  void testGameEndsWhenPlayerReachesLastTile() {
    game.setCurrentPlayer(player1);
    player1.setCurrentTile(game.getBoard().getTile(game.getBoard().getTiles().size())); // Sett spilleren på siste brikke
    assertEquals(player1, game.getWinner(), "The player on the last tile should be the winner.");
  }

  @Test
  void testGetWinnerThrowsExceptionWhenBoardNotInitialized() {
    BoardGame notInitializedGame = new BoardGame();
    Exception exception = assertThrows(BoardNotInitializedException.class, notInitializedGame::getWinner);
    assertEquals("Board must be created before checking for a winner.", exception.getMessage());
  }
}
