package view;

import controller.AmountOfPlayersViewController;
import controller.PlayerViewController;
import java.util.Objects;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.logic.GameType;
import model.util.FullscreenHandler;
import view.ui.MainView;

/**
 * View for selecting the number of players.
 * <p>The view reuses the primary stage and binds the layout to fill the whole window.
 */
public class AmountOfPlayersView implements BoardGameObserver {

  /**
   * Constructs player selection view. The layout resizes with the stage.
   *
   * @param stage    the primary stage to display the scene on
   * @param gameType the name of the game variant ("Ladders & Snakes" or "Quiz")
   * @param fullscreenHandler the handler for fullscreen mode
   */
  public AmountOfPlayersView(Stage stage, GameType gameType, FullscreenHandler fullscreenHandler) {
    // creates the controller and registers this view as observer
    AmountOfPlayersViewController controller = new AmountOfPlayersViewController();

    Label title = new Label(gameType.getDisplayName());
    title.getStyleClass().add("main-title");

    DropShadow titleGlow = new DropShadow();
    titleGlow.setColor(Color.web("#00000020"));
    titleGlow.setRadius(3);
    titleGlow.setSpread(0.1);
    title.setEffect(titleGlow);

    Label instruction = new Label("Number of players:");
    instruction.getStyleClass().add("label-sub");

    ComboBox<Integer> comboBox = new ComboBox<>();
    comboBox.getItems().addAll(2, 3, 4, 5);
    comboBox.setValue(2);
    comboBox.getStyleClass().add("combo-box-enhanced");
    comboBox.setMaxWidth(150);

    HBox buttonBox = new HBox(20);

    Button startButton = new Button("Start");
    startButton.getStyleClass().add("game-button");
    startButton.setPrefWidth(150);
    startButton.setMinWidth(150);
    startButton.setPrefHeight(50);
    startButton.setOnAction(e -> {
          int selected = comboBox.getValue();
          controller.setNumberOfPlayers(selected);
          PlayerViewController pvc = new PlayerViewController(stage, controller, stage.getScene(),
              gameType, fullscreenHandler);
          pvc.showPlayerView();
        });

    Button backButton = new Button("Back");
    backButton.getStyleClass().addAll("game-button", "secondary");
    backButton.setOnAction(e ->
      MainView.getInstance().backToMainMenu());

    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backButton, startButton);

    VBox layout = new VBox(20, title, instruction, comboBox, buttonBox);
    layout.setAlignment(Pos.CENTER);
    layout.getStyleClass().add("content-box-amount");
    layout.setMaxWidth(600);
    layout.setMaxHeight(350);
    layout.setPrefWidth(600);

    StackPane root = new StackPane(layout);
    root.getStyleClass().add("main-root");

    // Binds new scenes to stage size so that the new scene always fills the window
    root.prefWidthProperty().bind(stage.widthProperty());
    root.prefHeightProperty().bind(stage.heightProperty());

    Scene scene = new Scene(root);
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
            .getResource("/css/style.css"))
        .toExternalForm());

    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), layout);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);
    fadeIn.play();

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

  /**
   * Updates the view when the board is ready.
   * <p>This method is called when the board is ready.
   * It does not perform any action in this view.
   */
  @Override
  public void onBoardReady() {
    // No action needed
  }
}
