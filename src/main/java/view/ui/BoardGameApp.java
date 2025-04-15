package view.ui;

import controller.MainViewController;
import controller.AmountOfPlayersController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BoardGameApp extends Application {

  private final MainViewController controller = new MainViewController();

  @Override
  public void start(Stage primaryStage) {
    Label title = new Label("Boardgame");
    title.getStyleClass().add("label-title");

    Button laddersButton = new Button("Ladders & Snakes");
    Button ludoButton = new Button("Ludo");

    laddersButton.getStyleClass().add("button-main");
    ludoButton.getStyleClass().add("button-main");

    laddersButton.setOnAction(e -> {
      controller.selectGame("Ladders");
      new AmountOfPlayersView(primaryStage, new AmountOfPlayersController());
    });

    ludoButton.setOnAction(e -> {
      controller.selectGame("Ludo");
      new AmountOfPlayersView(primaryStage, new AmountOfPlayersController());
    });

    VBox layout = new VBox(20, title, laddersButton, ludoButton);
    layout.setStyle("-fx-alignment: center;");
    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    Scene scene = new Scene(root, 400, 300);
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

    primaryStage.setTitle("Choose Game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
