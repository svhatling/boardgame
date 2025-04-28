package view.ui;

import controller.AmountOfPlayersController;
import controller.PlayerViewController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AmountOfPlayersView {

  public AmountOfPlayersView(Stage stage, AmountOfPlayersController controller) {

    Label title = new Label("Ladders & Snakes");
    title.getStyleClass().add("label-title");

    Label instruction = new Label("Number of players:");
    instruction.getStyleClass().add("label-sub");

    ComboBox<Integer> comboBox = new ComboBox<>();
    comboBox.getItems().addAll(2, 3, 4, 5, 6);
    comboBox.setValue(2);
    comboBox.getStyleClass().add("combo-box");

    Button startButton = new Button("Start");
    startButton.getStyleClass().add("button-main");

    startButton.setOnAction(e -> {
      int selected = comboBox.getValue();
      controller.setNumberOfPlayers(selected);

      // Bytt scene til PlayerView

      PlayerViewController pvc = new PlayerViewController(stage, controller, stage.getScene());
      pvc.showPlayerView();
    });

    VBox layout = new VBox(20, title, instruction, comboBox, startButton);
    layout.setAlignment(Pos.CENTER);

    StackPane root = new StackPane(layout);
    root.getStyleClass().add("root");

    Scene scene = new Scene(root, 400, 300);
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

    stage.setScene(scene);
    stage.setTitle("Choose Number of Players");
    stage.show();
  }
}
