package view.ui;

import controller.MainViewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main class for the board game.
 * <p>
 * Shows a menu for choosing the game variant and
 * user actions tasks is delegated to the relevant view classes.
 * </p>
 */
public class BoardGameApp extends Application {

  private final MainViewController controller = new MainViewController();

  /**
   * Starting the JavaFX application.
   *
   * @param primaryStage the primary stage for starting the UI
   */
  @Override
  public void start(Stage primaryStage) {
    Label title = new Label("Boardgame");
    title.getStyleClass().add("label-title");

    Button laddersButton = new Button("Ladders & Snakes");
    laddersButton.getStyleClass().add("button-main");
    laddersButton.setOnAction(e ->
        new AmountOfPlayersView(primaryStage, "Ladders & Snakes")
    );

    Button ludoButton = new Button("Ludo");
    ludoButton.getStyleClass().add("button-main");
    ludoButton.setOnAction(e ->
        new AmountOfPlayersView(primaryStage, "Ludo")
    );

    VBox layout = new VBox(20, title, laddersButton, ludoButton);
    layout.setStyle("-fx-alignment: center;");

    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass()
        .getResource("/css/style.css")
        .toExternalForm());

    primaryStage.setTitle("Choose Game");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  /**
   * Starts the JavaFX application.
   *
   * @param args command-line arguments (not used yet)
   */
  public static void main(String[] args) {
    launch(args);
  }
}