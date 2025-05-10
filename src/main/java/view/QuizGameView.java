package view;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import model.entity.Player;
import model.util.PieceImageLoader;

public class QuizGameView extends BorderPane {

  public interface Observer {

    void onRollDice();

    void onTileClicked(int tileId);

    void onAnswerSelected(String answer);

    void onSkipQuestion();
  }

  private Observer observer;

  private Set<Integer> questionTileIds = Collections.emptySet();

  // Størrelse på grid
  private static final int COLS = 10, ROWS = 9;

  // Grid for brettet
  private final GridPane boardGrid = new GridPane();
  private final Map<Integer, Label> tileLabels = new HashMap<>();

  // UI‐elementer for quiz
  private final Label currentPlayerLabel = new Label("Current player");
  private final ImageView die1 = new ImageView();
  private final ImageView die2 = new ImageView();
  private final Image[] diceImages = new Image[7];
  private final Button rollButton = new Button("Roll dice");

  private final VBox quizPane = new VBox(10);
  private final Label questionLabel = new Label();
  private final ToggleGroup answerGroup = new ToggleGroup();
  private final VBox answersBox = new VBox(10);
  private final HBox quizButtons = new HBox(10);
  private final Button submitButton = new Button("Submit");
  private final Button skipButton = new Button("Skip");

  private final List<Player> players;

  // Score‐pane (valgfritt plasseringssted)
  private final VBox scorePane = new VBox(10);

  public QuizGameView(List<Player> players) {
    this.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
    this.players = players;

    // --- Last dice‐ikoner ---
    for (int f = 1; f <= 6; f++) {
      diceImages[f] = new Image(
          Objects.requireNonNull(getClass().getResourceAsStream("/icons/dice" + f + ".png"))
      );
    }
    die1.setFitWidth(40);
    die1.setFitHeight(40);
    die2.setFitWidth(40);
    die2.setFitHeight(40);
    die1.setImage(diceImages[1]);
    die2.setImage(diceImages[1]);

    // --- Bygg header ---
    currentPlayerLabel.getStyleClass().add("label-sub");
    rollButton.getStyleClass().add("roll-dice-button");
    rollButton.setOnAction(e -> {
      if (observer != null) {
        observer.onRollDice();
      }
    });
    HBox diceBox = new HBox(5, die1, die2, rollButton);
    diceBox.setAlignment(Pos.CENTER_RIGHT);

    Label title = new Label("Quiz Board");
    title.getStyleClass().add("label-title");
    VBox header = new VBox(5, title, currentPlayerLabel, diceBox);
    header.setAlignment(Pos.CENTER);
    header.setPadding(new Insets(10));
    setTop(header);

    // --- Bygg brett‐grid ---
    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER);
    buildBoardGrid();
    StackPane centerStack = new StackPane(boardGrid, quizPane);
    setCenter(centerStack);

    // --- Bygg quiz‐pane (skjult i starten) ---
    quizPane.setVisible(false);
    questionLabel.getStyleClass().add("label-sub");
    answersBox.setPadding(new Insets(10));
    submitButton.getStyleClass().add("button-main");
    skipButton.getStyleClass().add("button-secondary");

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
    quizButtons.getChildren().addAll(submitButton, skipButton);
    quizButtons.setAlignment(Pos.CENTER);

    quizPane.getChildren().addAll(questionLabel, answersBox, quizButtons);
    quizPane.setAlignment(Pos.CENTER);
    quizPane.setPadding(new Insets(20));
    quizPane.setStyle("-fx-background-color: rgba(255,255,255,0.9);");

    // --- Score‐pane til høyre (kan fjerne om uønsket) ---
    scorePane.setPadding(new Insets(10));
    scorePane.setAlignment(Pos.TOP_RIGHT);
    setRight(scorePane);
  }

  /**
   * Koble til controller
   */
  public void setObserver(Observer obs) {
    this.observer = obs;
  }

  public void updatePiecePositions() {
    // Fjern alle ikoner:
    tileLabels.values().forEach(cell -> cell.setGraphic(null));
    // Plasser alle spillernes brikker:
    for (Player p : players) {
      int id = p.getCurrentTile().getTileId();
      ImageView iv = new ImageView(PieceImageLoader.get(p.getPiece()));
      iv.setFitWidth(24);
      iv.setFitHeight(24);
      tileLabels.get(id).setGraphic(iv);
    }
    // Oppdater header:
    currentPlayerLabel.setText("Current: " + players.stream()
        .filter(pl -> pl.isCurrent())
        .findFirst().map(Player::getName).orElse("-"));
  }

  /**
   * Kalles for å vise spørsmål‐helt over brettet
   */
  public void showQuestion(String question, List<String> options) {
    quizPane.setVisible(true);
    questionLabel.setText(question);
    answersBox.getChildren().clear();
    answerGroup.getToggles().clear();
    for (String opt : options) {
      RadioButton rb = new RadioButton(opt);
      rb.setToggleGroup(answerGroup);
      answersBox.getChildren().add(rb);
    }
  }

  /**
   * Skjuler spørsmåls‐panelet igjen
   */
  public void hideQuestion() {
    quizPane.setVisible(false);
  }

  /**
   * Oppdaterer et spørsmål + alternativer
   */
  public void updateQuestion(String question, List<String> options) {
    questionLabel.setText(question);
    answersBox.getChildren().clear();
    answerGroup.getToggles().clear();
    for (var opt : options) {
      RadioButton rb = new RadioButton(opt);
      rb.setToggleGroup(answerGroup);
      answersBox.getChildren().add(rb);
    }
  }

  /**
   * Oppdaterer scorepane (hvis brukt)
   */
  public void updateScores(List<?> playersWithScore) {
    scorePane.getChildren().clear();
    Label hdr = new Label("Scores");
    hdr.getStyleClass().addAll("label-sub", "label-list-header");
    scorePane.getChildren().add(hdr);
    // forventer at spiller‐objektene har getName() og getScore()
    for (Object p : playersWithScore) {
      // kast til korrekt type om nødvendig
      try {
        var pl = (model.entity.Player) p;
        Label l = new Label(pl.getName() + ": " + pl.getScore());
        l.getStyleClass().add("label-sub");
        scorePane.getChildren().add(l);
      } catch (ClassCastException ignored) {
      }
    }
  }

  public void setQuestionTiles(Set<Integer> questionTileIds) {
    this.questionTileIds = questionTileIds;
    buildBoardGrid();
    updatePiecePositions();
  }

  /**
   * Lager en 10×9 Grid med tile‐id og border
   */
  private void buildBoardGrid() {
    boardGrid.getChildren().clear();
    tileLabels.clear();
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

      // Marker klikk bare om du vil – ikke nødvendig for landing
      boardGrid.add(cell, col, ROWS - 1 - row);
      tileLabels.put(id, cell);
    }
  }

  public void updateDice(int die1Value, int die2Value) {
    die1.setImage(diceImages[die1Value]);
    die2.setImage(diceImages[die2Value]);
  }
}


