package controller;

import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import view.ui.AmountOfPlayersView;
import view.ui.PlayerView;

public class PlayerViewController {
  private final Stage primaryStage;
  private Scene previousScene;
  private final AmountOfPlayersController amountController;
  private PlayerView playerView;
  private PlayerViewController controller;

  /**
   * @param primaryStage    Hovedvinduet der vi skal vise PlayerView
   * @param amountController Den kontrolleren som har antallet spillere
   */
  public PlayerViewController(Stage primaryStage, AmountOfPlayersController amountController, Scene previousScene) {
    this.primaryStage = primaryStage;
    this.amountController = amountController;
    this.previousScene = previousScene;
  }

  /**
   * Lager et nytt PlayerView basert på antallet spillere, og bytter scenen.
   */
  public void showPlayerView() {
    int numPlayers = amountController.getNumberOfPlayers();
    playerView = new PlayerView(numPlayers, this);

    Scene scene = new Scene(playerView, 700, 500);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Ladders & Snakes - Player View");
    primaryStage.show();
  }

  /**
   * Gir tilgang til PlayerView hvis du trenger å hente data etterpå.
   */
  public PlayerView getPlayerView() {
    return playerView;
  }

  public void goBack() {
    if (previousScene != null) {
      primaryStage.setScene(previousScene);
    } else {
      System.out.println("No previous scene to go back to.");
    }
  }
}
