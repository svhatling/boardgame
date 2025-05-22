package controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import model.entity.BoardGame;
import model.entity.Player;
import model.factory.BoardGameFactory;
import model.logic.GameType;
import model.util.FullscreenHandler;
import view.BoardGameView;
import view.BoardGameView.Observer;
import view.PlayerView.PlayerData;
import view.ui.MainView;

/**
 * Controller for the boardgame screen.
 * Handles board view actions and updates the game.
 */
public class BoardGameViewController implements Observer {
  private final Stage primaryStage;
  private final List<PlayerData> playersData;
  private final GameType gameType;
  private final Scene previousScene;
  private final FullscreenHandler fullscreenHandler;
  private final BoardGame game;
  private final BoardGameView view;
  private static final Logger logger =
      Logger.getLogger(BoardGameViewController.class.getName());

  /**
   * Builds controller, model, view, and displays the boardgame.
   *
   * @param primaryStage the main application window
   * @param playersData  the list of players with pieces
   * @param gameType     the type of game to be played
   * @param fullscreenHandler the handler for fullscreen mode
   */
  public BoardGameViewController(Stage primaryStage, List<PlayerData> playersData, GameType gameType,
      FullscreenHandler fullscreenHandler) {
    this.primaryStage = primaryStage;
    this.previousScene = primaryStage.getScene();
    this.playersData  = playersData;
    this.gameType     = gameType;
    this.fullscreenHandler = fullscreenHandler;

    switch (gameType) {
      case SNAKES_AND_LADDERS:
        game = BoardGameFactory.createSnakesAndLadders(playersData.size());
        break;
      case QUIZ:
        game = BoardGameFactory.createQuizGame(playersData.size());
        break;
      default:
        throw new IllegalArgumentException("Invalid game type: " + gameType);
    }

    for (PlayerData pd : playersData) {
      game.addPlayer(new Player(pd.name(), game.getBoard(), pd.piece()));
    }
    game.setCurrentPlayer(game.getPlayers().getFirst());

    // Initializes and shows the view
    view = new BoardGameView(game, fullscreenHandler);
    view.setObserver(this);

    boolean wasFullScreen = primaryStage.isFullScreen();
    boolean wasMaximized = primaryStage.isMaximized();

    primaryStage.setScene(new Scene(view, 800, 600));
    primaryStage.setTitle(
        gameType == GameType.SNAKES_AND_LADDERS
        ? "Ladders & Snakes"
        : "Quiz");

    if (wasFullScreen) {
      primaryStage.setFullScreen(true);
    } else {
      primaryStage.setMaximized(wasMaximized);
    }

    primaryStage.show();
  }

  /**
   * Called when the user clicks the "Back" button in BoardGameView.
   * Returns to the previous scene.
   */
  @Override
  public void onBack() {
    if (previousScene != null) {
      primaryStage.setScene(previousScene);
    } else {
      System.out.println("No previous scene to go back to.");
    }
  }

  /**
   * Called when the user clicks the "Roll Dice" button in BoardGameView.
   */
  @Override
  public void onRollDice() {
    int roll = game.getDice().rollDice();
    Player current = game.getCurrentplayer();
    current.move(roll);
    view.updateView();

    Player winner = game.getWinner();
    if (winner != null) {
      showEndGameDialog(winner.getName());
      return;
    }

    List<Player> list = game.getPlayers();
    int index = list.indexOf(current);
    game.setCurrentPlayer(list.get((index + 1) % list.size()));
    view.updateView();
  }

  /**
   * Shows a dialog when the game ends.
   * Gives two choices: "Play Again" or "Main Menu".
   *
   * @param winnerName the name of the winning player
   */
  private void showEndGameDialog(String winnerName) {
    Alert alert = new Alert(AlertType.NONE);
    alert.setTitle("Game Over");
    alert.setHeaderText(winnerName + " wins!");

    ButtonType playAgain = new ButtonType("Play Again");
    ButtonType mainMenu  = new ButtonType("Main Menu");
    alert.getButtonTypes().setAll(playAgain, mainMenu);

    DialogPane pane = alert.getDialogPane();
    pane.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm()
    );
    pane.getStyleClass().add("root");

    Button buttonPlay = (Button) pane.lookupButton(playAgain);
    buttonPlay.getStyleClass().add("button-main");
    Button buttonMenu = (Button) pane.lookupButton(mainMenu);
    buttonMenu.getStyleClass().add("button-main");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == playAgain) {
      restartGame();
    } else {
      backToMainMenu();
    }
  }

  /**
   * When the user chooses the "Play again" button. Restarts the game with the same players and pieces.
   */
  private void restartGame() {
    new BoardGameViewController(primaryStage, playersData, gameType, fullscreenHandler);
  }

  /**
   * When the user chooses the "Main menu" button. Returns the user to the main menu (start screen).
   */
  private void backToMainMenu() {
    try {
      MainView.getInstance().backToMainMenu();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error starting main menu", e);
      Platform.exit();
    }
  }
}
