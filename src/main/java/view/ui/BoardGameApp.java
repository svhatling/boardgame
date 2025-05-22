package view.ui;

import controller.MainViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.logic.GameType;
import model.util.FullscreenHandler;

/**
 * Main class for the board game.
 * <p>
 * Shows a menu for choosing game variant and user action
 * tasks are given to the relevant view classes.
 * </p>
 */
public class BoardGameApp extends Application {
  private final MainViewController controller = new MainViewController();
  private FullscreenHandler fullscreenHandler;

  /**
   * Starting the JavaFX application.
   *
   * @param primaryStage the primary stage for starting the UI
   */
  @Override
  public void start(Stage primaryStage) {
    this.fullscreenHandler = new FullscreenHandler(primaryStage);
    Label title = new Label("Boardgame");
    title.getStyleClass().add("label-title");

    Button laddersButton = new Button("Ladders & Snakes");
    laddersButton.getStyleClass().add("button-main");
    laddersButton.setPrefWidth(200);
    laddersButton.setOnAction(e -> {
      controller.selectGame(GameType.SNAKES_AND_LADDERS);
      new AmountOfPlayersView(primaryStage, GameType.SNAKES_AND_LADDERS, fullscreenHandler);
    });

    Button quizButton = new Button("Quiz");
    quizButton.getStyleClass().add("button-main");
    quizButton.setPrefWidth(200);
    quizButton.setOnAction(e -> {
      controller.selectGame(GameType.QUIZ);
      new AmountOfPlayersView(primaryStage, GameType.QUIZ, fullscreenHandler);
    });

    VBox layout = new VBox(20, title, laddersButton, quizButton);
    layout.setAlignment(Pos.CENTER);
    layout.setMaxWidth(300);

    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    // Bind to the stage size so the UI elements scales accordingly
    root.prefWidthProperty().bind(primaryStage.widthProperty());
    root.prefHeightProperty().bind(primaryStage.heightProperty());

    Scene scene = new Scene(root, 800, 600);
    scene.getStylesheets().add(getClass()
        .getResource("/css/style.css")
        .toExternalForm());

    primaryStage.setTitle("Choose Game");
    primaryStage.setScene(scene);

    fullscreenHandler.setupFullscreenHandling(root);
    primaryStage.show();
  }

  /**
   * Application entry point.
   * Exception handling before launching JavaFX, is practical
   * for seeing whether the program can launch or has errors.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Handler for uncaught exceptions on any thread
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
      // Print to console
      throwable.printStackTrace();

      // Show an error dialog in UI
      Platform.runLater(() -> {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Unexpected Error");
        alert.setHeaderText("Unexpected error occurred");
        alert.setContentText(throwable.getMessage());
        alert.showAndWait();
      });
    });


    /**
     * Starts the JavaFX application.
     *
     * @param args command-line arguments
     */
      launch(args);
    }
  }
