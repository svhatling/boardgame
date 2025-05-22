package view;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.entity.BoardGame;
import model.entity.Player;
import model.util.FullscreenHandler;
import model.util.PieceImageLoader;

/**
 * QuizGameView class represents the graphical user interface for a quiz game. It displays the game
 * board, player information, and quiz questions.
 */
public class QuizGameView extends BorderPane {

  /**
   * Observer interface for handling game events. It defines methods for rolling dice, selecting
   * answers, skipping questions, and selecting categories.
   */
  public interface Observer {

    void onRollDice();

    void onAnswerSelected(String answer);

    void onSkipQuestion();

    void onCategorySelected(String playerName);

    void onBack();
  }

  private Observer observer;
  private final List<Player> players;
  private final BoardGame game;
  private Set<Integer> questionTileIds = Collections.emptySet();

  private static final int COLS = 10, ROWS = 9;

  private final GridPane boardGrid = new GridPane();

  private final Label currentPlayerLabel = new Label("Current player");
  private final ImageView die1 = new ImageView();
  private final ImageView die2 = new ImageView();
  private final Image[] diceImages = new Image[7];
  private final Button rollButton = new Button("Roll dice");

  private final VBox quizPane = new VBox(10);
  private final Label questionLabel = new Label();
  private final ToggleGroup answerGroup = new ToggleGroup();
  private final VBox answersBox = new VBox(10);
  private final Button skipButton = new Button("Skip");

  private final VBox playerListBox = new VBox(5);

  private final VBox categoryPane;

  /**
   * Constructor for QuizGameView. Initializes the view with the game, fullscreen handler, and
   * styles.
   *
   * @param game              the BoardGame instance
   * @param fullscreenHandler the FullscreenHandler instance
   */
  public QuizGameView(BoardGame game, FullscreenHandler fullscreenHandler) {
    this.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
    this.players = game.getPlayers();
    this.game = game;

    this.getStyleClass().add("main-root");

    rollButton.setDisable(true);

    for (int f = 1; f <= 6; f++) {
      diceImages[f] = new Image(
          Objects.requireNonNull(getClass().getResourceAsStream("/icons/dice" + f + ".png"))
      );
    }
    die1.setFitWidth(64);
    die1.setFitHeight(64);
    die2.setFitWidth(64);
    die2.setFitHeight(64);
    die1.setImage(diceImages[1]);
    die2.setImage(diceImages[1]);

    currentPlayerLabel.getStyleClass().add("label-sub");
    rollButton.getStyleClass().add("roll-dice-button");
    rollButton.setOnAction(e -> {
      if (observer != null) {
        observer.onRollDice();
      }
    });
    HBox diceBox = new HBox(10, die1, die2);
    diceBox.setAlignment(Pos.CENTER);

    HBox rollBox = new HBox(rollButton);
    rollBox.setAlignment(Pos.CENTER);

    Label title = new Label("Quiz Board");
    title.getStyleClass().add("label-title");
    VBox header = new VBox(5, title, currentPlayerLabel);
    header.setAlignment(Pos.CENTER);
    header.setPadding(new Insets(10));
    setTop(header);

    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER);
    buildBoardGrid();

    quizPane.setVisible(false);
    questionLabel.getStyleClass().add("label-sub");
    answersBox.setPadding(new Insets(10));
    Button submitButton = new Button("Submit");
    submitButton.getStyleClass().add("button-main");
    skipButton.getStyleClass().add("button-main");

    submitButton.setOnAction(e -> {
      var toggle = answerGroup.getSelectedToggle();
      if (toggle != null && observer != null) {
        observer.onAnswerSelected(((RadioButton) toggle).getText());
      }
    });
    skipButton.setOnAction(e -> {
      if (observer != null) {
        observer.onSkipQuestion();
      }
    });

    skipButton.setDisable(true);

    HBox quizButtons = new HBox(10);
    quizButtons.getChildren().addAll(submitButton, skipButton);
    quizButtons.setAlignment(Pos.CENTER);

    quizPane.getChildren().addAll(questionLabel, answersBox, quizButtons);
    quizPane.setAlignment(Pos.CENTER);
    quizPane.setPadding(new Insets(20));
    quizPane.setStyle("-fx-background-color: rgba(255,255,255,0.9);");

    Button geographyButton = new Button("Geography");
    geographyButton.getStyleClass().add("geography-button");
    Button generalButton = new Button("General Knowledge");
    generalButton.getStyleClass().add("general-button");
    geographyButton.setPrefWidth(150);
    generalButton.setPrefWidth(150);

