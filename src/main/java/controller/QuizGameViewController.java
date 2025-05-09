package controller;

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

/** Enkel dataklasse for spørsmål */
class Question {
  final String text;
  final List<String> options;
  final String correct;
  Question(String text, List<String> options, String correct) {
    this.text    = text;
    this.options = options;
    this.correct = correct;
  }
}

public class QuizGameViewController implements Observer {
  private final Stage stage;
  private final List<Player> players = new ArrayList<>();
  private final List<Question> questions = new ArrayList<>();
  private final QuizGameView view;

  private int currentQuestion = 0;
  private int currentPlayerIdx = 0;

  public QuizGameViewController(Stage stage, List<PlayerData> pdList) {
    this.stage = stage;

    BoardGame quizGame = BoardGameFactory.createQuizGame(2);
    var board = quizGame.getBoard();

    // opprett Player‐objekter (må ha getScore()/incrementScore() i Player!)
    for (var pd : pdList) {
      Player p = new Player(pd.name, board, pd.piece);
      p.setScore(0);
      players.add(p);
    }

    loadQuestions();
    this.view = new QuizGameView(players);
    this.view.setObserver(this);
    showCurrent();

    stage.setScene(new Scene(view, 800, 600));
    stage.setTitle("Quiz Game");
    stage.show();
  }

  /** Fyller questions‐listen */
  private void loadQuestions() {
    questions.add(new Question(
        "Hva er hovedstaden i Norge?",
        List.of("Oslo","Bergen","Trondheim","Stavanger"),
        "Oslo"
    ));
    questions.add(new Question(
        "Hvor mange kontinenter har vi?",
        List.of("5","6","7","8"),
        "7"
    ));
    // ... legg til flere
  }

  /** Viser det neste spørsmålet */
  private void showCurrent() {
    Question q = questions.get(currentQuestion);
    view.updateQuestion(q.text, q.options);
    view.updateScores(players);
  }

  @Override
  public void onAnswerSelected(String answer) {
    Question q = questions.get(currentQuestion);
    Player p  = players.get(currentPlayerIdx);

    if (q.correct.equals(answer)) {
      p.incrementScore();
    }

    advance();
  }

  @Override
  public void onSkipQuestion() {
    advance();
  }

  /** Går videre til neste spiller/spørsmål, eller avslutter */
  private void advance() {
    currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
    if (currentPlayerIdx == 0) {
      currentQuestion++;
    }
    if (currentQuestion < questions.size()) {
      showCurrent();
    } else {
      showEndDialog();
    }
  }

  /** Når quizen er ferdig */
  private void showEndDialog() {
    String winner = players.stream()
        .max((a,b) -> Integer.compare(a.getScore(), b.getScore()))
        .map(Player::getName)
        .orElse("Ingen");

    Alert alert = new Alert(Alert.AlertType.NONE,
        winner + " wins!",
        ButtonType.OK, new ButtonType("Main Menu")
    );
    alert.setTitle("Quiz Over");
    Optional<ButtonType> res = alert.showAndWait();
    // Her kan du kalle MainMenu‐flowen din eller restart‐logikk
    if (res.isPresent() && res.get().getText().equals("Main Menu")) {
      new view.ui.BoardGameApp().start(stage);
    }
  }
}

