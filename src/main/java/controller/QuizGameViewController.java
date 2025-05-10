package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
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
import view.ui.PlayerView.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizGameViewController implements Observer {
  private final static int COLS = 10, ROWS = 9;
  private int remainingSteps = 0;

  private final Stage stage;
  private final BoardGame game;
  private final List<Player> players = new ArrayList<>();
  private final List<Questions> questions = new ArrayList<>();
  private final QuizGameView view;
  private final Map<Integer, Questions> questionMap = new HashMap<>();

  public QuizGameViewController(Stage stage, List<PlayerData> pdList) {
    this.stage = stage;
    this.game = BoardGameFactory.createQuizGame(2);

    var board = game.getBoard();
    for (var pd : pdList) {
      Player p = new Player(pd.name, board, pd.piece);
      p.setScore(0);
      game.addPlayer(p);
      players.add(p);
    }

    game.setCurrentPlayer(players.get(0));

    loadQuestions();

    // Lag og vis view
    this.view = new QuizGameView(players);
    view.setObserver(this);
    view.setQuestionTiles(questionMap.keySet());
    view.updatePiecePositions();

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
          new TypeReference<List<Questions>>() {}
      );
      // Legg dem i et map for enkel oppslag
      questions.clear();
      questions.addAll(list);
      questionMap.clear();
      for (Questions q : list) {
        questionMap.put(q.getTileId(), q);
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not load question", e);
    }
  }

  @Override
  public void onRollDice() {
    // 1) Rull én gang – får summen av de to terningene
    int sum = game.getDice().rollDice();

    // 2) Hent de to verdiene som ble rullet
    List<Integer> vals = game.getDice().getDiceValues();
    int v1 = vals.get(0), v2 = vals.get(1);

    // 3) Oppdater ternings‐ikonene
    view.updateDice(v1, v2);

    // 4) Fortsett å flytte brikken sum steg
    int steps = sum;
    Player cur = game.getCurrentplayer();

    for (int i = 0; i < steps; i++) {
      cur.move(1);
      view.updatePiecePositions();

      int tileId = cur.getCurrentTile().getTileId();
      if (questionMap.containsKey(tileId)) {
        var q = questionMap.get(tileId);
        view.showQuestion(q.getQuestion(), q.getOptions());
        remainingSteps = steps - i - 1;
        return;
      }
    }

    nextTurnOrEnd();
  }

  @Override
  public void onAnswerSelected(String answer) {
    view.hideQuestion();
    Player cur = game.getCurrentplayer();
    Questions q  = questionMap.get(cur.getCurrentTile().getTileId());
    if (q.getAnswer().equals(answer)) cur.incrementScore();

    if (remainingSteps > 0) {
      int r = remainingSteps;
      remainingSteps = 0;
      continueMovement(r);
    } else {
      // Ingen flere skritt å ta, gå videre til neste spiller
      nextTurnOrEnd();
    }
  }

  @Override
  public void onSkipQuestion() {
    view.hideQuestion();
    if (remainingSteps > 0) {
      int r = remainingSteps;
      remainingSteps = 0;
      continueMovement(r);
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
    var all = game.getPlayers();
    int index = all.indexOf(cur);
    game.setCurrentPlayer(all.get((index + 1) % all.size()));
    view.updatePiecePositions();
  }

  /** Når quizen er ferdig */
  private void showEndDialog() {
    Optional<Player> winner = players.stream()
        .max(Comparator.comparingInt(Player::getScore));

    String message = winner
        .map(p -> p.getName() + " wins with " + p.getScore() + " points!")
        .orElse("No winner");

    Alert alert = new Alert(Alert.AlertType.NONE,
        message,
        ButtonType.OK, new ButtonType("Main Menu")
    );
    alert.setTitle("Quiz Over");
    alert.showAndWait().ifPresent(bt -> {
      new view.ui.BoardGameApp().start(stage);
    });
  }

  @Override
  public void onTileClicked(int tileId) {
  }

  private void continueMovement(int steps) {
    Player cur = game.getCurrentplayer();
    for (int i = 0; i < steps; i++) {
      cur.move(1);
      view.updatePiecePositions();
      int landed = cur.getCurrentTile().getTileId();
      if (questionMap.containsKey(landed)) {
        remainingSteps = steps - i - 1;
        Questions q = questionMap.get(landed);
        view.showQuestion(q.getQuestion(), q.getOptions());
        return;
      }
    }
    // Når alle steg er brukt uten spørsmål:
    nextTurnOrEnd();
  }
}

