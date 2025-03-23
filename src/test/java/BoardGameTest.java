package entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardGameTest {

  private BoardGame game;
  private Player player1, player2;

  @BeforeEach
  void setUp() {
    game = new BoardGame();
    game.createBoard();
    game.createDice(2);
    player1 = new Player("Player1", game.getBoard());
    player2 = new Player("Player2", game.getBoard());
    game.addPlayer(player1);
    game.addPlayer(player2);
  }

  @Test
  void testBoardIsCreated() {
    assertNotNull(game.getBoard());
  }

  @Test
  void testPlayersAreAdded() {
    assertEquals(2, game.getPlayers().size()); // Sjekker riktig antall spillere
  }

  @Test
  void testGameStartsNotFinished() {
    assertFalse(game.isFinished());
  }

  @Test
  void testPlayerMovesAfterRound() {
    int initialTile = player1.getCurrentTile().getTileId();
    game.playOneRound();
    assertNotEquals(initialTile, player1.getCurrentTile().getTileId());
  }

  @Test
  void testGameEndsWhenPlayerReachesTile90() {
    player1.setCurrentTile(game.getBoard().getTile(90));
    game.playOneRound();
    assertTrue(game.isFinished());
    assertEquals(player1, game.getWinner());
  }
}
