package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.util.BoardConfigLoader.TileConfig; // bare for orientering, vi tegner ingen lister her

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizGameView extends BorderPane {

  public interface Observer {
    void onAnswerSelected(String answer);
    void onSkipQuestion();
  }

  private Observer observer;

  // Størrelse på grid
  private static final int COLS = 10, ROWS = 9;

  // Grid for brettet
  private final GridPane boardGrid = new GridPane();
  private final Map<Integer, Label> tileLabels = new HashMap<>();

  // UI‐elementer for quiz
  private final Label questionLabel = new Label();
  private final ToggleGroup answerGroup = new ToggleGroup();
  private final VBox answersBox = new VBox(10);
  private final Button submitButton = new Button("Submit");
  private final Button skipButton   = new Button("Skip");

  // Score‐pane (valgfritt plasseringssted)
  private final VBox scorePane = new VBox(10);

  public QuizGameView(List<?> players) {
    // --- Bygg brett‐grid likt BoardGameView ---
    buildBoardGrid();

    // --- Quiz‐UI i et eget panel på toppen ---
    questionLabel.getStyleClass().add("label-sub");
    answersBox.setPadding(new Insets(10));
    answersBox.setAlignment(Pos.TOP_LEFT);

    submitButton.getStyleClass().add("button-main");
    submitButton.setOnAction(e -> {
      if (observer != null) {
        var sel = answerGroup.getSelectedToggle();
        if (sel != null) {
          observer.onAnswerSelected(((RadioButton)sel).getText());
        }
      }
    });
    skipButton.getStyleClass().add("button-secondary");
    skipButton.setOnAction(e -> {
      if (observer != null) observer.onSkipQuestion();
    });

    HBox buttonBox = new HBox(10, submitButton, skipButton);
    buttonBox.setAlignment(Pos.CENTER);

    VBox quizPane = new VBox(20, questionLabel, answersBox, buttonBox);
    quizPane.setAlignment(Pos.TOP_CENTER);
    quizPane.setPadding(new Insets(20));
    quizPane.setMaxWidth(300);
    quizPane.setStyle("-fx-background-color: rgba(255,255,255,0.8);");
    // semitransparent bakgrunn så grid synes gjennom

    // --- Legg grid og quizPane i et StackPane ---
    StackPane centerStack = new StackPane(boardGrid, quizPane);
    centerStack.setAlignment(Pos.CENTER);
    setCenter(centerStack);

    // --- Score‐pane til høyre (kan fjerne om uønsket) ---
    scorePane.setPadding(new Insets(10));
    scorePane.setAlignment(Pos.TOP_RIGHT);
    setRight(scorePane);
  }

  /** Koble til controller */
  public void setObserver(Observer obs) {
    this.observer = obs;
  }

  /** Oppdaterer et spørsmål + alternativer */
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

  /** Oppdaterer scorepane (hvis brukt) */
  public void updateScores(List<?> playersWithScore) {
    scorePane.getChildren().clear();
    Label hdr = new Label("Scores");
    hdr.getStyleClass().addAll("label-sub","label-list-header");
    scorePane.getChildren().add(hdr);
    // forventer at spiller‐objektene har getName() og getScore()
    for (Object p : playersWithScore) {
      // kast til korrekt type om nødvendig
      try {
        var pl = (model.entity.Player)p;
        Label l = new Label(pl.getName() + ": " + pl.getScore());
        l.getStyleClass().add("label-sub");
        scorePane.getChildren().add(l);
      } catch (ClassCastException ignored) {}
    }
  }

  /** Lager en 10×9 Grid med tile‐id og border */
  private void buildBoardGrid() {
    boardGrid.getChildren().clear();
    tileLabels.clear();
    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER);
    for (int id = 1; id <= COLS*ROWS; id++) {
      int row = (id-1) / COLS;
      int colIdx = (id-1) % COLS;
      int col = (row % 2 == 0) ? colIdx : (COLS - 1 - colIdx);

      Label cell = new Label(String.valueOf(id));
      cell.setPrefSize(50,50);
      cell.setAlignment(Pos.CENTER);
      boardGrid.add(cell, col, ROWS-1-row);
      tileLabels.put(id, cell);
    }
  }
}


