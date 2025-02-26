package test;

import entity.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
  @Test
  void testTileActionExecution() {
    // Opprett et brett (Board)
    Board board = new Board();

    // Opprett en tile og legg til en LadderAction
    Tile tile = new Tile(1);  // Sørg for at ID er satt til 1
    LadderAction action = new LadderAction(20, "Move up to 20");
    tile.setTileAction(action);

    // Opprett en spiller og plasser den på tile (nå trenger vi Board)
    Player player = new Player("TestPlayer", tile, board); // Spilleren starter på tile 1

    System.out.println("Before action: Player is on tile " + player.getCurrentTile().getTileId());

    // Spilleren lander på tile og utfører action
    tile.landPlayer(player);

    // Test at spilleren er på felt 20 etter å ha brukt stigen
    System.out.println("After action: Player is on tile " + player.getCurrentTile().getTileId());
    assertEquals(20, player.getCurrentTile().getTileId());
  }
}
