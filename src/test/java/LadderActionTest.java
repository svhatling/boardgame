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
    board = new Board();
    for (int i = 1; i <= 90; i++) {
      board.addTile(new Tile(i));
    }
    player = new Player("TestPlayer", board);

    ladderAction = new LadderAction(destinationTileId, "climbing up the ladder!");
    board.getTile(startTileId).setTileAction(ladderAction);

    player.setCurrentTile(board.getTile(startTileId));
  }

  @Test
  void testLadderActionMovesPlayerToCorrectTile() {
    board.getTile(startTileId).landPlayer(player);
    assertEquals(destinationTileId, player.getCurrentTile().getTileId(),
        "The player should be moved to the destination tile after ladder action.");
  }

  @Test
  void testLadderActionNotSet() {
    Tile tileWithoutAction = board.getTile(10);
    player.setCurrentTile(tileWithoutAction);

    tileWithoutAction.landPlayer(player);
    assertEquals(10, player.getCurrentTile().getTileId(),
        "The player should remain on the same tile when there is no ladder action.");
  }

  @Test
  void testLadderActionWithNegativeDestinationThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new LadderAction(-5, "Invalid ladder!")
    );
    assertEquals("Destination tile ID cannot be negative or zero.", exception.getMessage());
  }

  @Test
  void testLadderActionMovesPlayerToMaxTileIfOver90() {
    LadderAction ladderAction = new LadderAction(110, "Too high! Moving to 90.");
    board.getTile(startTileId).setTileAction(ladderAction);

    player.setCurrentTile(board.getTile(startTileId));
    board.getTile(startTileId).landPlayer(player);

    assertEquals(90, player.getCurrentTile().getTileId(),
        "The player should be moved to tile 90 if the destination is higher than 90.");
  }

  @Test
  void testLadderActionMultipleTimes() {
    board.getTile(startTileId).landPlayer(player);
    board.getTile(startTileId).landPlayer(player);
    assertEquals(destinationTileId, player.getCurrentTile().getTileId(),
        "The player should still end up on the destination tile.");
  }
}
