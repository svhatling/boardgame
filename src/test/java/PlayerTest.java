package test;

import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
  private Player player;
  private Tile startTile;
  private Board board;

  @BeforeEach
  void setUp() {
    // Oppretting av brett
    board = new Board();

    // Spilleren skal opprettes med navn, startfelt og brett
    player = new Player("TestPlayer", new Tile(1), board);

    // Legg til noen startfelter på brettet
    board.addTile(new Tile(1));  // Startfelt
    board.addTile(new Tile(2));  // Felt for stigen
    board.addTile(new Tile(45)); // Feltet hvor stigen ender

    // Legg til en stige på felt 2 som fører til felt 45
    Tile tile = board.getTile(2);
    tile.setTileAction(new LadderAction(45, "klatrer opp stigen!"));
  }

  @Test
  void testPlayerMovesUpViaLadder() {
    // Spilleren starter på felt 2
    player.placeOnTile(board.getTile(2));

    // Spilleren ruller terningen (rull = 1 betyr at spilleren skal gå én plass videre)
    player.move(1, board);

    // Spilleren skal bruke stigen på felt 2 og gå direkte til felt 45
    assertEquals(45, player.getCurrentTile().getTileId());
  }

  @Test
  void testPlayerMovesWithoutLadder() {
    // Spilleren starter på felt 1
    player.placeOnTile(board.getTile(1));

    // Spilleren ruller terningen (rull = 1 betyr at spilleren skal gå én plass videre)
    player.move(1, board);

    // Spilleren skal være på felt 2 (ettersom det ikke er noen stige på felt 1)
    assertEquals(2, player.getCurrentTile().getTileId());
  }
}
