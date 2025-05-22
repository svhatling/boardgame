package controller;

import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.logic.GameType;
import model.util.FullscreenHandler;
import view.PlayerView;
import view.PlayerView.PlayerData;

/**
 * Controller for the player view screen.
 * Handles events from PlayerView and starts the game.
 */
public class PlayerViewController implements PlayerView.Observer {
  private final Stage primaryStage;
  private final Scene previousScene;
  private final AmountOfPlayersViewController amountController;
  private final GameType gameType;
  private final FullscreenHandler fullscreenHandler;

  /**
   * Constructor of the controller.
   *
   * @param primaryStage     the main application window
   * @param amountController controller for selection of number of players
   * @param previousScene    the scene to return to if the user chooses the "Back" button
   * @param gameType        the type of game to be played
   * @param fullscreenHandler the handler for fullscreen mode
   */
  public PlayerViewController(Stage primaryStage,
      AmountOfPlayersViewController amountController,
      Scene previousScene, GameType gameType, FullscreenHandler fullscreenHandler) {
    this.primaryStage     = primaryStage;
    this.amountController = amountController;
    this.previousScene    = previousScene;
    this.gameType        = gameType;
    this.fullscreenHandler = fullscreenHandler;
  }

  /**
   * Called when the user clicks on the "Back" button.
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
   * Called when the user clicks "Start Game" after input of player data.
   * Launches the BoardGameController.
   *
   * @param players the list of player names and pieces
   */
  @Override
  public void onStartGame(List<PlayerData> players) {
    if (gameType == GameType.SNAKES_AND_LADDERS) {
      new BoardGameViewController(primaryStage, players, gameType, fullscreenHandler);
    } else {
      new QuizGameViewController(primaryStage, players, fullscreenHandler);
    }
  }

  /**
   * Shows the PlayerView for input of player names and choosing pieces.
   */
  public void showPlayerView() {
    int numPlayers = amountController.getNumberOfPlayers();
    PlayerView playerview = new PlayerView(numPlayers, gameType, fullscreenHandler);
    playerview.setObserver(this);

    boolean wasFullScreen = primaryStage.isFullScreen();
    boolean wasMaximized = primaryStage.isMaximized();

    primaryStage.setScene(new Scene(playerview, 700, 500));
    primaryStage.setTitle(
        gameType == GameType.SNAKES_AND_LADDERS
            ? "Ladders & Snakes - Player setup"
            : "Quiz - Player setup"
    );

    if (wasFullScreen) {
      primaryStage.setFullScreen(true);
    } else {
      primaryStage.setMaximized(wasMaximized);
    }

    primaryStage.show();
  }
}
