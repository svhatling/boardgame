package view.ui;

import controller.MainViewController;
import java.util.Objects;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
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
  private Pane animationLayer;
  private static MainView instance;

  /**
   * Initializes the main view.
   */
  @Override
  public void init() {
    instance = this;
  }

  /**
   * Returns the instance of MainView.
   *
   * @return the instance of MainView
   */
  public static MainView getInstance() {
    return instance;
  }

  /**
   * Starts the JavaFX application.
   *
   * @param primaryStage the primary stage for this application
   */
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
   * Shows the game selection screen with animations.
   */
  private void showGameSelection() {
    animationLayer = new Pane();
    animationLayer.getStyleClass().add("animation-layer");
    createFloatingShapes();

    // Header label
    Label title = new Label("Board Game");
    title.getStyleClass().add("main-title");

    // Adds glow effect to title
    DropShadow titleGlow = new DropShadow();
    titleGlow.setColor(Color.web("#ffffff30"));
    titleGlow.setRadius(20);
    titleGlow.setSpread(0.2);
    title.setEffect(titleGlow);

    Button laddersButton = new Button("Ladders & Snakes");
    laddersButton.getStyleClass().add("game-button");
    laddersButton.setPrefWidth(250);
    laddersButton.setPrefHeight(60);

    Button quizButton = new Button("Quiz");
    quizButton.getStyleClass().add("game-button");
    quizButton.setPrefWidth(250);
    quizButton.setPrefHeight(60);

    // Adds hover animations to buttons
    addButtonHoverAnimation(laddersButton);
    addButtonHoverAnimation(quizButton);

    // Button actions with transition animation
    laddersButton.setOnAction(e -> {
      animateButtonClick(laddersButton);
      controller.selectGame(GameType.SNAKES_AND_LADDERS);

      // Delay scene transition for smooth animation
      PauseTransition pause = new PauseTransition(Duration.millis(300));
      pause.setOnFinished(event -> showAmountOfPlayersView(GameType.SNAKES_AND_LADDERS));
      pause.play();
    });

    quizButton.setOnAction(e -> {
      animateButtonClick(quizButton);
      controller.selectGame(GameType.QUIZ);

      PauseTransition pause = new PauseTransition(Duration.millis(300));
      pause.setOnFinished(event -> showAmountOfPlayersView(GameType.QUIZ));
      pause.play();
    });

    VBox content = new VBox(30, title, laddersButton, quizButton);
    content.setAlignment(Pos.CENTER);
    content.setMaxWidth(400);
    content.getStyleClass().add("content-box");

    content.setMaxWidth(400);
    content.setMaxHeight(400);
    content.setPrefWidth(400);

    StackPane root = new StackPane();
    root.getStyleClass().add("main-root");
    root.getChildren().addAll(animationLayer, content);

    root.prefWidthProperty().bind(primaryStage.widthProperty());
    root.prefHeightProperty().bind(primaryStage.heightProperty());
    animationLayer.prefWidthProperty().bind(root.widthProperty());
    animationLayer.prefHeightProperty().bind(root.heightProperty());

    Scene scene = new Scene(root, 800, 600);
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
            .getResource("/css/style.css"))
        .toExternalForm());

    // Entrance animations
    animateEntrance(content);

    primaryStage.setScene(scene);
    primaryStage.setTitle("Choose Game");
    primaryStage.setMinWidth(600);
    primaryStage.setMinHeight(500);

    fullscreenHandler.setupFullscreenHandling(root);
  }

  /**
   * Creates floating background shapes for visual interest.
   */
  private void createFloatingShapes() {
    for (int i = 0; i < 6; i++) {
      Circle shape = new Circle(Math.random() * 40 + 20);
      shape.getStyleClass().add("floating-shape");
      shape.setLayoutX(Math.random() * 800);
      shape.setLayoutY(800 + 100); // Start below screen

      animationLayer.getChildren().add(shape);
      animateFloatingShape(shape, i);
    }
  }

  /**
   * Animates a floating shape.
   *
   * @param shape the shape to animate
   * @param index the index of the shape for staggered animation
   */
  private void animateFloatingShape(Circle shape, int index) {
    TranslateTransition translate = new TranslateTransition(
        Duration.seconds(20 + Math.random() * 10), shape);
    translate.setFromY(0);
    translate.setToY(-1000);
    translate.setInterpolator(Interpolator.LINEAR);

    RotateTransition rotate = new RotateTransition(
        Duration.seconds(10 + Math.random() * 5), shape);
    rotate.setByAngle(360);
    rotate.setCycleCount(Animation.INDEFINITE);

    ParallelTransition parallel = new ParallelTransition(translate, rotate);
    parallel.setDelay(Duration.seconds(index * 2));
    parallel.setCycleCount(Animation.INDEFINITE);
    parallel.play();
  }

  /**
   * Adds hover animation to buttons.
   *
   * @param button the button to animate
   */
  private void addButtonHoverAnimation(Button button) {
    ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
    scaleUp.setToX(1.05);
    scaleUp.setToY(1.05);

    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
    scaleDown.setToX(1.0);
    scaleDown.setToY(1.0);

    DropShadow shadow = new DropShadow();
    shadow.setColor(Color.web("#667eea80"));
    shadow.setRadius(20);
    shadow.setSpread(0.2);

    button.setOnMouseEntered(e -> {
      scaleUp.play();
      button.setEffect(shadow);
    });

    button.setOnMouseExited(e -> {
      scaleDown.play();
      button.setEffect(null);
    });
  }

  /**
   * Animates button click.
   *
   * @param button the button to animate
   */
  private void animateButtonClick(Button button) {
    ScaleTransition press = new ScaleTransition(Duration.millis(100), button);
    press.setToX(0.95);
    press.setToY(0.95);

    ScaleTransition release = new ScaleTransition(Duration.millis(100), button);
    release.setToX(1.0);
    release.setToY(1.0);

    SequentialTransition clickAnimation = new SequentialTransition(press, release);
    clickAnimation.play();
  }

  /**
   * Animates the entrance of the main content.
   *
   * @param content the content to animate
   */
  private void animateEntrance(Node content) {
    // Fade in
    FadeTransition fadeIn = new FadeTransition(Duration.millis(800), content);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    // Slide up
    TranslateTransition slideUp = new TranslateTransition(Duration.millis(800), content);
    slideUp.setFromY(50);
    slideUp.setToY(0);
    slideUp.setInterpolator(Interpolator.EASE_OUT);

    // Play together
    ParallelTransition entrance = new ParallelTransition(fadeIn, slideUp);
    entrance.play();
  }

  /**
   * Shows the number of players selection views with fade out transition.
   *
   * @param gameType the selected game type
   */
  private void showAmountOfPlayersView(GameType gameType) {
    FadeTransition fadeOut = new FadeTransition(
        Duration.millis(300),
        primaryStage.getScene().getRoot()
    );
    fadeOut.setFromValue(1);
    fadeOut.setToValue(0);
    fadeOut.setOnFinished(e ->
      new AmountOfPlayersView(primaryStage, gameType, fullscreenHandler));
    fadeOut.play();
  }

  public void backToMainMenu() {
    showGameSelection();
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