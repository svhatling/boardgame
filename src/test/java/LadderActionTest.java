package test;

import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LadderActionTest {
  private LadderAction ladderAction;
  private Player player;
  private Tile startTile, destinationTile;
  private Board board;

  @BeforeEach
  void setUp() {
    // Opprett Board-objekt som trengs av Player
    board = new Board();

    // Opprett Tiles og LadderAction
    startTile = new Tile(3); // Start på felt 3
    destinationTile = new Tile(20); // Destinasjon er felt 20
    ladderAction = new LadderAction(20, "Climb to 20");

    // Sett LadderAction på startTile
    startTile.setTileAction(ladderAction);

    // Opprett Player med startTile og board
    player = new Player("TestPlayer", startTile, board);
  }

  @Test
  void testLadderActionMovesPlayer() {
    // Spilleren lander på startTile og utløser LadderAction
    startTile.landPlayer(player);

    // Sjekk spillerens posisjon etter LadderAction
    System.out.println("Test: Player is on tile " + player.getCurrentTile().getTileId());

    // Test at spilleren er på destinationTile etter LadderAction
    assertEquals(20, player.getCurrentTile().getTileId(), "Player should land on tile 20 after using ladder");
  }
}
