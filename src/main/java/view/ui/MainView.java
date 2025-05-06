package view.ui;

import controller.MainViewController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main application class that starts the board game selection.
 */
public class MainView extends Application {
  private Stage primaryStage;
  private MainViewController controller;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.controller = new MainViewController();

    // Set up the game selection screen
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
      controller.selectGame("Ladders & Snakes");
      showAmountOfPlayersView("Ladders & Snakes");
    });

    Button memoryButton = new Button("Memory");
    memoryButton.getStyleClass().add("button-main");
    memoryButton.setPrefWidth(200);
    memoryButton.setOnAction(e -> {
      controller.selectGame("Memory");
      showAmountOfPlayersView("Memory");
    });

    // arrange in a vertical box
    VBox layout = new VBox(20, title, laddersButton, memoryButton);
    layout.setAlignment(Pos.CENTER);
    layout.setMaxWidth(300);

    // root pane that fills the stage
    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    // bind to stage size
    root.prefWidthProperty().bind(primaryStage.widthProperty());
    root.prefHeightProperty().bind(primaryStage.heightProperty());

    Scene scene = new Scene(root, 800, 600);
    scene.getStylesheets().add(getClass()
        .getResource("/css/style.css")
        .toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.setTitle("Choose Game");
  }

  /**
   * Shows the amount of players selection view.
   *
   * @param gameType the selected game type
   */
  private void showAmountOfPlayersView(String gameType) {
    new AmountOfPlayersView(primaryStage, gameType);
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