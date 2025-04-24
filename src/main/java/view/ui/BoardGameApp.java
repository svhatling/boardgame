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
    laddersButton.setPrefWidth(200);
    laddersButton.setOnAction(e -> {
      controller.selectGame("Ladders & Snakes");
      new AmountOfPlayersView(primaryStage, "Ladders & Snakes");
    });

    Button ludoButton = new Button("Ludo");
    ludoButton.getStyleClass().add("button-main");
    ludoButton.setPrefWidth(200);
    ludoButton.setOnAction(e -> {
      controller.selectGame("Ludo");
      new AmountOfPlayersView(primaryStage, "Ludo");
    });

    VBox layout = new VBox(20, title, laddersButton, ludoButton);
    layout.setAlignment(Pos.CENTER);
    layout.setMaxWidth(300);

    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    // Bind to the stage size so the content scales properly
    root.prefWidthProperty().bind(primaryStage.widthProperty());
    root.prefHeightProperty().bind(primaryStage.heightProperty());

    Scene scene = new Scene(root, 800, 600);
    scene.getStylesheets().add(getClass()
        .getResource("/css/style.css")
        .toExternalForm());

    primaryStage.setTitle("Choose Game");
    primaryStage.setScene(scene);
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