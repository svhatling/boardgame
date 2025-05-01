package view.ui;

import controller.AmountOfPlayersViewController;
import controller.PlayerViewController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.BoardGameObserver;

/**
 * View for selecting amount of players.
 * <p>The view reuses the primary stage and binds the layout to fill the whole window.
 */
public class AmountOfPlayersView implements BoardGameObserver {

  /**
   * Constructs player selection view. The layout resizes with
   * the stage.
   *
   * @param stage    the primary stage to display the scene on
   * @param gameType the name of the game variant ("Ladders & Snakes" or "Ludo")
   */
  public AmountOfPlayersView(Stage stage, String gameType) {
    // creates controller and registers this view as observer
    AmountOfPlayersViewController controller = new AmountOfPlayersViewController(this);

    // header label
    Label title = new Label(gameType);
    title.getStyleClass().add("label-title");

    // instruction label
    Label instruction = new Label("Number of players:");
    instruction.getStyleClass().add("label-sub");

    // box for selecting player count
    ComboBox<Integer> comboBox = new ComboBox<>();
    comboBox.getItems().addAll(2, 3, 4, 5);
    comboBox.setValue(1);
    comboBox.getStyleClass().add("combo-box");
    comboBox.setMaxWidth(150);

    // start button
    Button startButton = new Button("Start");
    startButton.getStyleClass().add("button-main");
    startButton.setPrefWidth(100);
    startButton.setOnAction(e -> controller.startGame(comboBox.getValue()));
    startButton.setOnAction(e -> { int selected = comboBox.getValue();
      controller.setNumberOfPlayers(selected);

      // Change scene to PlayerView
      PlayerViewController pvc = new PlayerViewController(stage, controller, stage.getScene());
      pvc.showPlayerView();
    });

    // Arranges in a vertical box
    VBox layout = new VBox(20, title, instruction, comboBox, startButton);
    layout.setAlignment(Pos.CENTER);
    layout.setMaxWidth(320);

    // Root pane that fills the stage
    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    // Bind new scenes to stage size. So that new scene always fills the window
    root.prefWidthProperty().bind(stage.widthProperty());
    root.prefHeightProperty().bind(stage.heightProperty());

    // create scene with css style
    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass()
        .getResource("/css/style.css")
        .toExternalForm());

    // change scene and keep screen size
    boolean wasFullScreen = stage.isFullScreen();
    boolean wasMaximized = stage.isMaximized();
    stage.setScene(scene);
    if (wasFullScreen) {
      stage.setFullScreen(true);
    } else {
      stage.setMaximized(wasMaximized);
    }
    stage.setTitle("Choose Number of Players");
  }

  /**
   * Called when the player count is chosen.
   *
   * @param count the number of players selected
   */
  @Override
  public void onPlayerCountChosen(int count) {
    // TODO: Go to the PlayerView using count
    System.out.println("Starting game with " + count + " players");
  }
}