    geographyButton.setOnAction(e -> {
      if (observer != null) {
        observer.onCategorySelected("Geography");
      }
      showGame();
      rollButton.setDisable(false);
    });
    generalButton.setOnAction(e -> {
      if (observer != null) {
        observer.onCategorySelected("General Knowledge");
      }
      showGame();
      rollButton.setDisable(false);
    });

    categoryPane = new VBox(20, geographyButton, generalButton);
    categoryPane.setAlignment(Pos.CENTER);
    categoryPane.setPadding(new Insets(50));

    StackPane centerStack = new StackPane(categoryPane, boardGrid, quizPane);
    setCenter(centerStack);

    boardGrid.setVisible(false);
    quizPane.setVisible(false);

    VBox instrBox = createInstructionsBox();
    instrBox.getStyleClass().add("instr-box");
    BorderPane.setAlignment(instrBox, Pos.TOP_LEFT);
    setLeft(instrBox);

    playerListBox.setPadding(new Insets(10));
    playerListBox.setAlignment(Pos.TOP_RIGHT);

    Button backButton = new Button("Back");
    backButton.getStyleClass().addAll("button-main-player", "back");
    backButton.setOnAction(e -> {
      if (observer != null) {
        observer.onBack();
      }
    });

    VBox rightPanel = new VBox(30, diceBox, rollBox, playerListBox, backButton);
    rightPanel.setAlignment(Pos.TOP_RIGHT);
    rightPanel.setPadding(new Insets(10));
    setRight(rightPanel);

    fullscreenHandler.setupFullscreenHandling(this);

