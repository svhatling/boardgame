package view.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.entity.BoardGame;
import model.entity.Player;
import model.util.BoardConfigLoader;    // ← import for config
import model.util.BoardConfigLoader.TileConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * INITIAL DRAFT.
 *
 * A view that displays:
 *  - a top header with title, current player and last roll
 *  - a center board grid plus a canvas layer for ladders/snakes
 *  - a right panel listing all players and their tile positions
 */
public class BoardGameView extends BorderPane {

  /** Listener for user actions on the board screen. */
  public interface Observer {
    void onRollDice();
    void onSaveGame();
    void onLoadGame();
  }

  private static final int COLS = 10;
  private static final int ROWS = 9;

  private final BoardGame game;
  private Observer observer;

  // --- UI parts ---
  private final Label currentPlayerLabel = new Label("Current: -");
  private final Label diceResultLabel   = new Label("Last roll: -");

  private final GridPane boardGrid = new GridPane();
  private final Map<Integer, Label> tileLabels = new HashMap<>();

  private final Canvas ladderCanvas = new Canvas(500, 450);
  private final VBox playerListBox  = new VBox(5);

  /**
   * Build the view: header at top, board in center, player list on right.
   *
   * @param game the game model containing board, players, dice
   */
  public BoardGameView(BoardGame game) {
    this.game = game;

    // --- HEADER ---
    Label title = new Label("Game Board");
    title.getStyleClass().add("label-title");

    Button rollBtn = new Button("Roll Dice");
    rollBtn.setOnAction(e -> {
      if (observer != null) observer.onRollDice();
      updateView();
    });

    Button saveBtn = new Button("Save Game");
    saveBtn.setOnAction(e -> {
      if (observer != null) observer.onSaveGame();
    });

    Button loadBtn = new Button("Load Game");
    loadBtn.setOnAction(e -> {
      if (observer != null) observer.onLoadGame();
    });

    VBox header = new VBox(5,
        title,
        currentPlayerLabel,
        diceResultLabel,
        rollBtn,
        saveBtn,
        loadBtn
    );
    header.setAlignment(Pos.CENTER);
    header.setPadding(new Insets(10));
    setTop(header);

    // --- BOARD AREA ---
    buildBoardGrid();

    double cellW = 50, cellH = 50;
    ladderCanvas.setWidth(COLS * cellW);
    ladderCanvas.setHeight(ROWS * cellH);
    drawLaddersAndSnakes();   // ← now draws all ladders & snakes

    StackPane boardPane = new StackPane(boardGrid, ladderCanvas);
    boardPane.setAlignment(Pos.CENTER);
    boardPane.setPadding(new Insets(10));
    setCenter(boardPane);

    // --- PLAYER LIST PANEL ---
    playerListBox.setPadding(new Insets(10));
    playerListBox.setAlignment(Pos.TOP_LEFT);
    setRight(playerListBox);

    updateView();
  }

  /**
   * Register the observer for button events.
   *
   * @param observer an implementation of Observer
   */
  public void setObserver(Observer observer) {
    this.observer = observer;
  }

  /**
   * Draw all ladders and snakes based on the JSON config.
   */
  private void drawLaddersAndSnakes() {
    Map<Integer, TileConfig> config = BoardConfigLoader.loadConfig("board_config.json");
    GraphicsContext gc = ladderCanvas.getGraphicsContext2D();
    gc.setLineWidth(4);

    double cellW = ladderCanvas.getWidth()  / COLS;
    double cellH = ladderCanvas.getHeight() / ROWS;

    for (Map.Entry<Integer, TileConfig> e : config.entrySet()) {
      int from = e.getKey();
      int to   = e.getValue().to;

      int[] rcFrom = tileIdToRowCol(from);
      int[] rcTo   = tileIdToRowCol(to);

      double x1 = rcFrom[1] * cellW + cellW/2;
      double y1 = (ROWS - 1 - rcFrom[0]) * cellH + cellH/2;
      double x2 = rcTo  [1] * cellW + cellW/2;
      double y2 = (ROWS - 1 - rcTo  [0]) * cellH + cellH/2;

      // green for ladder (to > from), red for snake
      gc.setStroke(to > from ? Color.GREEN : Color.RED);
      gc.strokeLine(x1, y1, x2, y2);
    }
  }

  /** Lay out the COLS×ROWS grid of tile labels. */
  private void buildBoardGrid() {
    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER);
    tileLabels.clear();

    for (int id = 1; id <= COLS * ROWS; id++) {
      int[] rc = tileIdToRowCol(id);
      Label cell = new Label(String.valueOf(id));
      cell.setPrefSize(50, 50);
      cell.setAlignment(Pos.CENTER);
      boardGrid.add(cell, rc[1], ROWS - 1 - rc[0]);
      tileLabels.put(id, cell);
    }
  }

  /**
   * Convert a 1-based tile ID into zero-based [row,col].
   * Row 0 is bottom, col 0 is left.
   */
  private int[] tileIdToRowCol(int tileId) {
    int idx = tileId - 1;
    int row = idx / COLS;
    int colIdx = idx % COLS;
    int col = (row % 2 == 0) ? colIdx : (COLS - 1 - colIdx);
    return new int[]{ row, col };
  }

  /**
   * Refreshes header labels, board highlight, and player list.
   * Call after any change in the game model.
   */
  public void updateView() {
    // Header
    Player current = game.getCurrentplayer();
    currentPlayerLabel.setText(
        current != null ? "Current: " + current.getName() : "Current: -"
    );
    diceResultLabel.setText(
        game.getDice() != null
            ? "Last roll: " + game.getDice().getSum()
            : "Last roll: -"
    );

    // Clear old highlights
    tileLabels.values().forEach(cell -> cell.setStyle(""));

    // Highlight current tile
    if (current != null) {
      int id = current.getCurrentTile().getTileId();
      Label cell = tileLabels.get(id);
      if (cell != null) cell.setStyle("-fx-background-color: lightblue;");
    }

    // Rebuild player list on right
    playerListBox.getChildren().clear();
    Label pplTitle = new Label("Players:");
    pplTitle.getStyleClass().add("label-sub");
    playerListBox.getChildren().add(pplTitle);

    for (Player p : game.getPlayers()) {
      String text = p.getName() + " → tile " + p.getCurrentTile().getTileId();
      Label lbl = new Label(text);
      if (p == current) {
        lbl.setStyle("-fx-font-weight: bold;");
      }
      playerListBox.getChildren().add(lbl);
    }
  }
}
