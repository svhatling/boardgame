package view.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entity.BoardGame;
import model.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * FIRST DRAFT.
 *
 * Shows a grid of tiles, highlights current player,
 * and has buttons to roll, save, or load the game.
 */
public class BoardGameView {

  /**
   * Observer for user actions on the board screen.
   */
  public interface Observer {
    /** Called when the user clicks "Roll Dice". */
    void onRollDice();
    /** Called when the user clicks "Save Game". */
    void onSaveGame();
    /** Called when the user clicks "Load Game". */
    void onLoadGame();
  }

  private static final int COLS = 10;
  private static final int ROWS = 9;

  private final Stage stage;
  private final BoardGame game;
  private Observer observer;

  // Labels we update when the game changes
  private final Label currentPlayerLabel = new Label("Current: -");
  private final Label diceResultLabel   = new Label("Last roll: -");

  // Grid of tile labels, and a lookup map
  private final GridPane boardGrid = new GridPane();
  private final Map<Integer, Label> tileLabels = new HashMap<>();

  /**
   * Builds the board view and shows it in the given window.
   *
   * @param stage the main window
   * @param game  the game model (board, players, dice)
   */
  public BoardGameView(Stage stage, BoardGame game) {
    this.stage = stage;
    this.game  = game;

    buildBoardGrid();

    Label title = new Label("Game Board");
    title.getStyleClass().add("label-title");

    Button rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> {
      if (observer != null) observer.onRollDice();
      updateView();
    });

    Button saveButton = new Button("Save Game");
    saveButton.setOnAction(e -> {
      if (observer != null) observer.onSaveGame();
    });

    Button loadButton = new Button("Load Game");
    loadButton.setOnAction(e -> {
      if (observer != null) observer.onLoadGame();
    });

    VBox root = new VBox(10,
        title,
        currentPlayerLabel,
        diceResultLabel,
        rollButton,
        saveButton,
        loadButton,
        boardGrid
    );
    root.setAlignment(Pos.CENTER);

    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
    stage.setTitle("Board Game");

    updateView();
  }

  /**
   * Observer for button actions.
   *
   * @param observer an implementation of Observer
   */
  public void setObserver(Observer observer) {
    this.observer = observer;
  }

  /**
   * Build a cols x rows grid of tiles, numbered 1-90.
   */
  private void buildBoardGrid() {
    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER);
    tileLabels.clear();

    int total = COLS * ROWS; // 90
    for (int id = 1; id <= total; id++) {
      int[] rc = tileIdToRowCol(id);
      Label cell = new Label(String.valueOf(id));
      cell.setPrefSize(50, 50);
      cell.setAlignment(Pos.CENTER);
      boardGrid.add(cell, rc[1], ROWS - 1 - rc[0]);
      tileLabels.put(id, cell);
    }
  }

  /**
   * Convert a tile ID into row/column.
   *
   * @param tileId tile number from 1 to 90
   * @return [row, col], where row=0 is bottom row, col=0 is left column
   */
  private int[] tileIdToRowCol(int tileId) {
    // tileIndex is the zero-based index of the tile
    int tileIndex = tileId - 1;
    int row        = tileIndex / COLS;
    int indexInRow = tileIndex % COLS;
    int col        = (row % 2 == 0)
        ? indexInRow
        : (COLS - 1 - indexInRow);
    return new int[]{ row, col };
  }

  /**
   * Update labels and highlight the current players' tile.
   * Call when there is any change in the model.
   */
  public void updateView() {
    Player current = game.getCurrentplayer();
    if (current != null) {
      currentPlayerLabel.setText("Current: " + current.getName());
    } else {
      currentPlayerLabel.setText("Current: -");
    }

    if (game.getDice() != null) {
      diceResultLabel.setText("Last roll: " + game.getDice().getSum());
    } else {
      diceResultLabel.setText("Last roll: -");
    }

    // Clears old highlights
    for (Label cell : tileLabels.values()) {
      cell.setStyle("");
    }

    // Highlights new tile
    if (current != null) {
      int id = current.getCurrentTile().getTileId();
      Label cell = tileLabels.get(id);
      if (cell != null) {
        cell.setStyle("-fx-background-color: blue;");
      }
    }
  }
}
