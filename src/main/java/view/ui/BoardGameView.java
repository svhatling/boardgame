package view.ui;

import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.entity.BoardGame;
import model.entity.Player;
import model.logic.GameType;
import model.util.BoardConfigLoader;
import model.util.BoardConfigLoader.TileConfig;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import model.util.FullscreenHandler;
import model.util.PieceImageLoader;

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
  private static final String Easy_config = "config/snakes_and_ladders/sl_easy_config.json";
  private static final String Hard_config = "config/snakes_and_ladders/sl_hard_config.json";
  private String chosenConfigFile = Easy_config;

  private final BoardGame game;
  private final FullscreenHandler fullscreenHandler;
  private Observer observer;
  private GameType gameType;

  // UI, text showing current player, all players, and dice roll.
  private final Label currentPlayerLabel = new Label("Current: -");
  private final Label diceResultLabel   = new Label("Last roll: -");

  private final ImageView die1View = new ImageView();
  private final ImageView die2View = new ImageView();
  private final Image[] diceImages = new Image[7];

  private final GridPane boardGrid = new GridPane();
  private final Canvas ladderCanvas = new Canvas(500, 450);

  private final VBox difficultyPane;
  private final Button easyButton;
  private final Button hardButton;
  private final Button rollButton;
  private final StackPane boardPane;
  private final StackPane centerStack;
  private final VBox playerListBox  = new VBox(5);
  private final Map<Integer, Label> tileLabels = new HashMap<>();

  /**
   * Constructor of view, with header on top, board in center, and player list on the right.
   *
   * @param game the game model with board, players and dice.
   */
  public BoardGameView(BoardGame game, GameType gameType, FullscreenHandler fullscreenHandler) {
    this.fullscreenHandler = fullscreenHandler;
    this.game = game;
    // Using css styling
    this.getStyleClass().add("root-boardgame");
    this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

    for (int face = 1; face <= 6; face++) {
      diceImages[face] =
          new Image(
              Objects.requireNonNull(getClass().getResourceAsStream("/icons/dice" + face + ".png")));
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
    diceBox.setAlignment(Pos.CENTER_RIGHT);

    this.rollButton = new Button("Roll Dice");
    rollButton.getStyleClass().add("roll-dice-button");
    rollButton.setOnAction(e -> {
      if (observer != null) observer.onRollDice();
      updateDiceImages();
      updateView();
    });

    rollButton.setDisable(true);

    VBox playerAndDice = new VBox(5,
        diceBox,
        rollButton
    );
    VBox header = new VBox(5,
        title,
        currentPlayerLabel
    );
    playerAndDice.setAlignment(Pos.CENTER);
    playerAndDice.setPadding(new Insets(10));
    header.setAlignment(Pos.CENTER);
    header.setPadding(new Insets(10));
    setTop(header);

    // Board area
    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    boardGrid.setVgap(1);
    boardGrid.setHgap(1);
    buildBoardGrid();

    double cellW = 50, cellH = 50;
    ladderCanvas.setWidth(COLS * cellW);
    ladderCanvas.setHeight(ROWS * cellH);
    drawLaddersAndSnakes("board_config.json");

    boardPane = new StackPane(boardGrid, ladderCanvas);
    boardPane.setAlignment(Pos.CENTER);
    boardPane.setPadding(new Insets(10));
    boardPane.setVisible(false);

    ladderCanvas.widthProperty().bind(boardPane.widthProperty().subtract(20));
    ladderCanvas.heightProperty().bind(boardPane.heightProperty().subtract(20));
    ladderCanvas.widthProperty().addListener((obs, oldW, newW) -> {
      drawLaddersAndSnakes(chosenConfigFile);
    });
    ladderCanvas.heightProperty().addListener((obs, oldH, newH) -> {
      drawLaddersAndSnakes(chosenConfigFile);
    });


    //Difficulty buttons
    easyButton = new Button("Easy");
    easyButton.getStyleClass().addAll("difficulty-button", "easy-button");
    hardButton = new Button("Hard");
    hardButton.getStyleClass().addAll("difficulty-button", "hard-button");
    easyButton.setPrefWidth(100);
    hardButton.setPrefWidth(100);


    //DifficultyPane
    difficultyPane = new VBox(10, easyButton, hardButton);
    difficultyPane.setAlignment(Pos.CENTER);
    difficultyPane.setPadding(new Insets(20));

    //Stack: difficulty on top of board
    centerStack = new StackPane(difficultyPane, boardPane);
    setCenter(centerStack);

    //Button actions
    easyButton.setOnAction(e -> {
      chosenConfigFile = Easy_config;
      loadConfigAndShowBoard(Easy_config);
      rollButton.setDisable(false);
    });
    hardButton.setOnAction(e -> {
      chosenConfigFile = Hard_config;
      loadConfigAndShowBoard(Hard_config);
      rollButton.setDisable(false);
    });

    // Player list panel
    playerListBox.setPadding(new Insets(10));
    playerListBox.setAlignment(Pos.TOP_RIGHT);

    // Box for rollButton, to make it more centered in the rightPanel
    HBox rollBox = new HBox(rollButton);
    rollBox.setAlignment(Pos.CENTER);

    VBox rightPanel = new VBox(30, diceBox, rollBox, playerListBox
    );
    rightPanel.setAlignment(Pos.TOP_RIGHT);
    rightPanel.setPadding(new Insets(10));

    setRight(rightPanel);

    VBox instrBox = createImprovedInstructionsBox();
    instrBox.getStyleClass().add("instr-box");
    BorderPane.setAlignment(instrBox, Pos.TOP_LEFT);
    setLeft(instrBox);

    fullscreenHandler.setupFullscreenHandling(this);

    updateView();
  }

  /**
   * Oppretter en forbedret instruksjonsboks med nummererte punkter og sirkeldesign
   */
  private VBox createImprovedInstructionsBox() {
    // Hovedcontainer
    VBox instructionsBox = new VBox(8);
    instructionsBox.setPadding(new Insets(10));
    instructionsBox.getStyleClass().add("instructions-box");

    // Tittel
    Label title = new Label("This is how you play:");
    title.getStyleClass().add("instructions-title");

    // Instruksjonspunkter
    VBox instructionPoints = new VBox(6);

    // Punkt 1
    HBox point1 = createInstructionPoint("1", "Choose difficulty.");

    // Punkt 2
    HBox point2 = createInstructionPoint("2", "Press \\\"Roll Dice\\\" to start the game");

    // Punkt 3
    HBox point3 = createInstructionPoint("3", "Your piece will automatically move the amount of spaces from the dice roll.");

    // Punkt 4
    HBox point4 = createInstructionPoint("4", "Red ladders bring you down and green ladders bring you up.");

    // Punkt 5
    HBox point5 = createInstructionPoint("5", "The first player to reach the end wins!");

    // Legg til alle instruksjonspunktene
    instructionPoints.getChildren().addAll(point1, point2, point3, point4, point5);

    // Legg til alle elementer i hovedcontaineren
    instructionsBox.getChildren().addAll(title, instructionPoints);
    instructionsBox.setAlignment(Pos.CENTER);

    return instructionsBox;
  }

  private HBox createInstructionPoint(String number, String text) {
    HBox pointContainer = new HBox(10);
    pointContainer.setAlignment(Pos.TOP_LEFT);

    // Nummersirkel
    Circle numberCircle = new Circle(8);
    numberCircle.getStyleClass().add("number-circle");

    // Nummertekst
    Text numberText = new Text(number);
    numberText.getStyleClass().add("number-text");

    // Instruksjonstekst
    Text instructionText = new Text(text);
    instructionText.getStyleClass().add("instruction-text");
    instructionText.setWrappingWidth(220);

    // Wrapper for sirkel og nummer (for å sentrere nummeret i sirkelen)
    StackPane numberContainer = new StackPane();
    numberContainer.getChildren().addAll(numberCircle, numberText);

    pointContainer.getChildren().addAll(numberContainer, instructionText);
    return pointContainer;
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

  private void loadConfigAndShowBoard(String configFile) {
    boardGrid.getChildren().clear();
    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER);
    buildBoardGrid();

    drawLaddersAndSnakes(configFile);
    styleLaddersAndSnakesTiles(configFile);

    boardPane.setVisible(true);
    difficultyPane.setVisible(false);
  }

  /**
   * Draw all ladders and snakes from the JSON config file.
   */
  private void drawLaddersAndSnakes(String configFile) {
    GraphicsContext graphicsContext = ladderCanvas.getGraphicsContext2D();
    double width = ladderCanvas.getWidth();
    double height = ladderCanvas.getHeight();
    graphicsContext.clearRect(0, 0, width, height);

    // Draw grid
    double cellWidth = ladderCanvas.getWidth() / COLS;
    double cellHeight = ladderCanvas.getHeight() / ROWS;

    graphicsContext.setStroke(Color.BLACK);
    graphicsContext.setLineWidth(1);
    for (int i = 0; i <= COLS; i++) {
      double x = i * cellWidth;
      graphicsContext.strokeLine(x, 0, x, height);
    }
    for (int j = 0; j <= ROWS; j++) {
      double y = j * cellHeight;
      graphicsContext.strokeLine(0, y, width, y);
    }

    // Draw ladders and snakes
    graphicsContext.setLineWidth(4);
    Map<Integer, TileConfig> config = BoardConfigLoader.loadConfig(configFile);
    for (Map.Entry<Integer, TileConfig> e : config.entrySet()) {
      int from = e.getKey();
      int to   = e.getValue().to;

      if (to == 1) continue;

      int[] rcFrom = tileIdToRowCol(from);
      int[] rcTo   = tileIdToRowCol(to);

      double x1 = rcFrom[1] * cellWidth + cellWidth / 2;
      double x2 = rcTo  [1] * cellWidth + cellWidth / 2;

      double centerY1 = (ROWS - 1 - rcFrom[0]) * cellHeight + cellHeight/2;
      double centerY2 = (ROWS - 1 - rcTo  [0]) * cellHeight + cellHeight/2;
      double offsetY  = cellHeight * 0.3;


      double y1 = to > from ? centerY1 - offsetY : centerY1 + offsetY;
      double y2 = to > from ? centerY2 + offsetY : centerY2 - offsetY;

      Color ladderColor = to > from ? Color.GREEN : Color.RED;
      drawLadder(graphicsContext, x1, y1, x2, y2, cellWidth * 0.1, 5, ladderColor);
    }
  }

  /**
   * Draws a ladder, two poles and N steps.
   *
   * @param graphicsContext GraphicsContext
   * @param x1,y1 start tile
   * @param x2,y2 end tile
   * @param offset how far from the middle the poles should be placed
   * @param steps number of steps
   * @param color color on the ladders
   *
   */

  private void drawLadder(GraphicsContext graphicsContext,
      double x1, double y1,
      double x2, double y2,
      double offset, int steps, Color color) {

    graphicsContext.setStroke(color);
    graphicsContext.setLineWidth(4);

    double dx = x2 - x1;
    double dy = y2 - y1;
    double len = Math.hypot(dx, dy);

    double px = -dy / len * offset;
    double py =  dx / len * offset;

    graphicsContext.strokeLine(x1 + px, y1 + py, x2 + px, y2 + py);
    graphicsContext.strokeLine(x1 - px, y1 - py, x2 - px, y2 - py);

    for (int i = 1; i <= steps; i++) {
      double t = i / (double)(steps + 1);
      double cx = x1 + dx * t;
      double cy = y1 + dy * t;
      graphicsContext.strokeLine(cx - px, cy - py, cx + px, cy + py);
    }
  }

  /**
  * Styling tiles where ladders/snakes start and end. Green for ladders, red for snakes.
  * Light color for start tile, dark color for end tile.
  */
  private void styleLaddersAndSnakesTiles(String configFile) {
    Map<Integer, TileConfig> config = BoardConfigLoader.loadConfig(configFile);
    for (Map.Entry<Integer, TileConfig> entry : config.entrySet()) {
      int from = entry.getKey();
      int to   = entry.getValue().to;

      if (to == 1){
        Label cell = tileLabels.get(from);
        if (cell != null) {
          cell.getStyleClass().add("tile-back-to-start");
        }
        continue;
      }

      Label fromCell = tileLabels.get(from);
      Label toCell   = tileLabels.get(to);
      if (fromCell == null || toCell == null) continue;
      if (to > from) {
        fromCell.getStyleClass().add("tile-ladder-start");
        toCell  .getStyleClass().add("tile-ladder-end");
      } else if (to == 1){
        fromCell.getStyleClass().add("tile-back-to-start");
      }else {
        fromCell.getStyleClass().add("tile-snake-start");
        toCell  .getStyleClass().add("tile-snake-end");
      }
    }
  }

  /**
   * Method for building playerList with the selected pieces newt to the player name.
   *
   * @param player name of the player.
   * @param isCurrent the player that is currently playing.
   * @return playerList with piece icons.
   */
  private Node makePlayerListOnRight(Player player, boolean isCurrent) {
    ImageView imageView = new ImageView(PieceImageLoader.get(player.getPiece()));
    imageView.setFitWidth(24);
    imageView.setFitHeight(24);
    imageView.setPreserveRatio(true);

    Label label = new Label(player.getName() + " (tile " + player.getCurrentTile().getTileId() + ")", imageView);
    label.setContentDisplay(ContentDisplay.LEFT);
    label.setGraphicTextGap(8);
    label.getStyleClass().add("label-sub");
    if (isCurrent) {
      label.getStyleClass().add("label-bold");
    }
    return label;
  }



  /** Cols X rows grid of tiles. */
  private void buildBoardGrid() {
    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER_LEFT);
    boardGrid.getChildren().clear();
    boardGrid.getColumnConstraints().clear();
    boardGrid.getRowConstraints().clear();
    tileLabels.clear();

    for (int c = 0; c < COLS; c++) {
      javafx.scene.layout.ColumnConstraints cc = new javafx.scene.layout.ColumnConstraints();
      cc.setPercentWidth(100.0 / COLS);
      boardGrid.getColumnConstraints().add(cc);
    }

    for (int r = 0; r < ROWS; r++) {
      javafx.scene.layout.RowConstraints rc = new javafx.scene.layout.RowConstraints();
      rc.setPercentHeight(100.0 / ROWS);
      boardGrid.getRowConstraints().add(rc);
    }

    for (int id = 1; id <= COLS * ROWS; id++) {
      int[] rc = tileIdToRowCol(id);
      Label cell = new Label(String.valueOf(id));
      cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
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
    boardGrid.getChildren().forEach(node -> {
      if (node instanceof Label) {
        Label cell = (Label) node;
        cell.setStyle("");
        cell.setGraphic(null);
      }
    });

    // Highlight current tile
    if (current != null) {
      int id = current.getCurrentTile().getTileId();
      for (javafx.scene.Node node : boardGrid.getChildren()) {
        if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(id))) {
          ((Label)node).setStyle("-fx-background-color: lightblue;");
          break;
        }
      }
    }

    // Rebuild player list on the right
    for (Player player : game.getPlayers()) {
      int id = player.getCurrentTile().getTileId();
      Label cell = tileLabels.get(id);
      if (cell == null) {
        continue;
      }
      Image image = PieceImageLoader.get(player.getPiece());
      if (image != null) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setPreserveRatio(true);
        for (javafx.scene.Node node : boardGrid.getChildren()) {
          if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(id))) {
            ((Label) node).setGraphic(imageView);
            break;
          }
        }
      }
    }

// Player list
    playerListBox.getChildren().clear();
    Label playersTitle = new Label("Players:");
    playersTitle.getStyleClass().addAll("label-sub","label-list-header");
    playerListBox.getChildren().add(playersTitle);
    playerListBox.setAlignment(Pos.TOP_LEFT);

    for (Player player : game.getPlayers()) {
      boolean isCurrent = player == current;
      // makePlayerListOnRight makes a label with icon and player name
      playerListBox.getChildren().add(makePlayerListOnRight(player, isCurrent));
    }
  }
}
