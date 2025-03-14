package entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LadderActionTest {

  private Board board;
  private Player player;
  private LadderAction ladderAction;
  private int startTileId = 5;
  private int destinationTileId = 20;

  @BeforeEach
  void setUp() {
    // Oppretter brett og spilleren
    board = new Board();
    for (int i = 1; i <= 90; i++) {
      board.addTile(new Tile(i));
    }
    player = new Player("TestPlayer", board);

    // Oppretter en LadderAction fra startTileId til destinationTileId
    ladderAction = new LadderAction(destinationTileId, "climbing up the ladder!");
    board.getTile(startTileId).setTileAction(ladderAction);

    // Plasserer spilleren på startTileId
    player.setCurrentTile(board.getTile(startTileId));
  }

  // Tester at LadderAction flytter spilleren til riktig tile
  @Test
  void testLadderActionMovesPlayerToCorrectTile() {
    board.getTile(startTileId).landPlayer(player);

    assertEquals(destinationTileId, player.getCurrentTile().getTileId(),
        "Spilleren skal flyttes til destinasjonstilen etter ladder action.");
  }

  // Tester at LadderAction ikke gjør noe hvis den ikke er satt på tile
  @Test
  void testLadderActionNotSet() {
    Tile tileWithoutAction = board.getTile(10);
    player.setCurrentTile(tileWithoutAction);

    tileWithoutAction.landPlayer(player);

    assertEquals(10, player.getCurrentTile().getTileId(),
        "Spilleren skal forbli på samme tile når det ikke er en ladder action.");
  }

  // Tester at LadderAction håndterer en ugyldig destinasjon (utenfor brettet)
  @Test
  void testLadderActionWithInvalidDestination() {
    int invalidDestination = 200; // Destinasjon utenfor brettet
    LadderAction invalidLadder = new LadderAction(invalidDestination, "Invalid ladder!");
    board.getTile(15).setTileAction(invalidLadder);

    player.setCurrentTile(board.getTile(15));
    board.getTile(15).landPlayer(player);

    assertEquals(invalidDestination, player.getCurrentTile().getTileId(),
        "Spilleren skal bli satt til ugyldig tile dersom LadderAction tillater det.");
  }

  // Tester at LadderAction håndterer en negativ destinasjon
  @Test
  void testLadderActionWithNegativeDestination() {
    int negativeDestination = -5;
    LadderAction negativeLadder = new LadderAction(negativeDestination, "Falling into the void!");
    board.getTile(7).setTileAction(negativeLadder);

    player.setCurrentTile(board.getTile(7));
    board.getTile(7).landPlayer(player);

    assertEquals(negativeDestination, player.getCurrentTile().getTileId(),
        "Spilleren skal bli flyttet til en negativ tile dersom LadderAction tillater det.");
  }

  // Tester at LadderAction utfører handlingen flere ganger korrekt
  @Test
  void testLadderActionMultipleTimes() {
    board.getTile(startTileId).landPlayer(player);
    board.getTile(startTileId).landPlayer(player); // Samme handling på nytt

    assertEquals(destinationTileId, player.getCurrentTile().getTileId(),
        "Spilleren skal fortsatt ende opp på destinasjonstilen.");
  }
}