    updateView();
  }

  /**
   * Updates the view with the current game state. This includes updating the current player, dice
   * values, and player pieces on the board.
   */
  public void updateView() {
    Player current = game.getCurrentplayer();
    currentPlayerLabel.setText(
        current != null ? "Current: " + current.getName() : "Current: -"
    );

    try {
      if (game.getDice() != null) {
        var diceVals = game.getDice().getDiceValues();
        if (diceVals.size() >= 2) {
          updateDice(diceVals.get(0), diceVals.get(1));
        }
      }
    } catch (model.exception.InvalidDiceRollException ignored) {
    }

    boardGrid.getChildren().forEach(node -> {
      if (node instanceof Label lbl) {
        lbl.setStyle("");
        lbl.setGraphic(null);
      }
    });

    if (current != null) {
      String id = String.valueOf(current.getCurrentTile().getTileId());
      boardGrid.getChildren().stream()
          .filter(n -> n instanceof Label)
          .map(n -> (Label) n)
          .filter(l -> l.getText().equals(id))
          .findFirst()
          .ifPresent(l -> l.setStyle(
              "-fx-background-color: linear-gradient(to bottom, rgba(255,255,255,1) 0%,rgba(255,255,255,0) 100%);"));
    }

    for (Player p : players) {
      String id = String.valueOf(p.getCurrentTile().getTileId());
      boardGrid.getChildren().stream()
          .filter(n -> n instanceof Label)
          .map(n -> (Label) n)
          .filter(l -> l.getText().equals(id))
          .findFirst()
          .ifPresent(l -> {
            ImageView iv = new ImageView(PieceImageLoader.get(p.getPiece()));
            iv.setFitWidth(24);
            iv.setFitHeight(24);
            iv.setPreserveRatio(true);
            l.setGraphic(iv);
          });
    }

    playerListBox.getChildren().clear();
    Label hdr = new Label("Players:");
    hdr.getStyleClass().addAll("label-sub", "label-list-header");
    playerListBox.getChildren().add(hdr);
    for (Player p : players) {
      boolean isCurr = p == current;
      playerListBox.getChildren().add(makePlayerListItem(p, isCurr));
    }
  }

  /**
   * Shows the game where the categories are invisible and the grid is visible. The questions are
   * hidden until a player lands on a question tile.
   */
  public void showGame() {
    categoryPane.setVisible(false);
    boardGrid.setVisible(true);
    quizPane.setVisible(false);
  }

  private VBox createInstructionsBox() {
    VBox instructionsBox = new VBox(8);
    instructionsBox.setPadding(new Insets(10));
    instructionsBox.getStyleClass().add("instructions-box");

    Label title = new Label("This is how you play:");
    title.getStyleClass().add("instructions-title");

    VBox instructionPoints = new VBox(6);
    HBox point1 = createInstructionPoint("1", "Press \"Roll dice\" to start the game.");
    HBox point2 = createInstructionPoint("2", "Your piece moves automatically.");
    HBox point3 = createInstructionPoint("3",
        "If the piece lands on or passes a blue tile, you get a question to answer.");
    HBox point4 = createInstructionPoint("4", "Each right answer gives you 1 point.");
    HBox point5 = createInstructionPoint("5",
        "The player with the most points when one player reaches the end wins.");

    instructionPoints.getChildren().addAll(point1, point2, point3, point4, point5);

    instructionsBox.getChildren().addAll(title, instructionPoints);
    instructionsBox.setAlignment(Pos.CENTER);

    return instructionsBox;
  }

  /**
   * Creates an instruction point with a number and text.
   *
   * @param number the number of the instruction
   * @param text   the instruction text
   */
  private HBox createInstructionPoint(String number, String text) {
    HBox pointContainer = new HBox(6);
    pointContainer.setAlignment(Pos.TOP_LEFT);

    Circle numberCircle = new Circle(10);
    numberCircle.getStyleClass().add("number-circle");

    Text numberText = new Text(number);
    numberText.getStyleClass().add("number-text");

    Text instructionText = new Text(text);
    instructionText.getStyleClass().add("instruction-text");
    instructionText.setWrappingWidth(200);

    StackPane numberContainer = new StackPane();
    numberContainer.getChildren().addAll(numberCircle, numberText);

    pointContainer.getChildren().addAll(numberContainer, instructionText);
    return pointContainer;
  }

  private Node makePlayerListItem(Player player, boolean isCurrent) {
    ImageView imageView = new ImageView(PieceImageLoader.get(player.getPiece()));
    imageView.setFitWidth(24);
    imageView.setFitHeight(24);
    imageView.setPreserveRatio(true);

    int score = player.getScore();
    String text = player.getName() + ": " + score + (score == 1 ? " point" : " points");

    Label label = new Label(text, imageView);
    label.setContentDisplay(ContentDisplay.LEFT);
    label.setGraphicTextGap(8);
    label.getStyleClass().add("label-sub");
    if (isCurrent) {
      label.getStyleClass().add("label-bold");
    }
    return label;
  }

  /**
   * Setter for the observer.
   *
   * @param obs the observer to set
   */
  public void setObserver(Observer obs) {
    this.observer = obs;
  }

  /**
   * Called when a player lands on a question tile.
   *
   * @param question the question to display
   * @param options  the list of answer options
   */
  public void showQuestion(String question, List<String> options) {
    rollButton.setDisable(true);
    skipButton.setDisable(false);

    questionLabel.setText(question);
    answersBox.getChildren().clear();
    answerGroup.getToggles().clear();
    for (String opt : options) {
      RadioButton rb = new RadioButton(opt);
      rb.setToggleGroup(answerGroup);
      answersBox.getChildren().add(rb);
    }
    quizPane.setVisible(true);
  }

  /**
   * Hides the question pane and shows the game board.
   */
  public void hideQuestion() {
    quizPane.setVisible(false);
    rollButton.setDisable(false);
    skipButton.setDisable(true);
  }

  /**
   * Sets the question tile IDs and builds the board grid.
   *
   * @param questionTileIds the set of question tile IDs
   */
  public void setQuestionTiles(Set<Integer> questionTileIds) {
    this.questionTileIds = questionTileIds;
    buildBoardGrid();
    updateView();
  }

  /**
   * Builds the board grid with tiles and their styles.
   */
  private void buildBoardGrid() {
    boardGrid.getChildren().clear();
    boardGrid.setGridLinesVisible(false);
    boardGrid.setAlignment(Pos.CENTER);

    for (int id = 1; id <= COLS * ROWS; id++) {
      int row = (id - 1) / COLS;
      int cidx = (id - 1) % COLS;
      int col = (row % 2 == 0) ? cidx : (COLS - 1 - cidx);
      Label cell = new Label(String.valueOf(id));
      cell.setPrefSize(50, 50);
      cell.setAlignment(Pos.CENTER);
      cell.getStyleClass().add("tile");

      if (questionTileIds.contains(id)) {
        cell.getStyleClass().add("tile-question");
      }

      boardGrid.add(cell, col, ROWS - 1 - row);
    }
  }

  /**
   * Updates the dice images based on the rolled values.
   *
   * @param die1Value the value of the first die
   * @param die2Value the value of the second die
   */
  public void updateDice(int die1Value, int die2Value) {
    die1.setImage(diceImages[die1Value]);
    die2.setImage(diceImages[die2Value]);
  }
}


