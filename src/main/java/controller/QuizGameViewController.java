package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import model.entity.BoardGame;
import model.entity.Player;
import model.entity.Questions;
import model.factory.BoardGameFactory;
import model.util.FullscreenHandler;
import view.QuizGameView;
import view.QuizGameView.Observer;
import view.ui.BoardGameApp;
import view.PlayerView.PlayerData;

/**
 * Controller for the quiz game view.
 * Handles events from the view and updates the game state.
 */
public class QuizGameViewController implements Observer {

  private static final int COLS = 10, ROWS = 9;
  private int remainingSteps = 0;

  private static final Logger logger =
      Logger.getLogger(QuizGameViewController.class.getName());

  private final Stage stage;
  private final Scene previousScene;
  private final BoardGame game;
  private final QuizGameView view;
  private final FullscreenHandler fullscreenHandler;
  private final Map<Integer, Questions> questionMap = new HashMap<>();

  private boolean questionActive = false;
  private Questions currentQuestion;

  /**
   * Constructor for the QuizGameViewController.
   *
   * @param stage            the main application window
   * @param pdList           list of player data
   * @param fullscreenHandler handler for fullscreen mode
   */
  public QuizGameViewController(Stage stage, List<PlayerData> pdList, FullscreenHandler fullscreenHandler) {
    this.stage = stage;
    this.previousScene = stage.getScene();
    this.fullscreenHandler = fullscreenHandler;
    this.game = BoardGameFactory.createQuizGame(pdList.size());

    var board = game.getBoard();
    for (PlayerData pd : pdList) {
      Player p = new Player(pd.name(), board, pd.piece());
      p.setScore(0);
      game.addPlayer(p);
    }

    game.setCurrentPlayer(game.getPlayers().getFirst());

    loadQuestions();

    this.view = new QuizGameView(game, fullscreenHandler);
    view.setObserver(this);
    view.setQuestionTiles(questionMap.keySet());
    view.updateView();

    stage.setScene(new Scene(view, 800, 600));
    stage.setTitle("Quiz Game");
    stage.show();
  }

  /**
   * Loads the list of questions with the questions from a JSON file.
   */
  private void loadQuestions() {
    try (var is = getClass().getResourceAsStream("/config/quiz/questions.json")) {
      if (is == null) {
        throw new RuntimeException("Could not find questions.json");
      }
      ObjectMapper mapper = new ObjectMapper();
      List<Questions> list = mapper.readValue(
          is,
          new TypeReference<>() {
          }
      );
      questionMap.clear();
      list.forEach(q -> questionMap.put(q.getTileId(), q));
    } catch (IOException e) {
      throw new RuntimeException("Could not load question", e);
    }
  }

  /**
   * Called when the user clicks the "Back" button in QuizGameView.
   * Returns to the previous scene.
   */
  @Override
  public void onBack() {
    if (previousScene != null) {
      stage.setScene(previousScene);
    } else {
      System.out.println("No previous scene to go back to.");
    }
  }

  /**
   * Called when the user clicks the "Roll Dice" button.
   * Rolls the dice and moves the player.
   */
  @Override
  public void onRollDice() {
    if (questionActive) {
      return;
    }
    int sum = game.getDice().rollDice();

    moveAndMaybeAsk(sum);
  }

  private void moveAndMaybeAsk(int steps) {
    Player cur = game.getCurrentplayer();
    for (int i = 0; i < steps; i++) {
      cur.move(1);
      view.updateView();
      int tileId = cur.getCurrentTile().getTileId();
      if (questionMap.containsKey(tileId)) {
        remainingSteps = steps - i - 1;
        currentQuestion = questionMap.get(tileId);
        questionActive = true;
        view.showQuestion(currentQuestion.getQuestion(), currentQuestion.getOptions());
        return;
      }
    }
    nextTurnOrEnd();
  }

  /**
   * Called when the user selects an answer to the question.
   *
   * @param answer the selected answer
   */
  @Override
  public void onAnswerSelected(String answer) {
    view.hideQuestion();
    questionActive = false;

    if (currentQuestion.getAnswer().equals(answer)) {
      game.getCurrentplayer().incrementScore();
    }
    currentQuestion = null;

    if (remainingSteps > 0) {
      int r = remainingSteps;
      remainingSteps = 0;
      moveAndMaybeAsk(r);
    } else {
      nextTurnOrEnd();
    }
  }

  /**
   * Called when the user skips the question.
   */
  @Override
  public void onSkipQuestion() {
    view.hideQuestion();
    questionActive = false;
    currentQuestion = null;

    if (remainingSteps > 0) {
      int r = remainingSteps;
      remainingSteps = 0;
      moveAndMaybeAsk(r);
    } else {
      nextTurnOrEnd();
    }
  }

  /**
   * Called when the user is done with a question.
   */
  private void nextTurnOrEnd() {
    Player cur = game.getCurrentplayer();
    if (cur.getCurrentTile().getTileId() == COLS * ROWS) {
      showEndDialog();
      return;
    }
    List<Player> all = game.getPlayers();
    int index = all.indexOf(cur);
    Player nextPlayer = all.get((index + 1) % all.size());
    game.setCurrentPlayer(nextPlayer);
    view.updateView();
  }

  /**
   * A dialog that shows up when the game is over.
   */
  private void showEndDialog() {
    Optional<Player> winner = game.getPlayers().stream()
        .max(Comparator.comparingInt(Player::getScore));

    String message = winner
        .map(p -> p.getName() + " wins with " + p.getScore() + " points!")
        .orElse("No winner");

    Alert alert = new Alert(Alert.AlertType.NONE);
    alert.setTitle("Game Over");
    alert.setHeaderText(message);

    ButtonType mainMenu = new ButtonType("Main Menu");
    ButtonType playAgain = new ButtonType("Play Again");

    alert.getButtonTypes().setAll(playAgain, mainMenu);

    DialogPane pane = alert.getDialogPane();
    pane.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm()
    );
    pane.getStyleClass().add("root");

    var result = alert.showAndWait();
    if (result.isPresent() && result.get() == playAgain) {
      List<PlayerData> pdList = game.getPlayers().stream()
          .map(p -> new PlayerData(p.getName(), p.getPiece()))
          .collect(Collectors.toList());
      new QuizGameViewController(stage, pdList, fullscreenHandler);
    } else {
      try {
        new BoardGameApp().start(stage);
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Error starting board game app", e);
      }
    }
  }

  /**
   * Called when the user selects a category.
   *
   * @param category the selected category
   */
  @Override
  public void onCategorySelected(String category) {
    String filename = category.equals("Geography")
        ? "/config/quiz/geography_questions.json"
        : "/config/quiz/questions.json";

    questionMap.clear();
    Set<Integer> tileIds = new HashSet<>();

    ObjectMapper mapper = new ObjectMapper();
    try (InputStream is = getClass().getResourceAsStream(filename)) {
      if (is == null) {
        throw new RuntimeException("Could not find file: " + filename);
      }
      List<Questions> list = mapper.readValue(is, new TypeReference<>() {});
      for (Questions q : list) {
        questionMap.put(q.getTileId(), q);
        tileIds.add(q.getTileId());
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error loading questions", e);
    }

    view.setQuestionTiles(tileIds);

    view.showGame();
    view.updateView();
  }
}

