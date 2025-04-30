package controller;

import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.ui.PlayerView;
import view.ui.PlayerView.PlayerData;

/**
 * Controller for the playerview screen.
 * Handles events from PlayerView and starts the game.
 */
public class PlayerViewController implements PlayerView.Observer {
  private final Stage primaryStage;
  private final Scene previousScene;
  private final AmountOfPlayersViewController amountController;
  private PlayerView playerView;

  /**
   * Constructor of the controller.
   *
   * @param primaryStage     the main application window
   * @param amountController controller for selection of amount of players
   * @param previousScene    the scene to return to if the user chooses the "Back" button
   */
  public PlayerViewController(Stage primaryStage,
      AmountOfPlayersViewController amountController,
      Scene previousScene) {
    this.primaryStage     = primaryStage;
    this.amountController = amountController;
    this.previousScene    = previousScene;
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
    new BoardGameViewController(primaryStage, players);
  }

  /**
   * Shows the PlayerView for input of player names and choosing pieces.
   */
  public void showPlayerView() {
    int numPlayers = amountController.getNumberOfPlayers();
    PlayerView playerview = new PlayerView(numPlayers);
    playerview.setObserver(this);

    primaryStage.setScene(new Scene(playerview, 700, 500));
    primaryStage.setTitle("Ladders & Snakes - Player Setup");
    primaryStage.show();
  }
}
