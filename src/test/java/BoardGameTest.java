package test;

import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardGameTest {
  private BoardGame boardGame;
  private Player player;

  @BeforeEach
  void setUp() {
    boardGame = new BoardGame();
    boardGame.createBoard();
    player = new Player("TestPlayer", new Tile(1), boardGame.getBoard()); // Oppdaterte konstruktøren her
    boardGame.addPlayer(player);
  }

  @Test
  void testPlayerWinsGame() {
    player.placeOnTile(new Tile(1));
    player.move(89, boardGame.getBoard());  // Spilleren beveger seg 89 steg fremover
    assertEquals(90, player.getCurrentTile().getTileId());  // Spilleren skal være på felt 90
  }

  @Test
  void testPlayerLadderAction() {
    // Spilleren starter på et felt med en stige
    Tile startTile = boardGame.getBoard().getTile(2);  // Felt 2 som har en stige
    LadderAction ladderAction = new LadderAction(10, "klatrer opp stigen!");
    startTile.setTileAction(ladderAction);
    player.placeOnTile(startTile);

    // Spilleren lander på feltet og stigen utføres
    startTile.landPlayer(player);

    // Spilleren skal ha kommet til felt 10 etter å ha brukt stigen
    assertEquals(10, player.getCurrentTile().getTileId());
  }
}
