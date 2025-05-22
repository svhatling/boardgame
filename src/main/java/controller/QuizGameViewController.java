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
import java.util.Optional;
import java.util.Set;
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
import view.ui.PlayerView.PlayerData;

public class QuizGameViewController implements Observer {

  private static final int COLS = 10, ROWS = 9;
  private int remainingSteps = 0;

  private final Stage stage;
  private final BoardGame game;
  private final QuizGameView view;
  private final FullscreenHandler fullscreenHandler;
  private final Map<Integer, Questions> questionMap = new HashMap<>();

  private boolean questionActive = false;
  private Questions currentQuestion;

  public QuizGameViewController(Stage stage, List<PlayerData> pdList, FullscreenHandler fullscreenHandler) {
    this.stage = stage;
    this.fullscreenHandler = fullscreenHandler;
    this.game = BoardGameFactory.createQuizGame(pdList.size());

    var board = game.getBoard();
    for (PlayerData pd : pdList) {
      Player p = new Player(pd.name, board, pd.piece);
      p.setScore(0);
      game.addPlayer(p);
    }

    game.setCurrentPlayer(game.getPlayers().get(0));

    loadQuestions();

    // Lag og vis view
    this.view = new QuizGameView(game, fullscreenHandler);
    view.setObserver(this);
    view.setQuestionTiles(questionMap.keySet());
    view.updateView();

    stage.setScene(new Scene(view, 800, 600));
    stage.setTitle("Quiz Game");
    stage.show();
  }

  /**
   * Fyller questions‐listen
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

  @Override
  public void onRollDice() {
    if (questionActive) {
      return;
    }

    // 1) Rull én gang – får summen av de to terningene
    int sum = game.getDice().rollDice();

    // 2) Hent de to verdiene som ble rullet
    List<Integer> vals = game.getDice().getDiceValues();
    int v1 = vals.get(0), v2 = vals.get(1);

    moveAndMaybeAsk(sum);
  }

  private void moveAndMaybeAsk(int steps) {
    Player cur = game.getCurrentplayer();
    for (int i = 0; i < steps; i++) {
      cur.move(1);
      view.updateView();
      int tileId = cur.getCurrentTile().getTileId();
      if (questionMap.containsKey(tileId)) {
        // still inn rest-steg og vis spørsmål
        remainingSteps = steps - i - 1;
        currentQuestion = questionMap.get(tileId);
        questionActive = true;
        view.showQuestion(currentQuestion.getQuestion(), currentQuestion.getOptions());
        return;
      }
    }
    nextTurnOrEnd();
  }

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
      // Ingen flere skritt å ta, gå videre til neste spiller
      nextTurnOrEnd();
    }
  }

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
      // Ingen flere skritt å ta, gå videre til neste spiller
      nextTurnOrEnd();
    }
  }

  private void nextTurnOrEnd() {
    // Sjekk om noen har vunnet (har nådd siste rute)
    Player cur = game.getCurrentplayer();
    if (cur.getCurrentTile().getTileId() == COLS * ROWS) {
      // Vis slutt‐dialog basert på høyest score
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
   * Når quizen er ferdig
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

    ButtonType playAgain = new ButtonType("Play Again");
    ButtonType mainMenu = new ButtonType("Main Menu");
    alert.getButtonTypes().setAll(playAgain, mainMenu);

    DialogPane pane = alert.getDialogPane();
    pane.getStylesheets().add(
        getClass().getResource("/css/style.css").toExternalForm()
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
        e.printStackTrace();
      }
    }
  }

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
        throw new RuntimeException("Kunne ikke finne fil: " + filename);
      }
      // Deserialiser JSON-array til List<Questions>
      List<Questions> list = mapper.readValue(is, new TypeReference<>() {});
      for (Questions q : list) {
        questionMap.put(q.getTileId(), q);
        tileIds.add(q.getTileId());
      }
    } catch (IOException e) {
      e.printStackTrace();
      // Her kan du eventuelt vise en feilmelding i GUI-en
    }

    // Fortell view hvilke ruter som gir spørsmål
    view.setQuestionTiles(tileIds);

    // Skjul kategori-pane og vis brett
    view.showGame();
    view.updateView();
  }
}

