package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import model.entity.Questions;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.entity.BoardGame;
import model.entity.Player;
import model.factory.BoardGameFactory;
import view.QuizGameView;
import view.QuizGameView.Observer;
import view.ui.BoardGameApp;
import view.ui.MainView;
import view.ui.PlayerView.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizGameViewController implements Observer {
  private static final int COLS = 10, ROWS = 9;
  private int remainingSteps = 0;

  private final Stage stage;
  private final BoardGame game;
  private final QuizGameView view;
  private final Map<Integer, Questions> questionMap = new HashMap<>();

  private boolean questionActive = false;
  private Questions currentQuestion;

  public QuizGameViewController(Stage stage, List<PlayerData> pdList) {
    this.stage = stage;
    this.game = BoardGameFactory.createQuizGame(2);

    var board = game.getBoard();
    for (PlayerData pd : pdList) {
      Player p = new Player(pd.name, board, pd.piece);
      p.setScore(0);
      game.addPlayer(p);
    }

    game.setCurrentPlayer(game.getPlayers().get(0));

    loadQuestions();

    // Lag og vis view
    this.view = new QuizGameView(game);
    view.setObserver(this);
    view.setQuestionTiles(questionMap.keySet());
    view.updateView();

    stage.setScene(new Scene(view, 800, 600));
    stage.setTitle("Quiz Game");
    stage.show();
  }

  /** Fyller questions‐listen */
  private void loadQuestions() {
    try (var is = getClass().getResourceAsStream("/questions.json")) {
      if (is == null) throw new RuntimeException("Could not find questions.json");
      ObjectMapper mapper = new ObjectMapper();
      List<Questions> list = mapper.readValue(
          is,
          new TypeReference<>() {}
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
        remainingSteps   = steps - i - 1;
        currentQuestion  = questionMap.get(tileId);
        questionActive   = true;
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
    if (cur.getCurrentTile().getTileId() == COLS*ROWS) {
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

  /** Når quizen er ferdig */
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
    ButtonType mainMenu  = new ButtonType("Main Menu");
    alert.getButtonTypes().setAll(playAgain, mainMenu);

    DialogPane pane = alert.getDialogPane();
    pane.getStylesheets().add(
        getClass().getResource("/css/style.css").toExternalForm()
    );
    pane.getStyleClass().add("root");

    var result = alert.showAndWait();
    if (result.isPresent() && result.get() == playAgain) {
      restartGame();
    } else {
      backToMainMenu();
    }
  }

  private void restartGame() {
    List<PlayerData> pdList = game.getPlayers().stream()
        .map(p -> new PlayerData(p.getName(), p.getPiece()))
        .collect(Collectors.toList());
    new QuizGameViewController(stage, pdList);
  }

  private void backToMainMenu() {
    try {
      new BoardGameApp().start(stage);
    } catch (Exception e) {
      e.printStackTrace();
      Platform.exit();
    }
  }
}

