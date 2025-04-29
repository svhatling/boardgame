package controller;

import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Scene;
import model.entity.BoardGame;
import model.factory.BoardGameFactory;
import model.entity.Player;
import view.ui.BoardGameView;
import view.ui.BoardGameView.Observer;
import view.ui.PlayerView;
import view.ui.PlayerView.PlayerData;

/**
 * Controller for the player setup and main game flow.
 * Listens to PlayerView and BoardGameView events.
 */
public class PlayerViewController implements PlayerView.Observer, BoardGameView.Observer {
  private final Stage primaryStage;
  private Scene previousScene;
  private final AmountOfPlayersController amountController;
  private PlayerView playerView;

  // Game model and view for the board
  private BoardGame game;
  private BoardGameView boardGameView;

  /**
   * Constructs the controller.
   *
   * @param primaryStage     the main application window
   * @param amountController the controller for the player count selection
   * @param previousScene    the scene to return to on "Back"
   */
  public PlayerViewController(Stage primaryStage,
      AmountOfPlayersController amountController,
      Scene previousScene) {
    this.primaryStage     = primaryStage;
    this.amountController = amountController;
    this.previousScene    = previousScene;
  }

  /**
   * Called when the user clicks the "Back" button in PlayerView.
   */
  @Override
  public void onBack() {
    goBack();
  }

  /**
   * Called when the user has entered player data and clicked "Start Game".
   *
   * @param players the list of player names and pieces
   */
  @Override
  public void onStartGame(List<PlayerData> players) {
    startGameWithPlayers(players);
  }

  /**
   * Called when the user clicks "Roll Dice" in BoardGameView.
   * Rolls the dice, moves the current player, checks for win,
   * advances the turn if no winner, and updates the view.
   */
  @Override
  public void onRollDice() {
    // 1) Roll
    int roll = game.getDice().rollDice();

    // 2) Move current player
    Player current = game.getCurrentplayer();
    current.move(roll);

    // 3) Refresh board so move is visible
    boardGameView.updateView();

    // 4) Check for winner
    Player winner = game.getWinner();
    if (winner != null) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Game Over");
      alert.setHeaderText(null);
      alert.setContentText(winner.getName() + " wins!");
      alert.showAndWait();
      return;  // stop further turns
    }

    // 5) Advance to next player
    List<Player> list = game.getPlayers();
    int idx = list.indexOf(current);
    int next = (idx + 1) % list.size();
    game.setCurrentPlayer(list.get(next));

    // 6) Update view to show new current and last roll
    boardGameView.updateView();
  }

  /**
   * Called when the user clicks "Save Game" in BoardGameView.
   * (Save logic to be implemented.)
   */
  @Override
  public void onSaveGame() {
    // TODO: implement save logic
  }

  /**
   * Called when the user clicks "Load Game" in BoardGameView.
   * (Load logic to be implemented.)
   */
  @Override
  public void onLoadGame() {
    // TODO: implement load logic
  }

  /**
   * Displays the PlayerView for entering player names.
   */
  public void showPlayerView() {
    int numPlayers = amountController.getNumberOfPlayers();
    playerView = new PlayerView(numPlayers);
    playerView.setObserver(this);

    primaryStage.setScene(new Scene(playerView, 700, 500));
    primaryStage.setTitle("Ladders & Snakes - Player View");
    primaryStage.show();
  }

  /**
   * Returns to the previous scene if available.
   */
  public void goBack() {
    if (previousScene != null) {
      primaryStage.setScene(previousScene);
    } else {
      System.out.println("No previous scene to go back to.");
    }
  }

  /**
   * Creates and starts the BoardGame and BoardGameView.
   *
   * @param playerList the list of players to add to the game
   */
  public void startGameWithPlayers(List<PlayerData> playerList) {
    // create game with 2 dice
    game = BoardGameFactory.create("snakesandladders", 2);
    for (PlayerData pd : playerList) {
      game.addPlayer(new Player(pd.name, game.getBoard(), pd.piece));
    }

    // set first player
    game.setCurrentPlayer(game.getPlayers().get(0));

    // create and show board view
    boardGameView = new BoardGameView(game);
    boardGameView.setObserver(this);

    Scene scene = new Scene(boardGameView, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Ladders & Snakes");
    primaryStage.show();
  }
}
