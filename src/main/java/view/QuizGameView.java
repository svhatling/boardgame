package view;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.entity.BoardGame;
import model.entity.Player;
import model.util.FullscreenHandler;
import model.util.PieceImageLoader;

public class QuizGameView extends BorderPane {

  public interface Observer {

    void onRollDice();

    void onAnswerSelected(String answer);

    void onSkipQuestion();

    void onCategorySelected(String playerName);
  }

  private Observer observer;
  private final List<Player> players;
  private final BoardGame game;
  private final FullscreenHandler fullscreenHandler;
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

  // Score‐pane (valgfritt plasseringssted)
  private final VBox playerListBox = new VBox(5);

  private final VBox categoryPane;
  private final Button geographyButton = new Button("Geography");
  private final Button generalButton = new Button("General Knowledge");

  public QuizGameView(BoardGame game, FullscreenHandler fullscreenHandler) {
    this.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
    this.players = game.getPlayers();
    this.game = game;
    this.fullscreenHandler = fullscreenHandler;

    rollButton.setDisable(true);

    // --- Last dice‐ikoner ---
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

    // --- Bygg header ---
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

    // --- Bygg brett‐grid ---
    boardGrid.setGridLinesVisible(true);
    boardGrid.setAlignment(Pos.CENTER);
    buildBoardGrid();

    // --- Bygg quiz‐pane (skjult i starten) ---
    quizPane.setVisible(false);
    questionLabel.getStyleClass().add("label-sub");
    answersBox.setPadding(new Insets(10));
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

    quizButtons.getChildren().addAll(submitButton, skipButton);
    quizButtons.setAlignment(Pos.CENTER);

    quizPane.getChildren().addAll(questionLabel, answersBox, quizButtons);
    quizPane.setAlignment(Pos.CENTER);
    quizPane.setPadding(new Insets(20));
    quizPane.setStyle("-fx-background-color: rgba(255,255,255,0.9);");

    geographyButton.getStyleClass().add("geography-button");
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

    // --- Bygg center med kategori, brett og quiz ---
    StackPane centerStack = new StackPane(categoryPane, boardGrid, quizPane);
    setCenter(centerStack);

    // Skjul brett og quiz inntil kategori valgt
    boardGrid.setVisible(false);
    quizPane.setVisible(false);

    // --- Instruksjons-boks øverst til venstre ---
    VBox instrBox = createInstructionsBox();
    instrBox.getStyleClass().add("instr-box");
    BorderPane.setAlignment(instrBox, Pos.TOP_LEFT);
    setLeft(instrBox);

    playerListBox.setPadding(new Insets(10));
    playerListBox.setAlignment(Pos.TOP_RIGHT);

    VBox rightPanel = new VBox(30, diceBox, rollBox, playerListBox);
    rightPanel.setAlignment(Pos.TOP_RIGHT);
    rightPanel.setPadding(new Insets(10));
    setRight(rightPanel);

    fullscreenHandler.setupFullscreenHandling(this);

    updateView();
  }

  public void updateView() {
    // Current player
    Player current = game.getCurrentplayer();
    currentPlayerLabel.setText(
        current != null ? "Current: " + current.getName() : "Current: -"
    );

    // Dice
    try {
      if (game.getDice() != null) {
        var diceVals = game.getDice().getDiceValues();
        if (diceVals.size() >= 2) {
          updateDice(diceVals.get(0), diceVals.get(1));
        }
      }
    } catch (model.exception.InvalidDiceRollException ignored) {
    }

    // Clear styles and graphics
    boardGrid.getChildren().forEach(node -> {
      if (node instanceof Label) {
        Label lbl = (Label) node;
        lbl.setStyle("");
        lbl.setGraphic(null);
      }
    });

    // Highlight current tile
    if (current != null) {
      String id = String.valueOf(current.getCurrentTile().getTileId());
      boardGrid.getChildren().stream()
          .filter(n -> n instanceof Label)
          .map(n -> (Label) n)
          .filter(l -> l.getText().equals(id))
          .findFirst()
          .ifPresent(l -> l.setStyle("-fx-background-color: white;"));
    }

    // Place all pieces
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

    // Update player list
    playerListBox.getChildren().clear();
    Label hdr = new Label("Players:");
    hdr.getStyleClass().addAll("label-sub", "label-list-header");
    playerListBox.getChildren().add(hdr);
    for (Player p : players) {
      boolean isCurr = p == current;
      playerListBox.getChildren().add(makePlayerListItem(p, isCurr));
    }
  }

  public void showGame() {
    categoryPane.setVisible(false);
    boardGrid.setVisible(true);
    quizPane.setVisible(false);
  }

  private VBox createInstructionsBox() {
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
    HBox point1 = createInstructionPoint("1", "Press \"Roll dice\" to start the game.");

    // Punkt 2
    HBox point2 = createInstructionPoint("2", "Your piece moves automatically.");

    // Punkt 3
    HBox point3 = createInstructionPoint("3",
        "If the piece lands on or passes a blue tile, you get a question to answer.");

    // Punkt 4
    HBox point4 = createInstructionPoint("4", "Each right answer gives you 1 point.");

    // Punkt 5
    HBox point5 = createInstructionPoint("5",
        "The player with the most points when one player reaches the end wins.");

    // Legg til alle instruksjonspunktene
    instructionPoints.getChildren().addAll(point1, point2, point3, point4, point5);

    // Legg til alle elementer i hovedcontaineren
    instructionsBox.getChildren().addAll(title, instructionPoints);
    instructionsBox.setAlignment(Pos.CENTER);

    return instructionsBox;
  }

  /**
   * Oppretter et enkelt instruksjonspunkt med nummerering og tekst
   */
  private HBox createInstructionPoint(String number, String text) {
    HBox pointContainer = new HBox(6);
    pointContainer.setAlignment(Pos.TOP_LEFT);

    // Nummersirkel
    Circle numberCircle = new Circle(10);
    numberCircle.getStyleClass().add("number-circle");

    // Nummertekst
    Text numberText = new Text(number);
    numberText.getStyleClass().add("number-text");

    // Instruksjonstekst
    Text instructionText = new Text(text);
    instructionText.getStyleClass().add("instruction-text");
    instructionText.setWrappingWidth(200);

    // Wrapper for sirkel og nummer (for å sentrere nummeret i sirkelen)
    StackPane numberContainer = new StackPane();
    numberContainer.getChildren().addAll(numberCircle, numberText);

    pointContainer.getChildren().addAll(numberContainer, instructionText);
    return pointContainer;
  }

  /**
   * Oppretter spillerlisten med ikoner og spillernavn (Synkronisert med BoardGameView)
   */
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
   * Koble til controller
   */
  public void setObserver(Observer obs) {
    this.observer = obs;
  }

  /**
   * Kalles for å vise spørsmål‐helt over brettet
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
   * Skjuler spørsmåls‐panelet igjen
   */
  public void hideQuestion() {
    quizPane.setVisible(false);
    rollButton.setDisable(false);
    skipButton.setDisable(true);
  }

  public void setQuestionTiles(Set<Integer> questionTileIds) {
    this.questionTileIds = questionTileIds;
    buildBoardGrid();
    updateView();
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


