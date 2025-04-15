package view.ui;

import controller.MainViewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BoardGameApp extends Application {

  private final MainViewController controller = new MainViewController();

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Board Game - Choose Game");

    Button laddersButton = new Button("Ladders & Snakes");
    Button ludoButton = new Button("Ludo");

    laddersButton.setOnAction(e -> controller.selectGame("Ladders"));
    ludoButton.setOnAction(e -> controller.selectGame("Ludo"));

    VBox layout = new VBox(15, laddersButton, ludoButton);
    layout.setStyle("-fx-padding: 40; -fx-alignment: center;");

    Scene scene = new Scene(layout, 300, 200);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
