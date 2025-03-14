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
    // Creates board and player
    board = new Board();
    for (int i = 1; i <= 90; i++) {
      board.addTile(new Tile(i));
    }
    player = new Player("TestPlayer", board);

    // Creates a LadderAction from startTileId to destinationTileId
    ladderAction = new LadderAction(destinationTileId, "climbing up the ladder!");
    board.getTile(startTileId).setTileAction(ladderAction);

    // Places the player on startTileId
    player.setCurrentTile(board.getTile(startTileId));
  }

  // Testing that LadderAction moves the player to the correct tile
  @Test
  void testLadderActionMovesPlayerToCorrectTile() {
    board.getTile(startTileId).landPlayer(player);

    assertEquals(destinationTileId, player.getCurrentTile().getTileId(),
        "The player should be moved to the destination tile after ladder action.");
  }

  // Testing that LadderAction does nothing if it's not set on a tile
  @Test
  void testLadderActionNotSet() {
    Tile tileWithoutAction = board.getTile(10);
    player.setCurrentTile(tileWithoutAction);

    tileWithoutAction.landPlayer(player);

    assertEquals(10, player.getCurrentTile().getTileId(),
        "The player should remain on the same tile when there is no ladder action.");
  }

  // Testing that LadderAction handles an invalid destination (outside the board)
  @Test
  void testLadderActionWithInvalidDestination() {
    int invalidDestination = 200; // Destination outside the board
    LadderAction invalidLadder = new LadderAction(invalidDestination, "Invalid ladder!");
    board.getTile(15).setTileAction(invalidLadder);

    player.setCurrentTile(board.getTile(15));
    board.getTile(15).landPlayer(player);

    assertEquals(invalidDestination, player.getCurrentTile().getTileId(),
        "The player should be moved to an invalid tile if LadderAction allows it.");
  }

  // Testing that LadderAction handles a negative destination
  @Test
  void testLadderActionWithNegativeDestination() {
    int negativeDestination = -5;
    LadderAction negativeLadder = new LadderAction(negativeDestination, "Falling into the void!");
    board.getTile(7).setTileAction(negativeLadder);

    player.setCurrentTile(board.getTile(7));
    board.getTile(7).landPlayer(player);

    assertEquals(negativeDestination, player.getCurrentTile().getTileId(),
        "The player should be moved to a negative tile if LadderAction allows it.");
  }

  // Testing that LadderAction executes the action multiple times correctly
  @Test
  void testLadderActionMultipleTimes() {
    board.getTile(startTileId).landPlayer(player);
    board.getTile(startTileId).landPlayer(player); // Execute the action again

    assertEquals(destinationTileId, player.getCurrentTile().getTileId(),
        "The player should still end up on the destination tile.");
  }
}
