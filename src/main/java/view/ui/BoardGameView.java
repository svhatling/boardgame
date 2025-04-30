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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.entity.BoardGame;
import model.entity.Player;
import model.util.BoardConfigLoader;
import model.util.BoardConfigLoader.TileConfig;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * INITIAL DRAFT.
 *
 * This view displays:
 *  - A top header with title, current player and dice roll
 *  - A center board grid plus with a canvas layer for ladders and snakes
 *  - A right panel showing all players and what tile they are currently on
 */
public class BoardGameView extends BorderPane {

  /** Observer for user actions on the boardgame screen. */
  public interface Observer {
    void onRollDice();
  }

  private static final int COLS = 10;
  private static final int ROWS = 9;

  private final BoardGame game;
  private Observer observer;

  // UI, text showing current player, all players, and dice roll.
  private final Label currentPlayerLabel = new Label("Current: -");
  private final Label diceResultLabel   = new Label("Last roll: -");

  private final GridPane boardGrid = new GridPane();
  private final Map<Integer, Label> tileLabels = new HashMap<>();

  private final Canvas ladderCanvas = new Canvas(500, 450);
  private final VBox playerListBox  = new VBox(5);

  private final ImageView die1View = new ImageView();
  private final ImageView die2View = new ImageView();
  private final Image[] diceImages = new Image[7];

  /**
   * Constructor of view, with header on top, board in center, and player list on the right.
   *
   * @param game the game model with board, players and dice.
   */
  public BoardGameView(BoardGame game) {
    this.game = game;
    // Using css styling
    this.getStyleClass().add("root");
    this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

    for (int face = 1; face <= 6; face++) {
      diceImages[face] =
          new Image(getClass().getResourceAsStream("/icons/dice" + face + ".png"));
    }
    die1View.setFitWidth(64);
    die1View.setFitHeight(64);
    die2View.setFitWidth(64);
    die2View.setFitHeight(64);
    die1View.setImage(diceImages[1]);
    die2View.setImage(diceImages[1]);

    //Title, sub labels and roll button
    Label title = new Label("Game Board");
    title.getStyleClass().add("label-title");

    currentPlayerLabel.getStyleClass().add("label-sub");

    HBox diceBox = new HBox(10, die1View, die2View);
    diceBox.setAlignment(Pos.CENTER);

    Button rollButton = new Button("Roll Dice");
    rollButton.getStyleClass().add("button-main");
    rollButton.setOnAction(e -> {
      if (observer != null) observer.onRollDice();
      updateDiceImages();
      updateView();
    });
    currentPlayerLabel.getStyleClass().add("label-sub");

    VBox header = new VBox(5,
        title,
        currentPlayerLabel,
        diceBox,
        rollButton
    );
    header.setAlignment(Pos.CENTER);
    header.setPadding(new Insets(10));
    setTop(header);

    // Board area
    buildBoardGrid();

    double cellW = 50, cellH = 50;
    ladderCanvas.setWidth(COLS * cellW);
    ladderCanvas.setHeight(ROWS * cellH);
    drawLaddersAndSnakes();

    StackPane boardPane = new StackPane(boardGrid, ladderCanvas);
    boardPane.setAlignment(Pos.CENTER);
    boardPane.setPadding(new Insets(10));
    setCenter(boardPane);

    // Player list panel
    playerListBox.setPadding(new Insets(10));
    playerListBox.setAlignment(Pos.TOP_LEFT);
    setRight(playerListBox);

    updateView();
  }

  private void updateDiceImages() {
    List<Integer> vals = game.getDice().getDiceValues();
    int v1 = vals.get(0);
    int v2 = vals.size() > 1 ? vals.get(1) : 1;
    die1View.setImage(diceImages[v1]);
    die2View.setImage(diceImages[v2]);
  }

  /**
   * Register the observer for button events.
   *
   * @param observer implementation of Observer
   */
  public void setObserver(Observer observer) {
    this.observer = observer;
  }

  /**
   * Draw all ladders and snakes from the JSON config file.
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

      // Green for ladder, red for snake
      gc.setStroke(to > from ? Color.GREEN : Color.RED);
      gc.strokeLine(x1, y1, x2, y2);
    }
  }

  /** Cols X rows grid of tiles. */
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
   * Convert a tile ID into row X col coordinates.
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
   * Refreshing header labels, board highlight, and player list.
   * Call after change in the game model.
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

    // Removes the old highlights
    tileLabels.values().forEach(cell -> cell.setStyle(""));

    // Highlight the tile the current player is on
    if (current != null) {
      int id = current.getCurrentTile().getTileId();
      Label cell = tileLabels.get(id);
      if (cell != null) cell.setStyle("-fx-background-color: lightblue;");
    }

    // Rebuild player list on the right
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
