package controller;

import java.util.List;
import java.util.Optional;
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
import view.ui.BoardGameApp;
import view.ui.BoardGameView;
import view.ui.BoardGameView.Observer;
import view.ui.PlayerView.PlayerData;

/**
 * Controller for the boardgame screen.
 * Handles board view actions and updates the game.
 */
public class BoardGameViewController implements Observer {
  private final Stage primaryStage;
  private final List<PlayerData> playersData;
  private final GameType gameType;
  private BoardGame game;
  private BoardGameView view;

  /**
   * Builds controller, model, view, and displays the boardgame.
   *
   * @param primaryStage the main application window
   * @param playersData  the list of players with pieces
   */
  public BoardGameViewController(Stage primaryStage, List<PlayerData> playersData, GameType gameType) {
    this.primaryStage = primaryStage;
    this.playersData  = playersData;
    this.gameType     = gameType;

    switch (gameType) {
      case SNAKES_AND_LADDERS:
        game = BoardGameFactory.create("snakesandladders", playersData.size());
        break;
      case QUIZ:
        game = BoardGameFactory.create("quiz", playersData.size());
        break;
      default:
        throw new IllegalArgumentException("Invalid game type: " + gameType);
    }

    for (PlayerData pd : playersData) {
      game.addPlayer(new Player(pd.name, game.getBoard(), pd.piece));
    }
    game.setCurrentPlayer(game.getPlayers().get(0));

    // Initializes and shows the view
    view = new BoardGameView(game, gameType);
    view.setObserver(this);

    primaryStage.setScene(new Scene(view, 800, 600));
    primaryStage.setTitle(
        gameType == GameType.SNAKES_AND_LADDERS
        ? "Ladders & Snakes"
        : "Quiz");
    primaryStage.show();
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

    // Applies CSS styling
    DialogPane pane = alert.getDialogPane();
    pane.getStylesheets().add(
        getClass().getResource("/css/style.css").toExternalForm()
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
    new BoardGameViewController(primaryStage, playersData, gameType);
  }

  /**
   * When the user chooses the "Main menu" button. Returns the user to the main menu (start screen).
   */
  private void backToMainMenu() {
    try {
      new BoardGameApp().start(primaryStage);
    } catch (Exception e) {
      e.printStackTrace();
      Platform.exit();
    }
  }
}
