package test;

import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LadderActionTest {

  private LadderAction ladderAction;
  private Player player;
  private Tile startTile;
  private Board board;
  private Dice dice;

  @BeforeEach
  void setUp() {
    // Create a new BoardGame instance and set up the board with ladders
    BoardGame boardGame = new BoardGame();
    boardGame.createBoard(); // Set up board with ladders
    boardGame.createDice(); // Create the dice

    // Get the board to use in this test
    board = boardGame.getBoard();

    // Access the Tile for starting on tile 3
    startTile = board.getTile(3);
    ladderAction = new LadderAction(20, "Climb to 20");
    startTile.setTileAction(ladderAction);

    // Initialize the player on startTile
    player = new Player("TestPlayer", startTile, board);

    // Initialize dice (1 die with 6 sides, as presumably configured elsewhere)
    dice = new Dice(1, 6);
  }

  @Test
  void testLadderActionMovesPlayer() {
    // Setup the Board and Tiles
    Board board = new Board();
    board.addTile(new Tile(1)); // Define tile 1 without a laddder
    board.addTile(new Tile(2));

    // Assume the starting Tile is tile 1
    Tile startingTile = board.getTile(1);

    // Now create the Player with name, starting tile, and the board
    Player player = new Player("Player1", startingTile, board);

    // Simulate a roll to move from tile 1 to tile 2
    int rollValue = 1; // Move from tile 1 to tile 2
    player.move(rollValue, board); // Move to tile 2

    // Check if the action from tile 2 executed correctly, moving to tile 45
    assertEquals(45, player.getCurrentTile().getTileId(), "Player should land on tile 45 after using the ladder from tile 2");
  }
}