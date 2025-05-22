package view.ui;

import controller.AmountOfPlayersViewController;
import controller.PlayerViewController;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.logic.GameType;
import model.util.FullscreenHandler;
import view.BoardGameObserver;

/**
 * View for selecting the number of players.
 * <p>The view reuses the primary stage and binds the layout to fill the whole window.
 */
public class AmountOfPlayersView implements BoardGameObserver {

  /**
   * Constructs player selection view. The layout resizes with the stage.
   *
   * @param stage    the primary stage to display the scene on
   * @param gameType the name of the game variant ("Ladders & Snakes" or "Ludo")
   */
  public AmountOfPlayersView(Stage stage, GameType gameType, FullscreenHandler fullscreenHandler) {
    // creates the controller and registers this view as observer
    AmountOfPlayersViewController controller = new AmountOfPlayersViewController();

    // header label
    Label title = new Label(gameType.getDisplayName());
    title.getStyleClass().add("label-title");

    // instruction label
    Label instruction = new Label("Number of players:");
    instruction.getStyleClass().add("label-sub");

    // box for selecting player count
    ComboBox<Integer> comboBox = new ComboBox<>();
    comboBox.getItems().addAll(2, 3, 4, 5);
    comboBox.setValue(2);
    comboBox.getStyleClass().add("combo-box");
    comboBox.setMaxWidth(150);

    // start button
    Button startButton = new Button("Start");
    startButton.getStyleClass().add("button-main");
    startButton.setPrefWidth(100);
    startButton.setOnAction(e -> {
          int selected = comboBox.getValue();
          controller.setNumberOfPlayers(selected);
          PlayerViewController pvc = new PlayerViewController(stage, controller, stage.getScene(),
              gameType, fullscreenHandler);
          pvc.showPlayerView();
        });

    // Arranges everything in a vertical box
    VBox layout = new VBox(20, title, instruction, comboBox, startButton);
    layout.setAlignment(Pos.CENTER);
    layout.setMaxWidth(320);

    // Root pane that fills the stage
    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    // Binds new scenes to stage size so that the new scene always fills the window
    root.prefWidthProperty().bind(stage.widthProperty());
    root.prefHeightProperty().bind(stage.heightProperty());

    // create the scene with styling
    Scene scene = new Scene(root);
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
            .getResource("/css/style.css"))
        .toExternalForm());

    // changes the scene and keeps the screen size
    boolean wasFullScreen = stage.isFullScreen();
    boolean wasMaximized = stage.isMaximized();
    stage.setScene(scene);
    if (wasFullScreen) {
      stage.setFullScreen(true);
    } else {
      stage.setMaximized(wasMaximized);
    }
    stage.setTitle("Choose Number of Players");
    stage.show();

    fullscreenHandler.setupFullscreenHandling(root);
  }

  @Override
  public void onBoardReady() {
    // No action needed
  }
}
