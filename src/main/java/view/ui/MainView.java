package view.ui;

import controller.MainViewController;
import java.util.Objects;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.logic.GameType;
import model.util.FullscreenHandler;
import view.AmountOfPlayersView;

/**
 * Main application class that starts the board game selection.
 */
public class MainView extends Application {
  private Stage primaryStage;
  private MainViewController controller;
  private FullscreenHandler fullscreenHandler;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.controller = new MainViewController();
    this.fullscreenHandler = new FullscreenHandler(primaryStage);

    // Sets up the game selection screen
    showGameSelection();

    primaryStage.show();
  }

  /**
   * Shows the game selection screen.
   */
  private void showGameSelection() {
    // header label
    Label title = new Label("Boardgame");
    title.getStyleClass().add("label-title");

    // Game type buttons
    Button laddersButton = new Button("Ladders & Snakes");
    laddersButton.getStyleClass().add("button-main");
    laddersButton.setPrefWidth(200);
    laddersButton.setOnAction(e -> {
      controller.selectGame(GameType.SNAKES_AND_LADDERS);
      showAmountOfPlayersView(GameType.SNAKES_AND_LADDERS);
    });

    Button quizButton = new Button("Quiz");
    quizButton.getStyleClass().add("button-main");
    quizButton.setPrefWidth(200);
    quizButton.setOnAction(e -> {
      controller.selectGame(GameType.QUIZ);
      showAmountOfPlayersView(GameType.QUIZ);
    });

    // arranges everything in a vertical box
    VBox layout = new VBox(20, title, laddersButton, quizButton);
    layout.setAlignment(Pos.CENTER);
    layout.setMaxWidth(300);

    // root pane that fills the stage
    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    // binds to stage size
    root.prefWidthProperty().bind(primaryStage.widthProperty());
    root.prefHeightProperty().bind(primaryStage.heightProperty());

    Scene scene = new Scene(root, 800, 600);
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
            .getResource("/css/style.css"))
        .toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.setTitle("Choose Game");

    fullscreenHandler.setupFullscreenHandling(root);
  }

  /**
   * Shows the number of players selection views.
   *
   * @param gameType the selected game type
   */
  private void showAmountOfPlayersView(GameType gameType) {
    new AmountOfPlayersView(primaryStage, gameType, fullscreenHandler);
  }

  /**
   * Main method that launches the application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}