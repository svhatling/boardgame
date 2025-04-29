
package view.ui;

import controller.PlayerViewController;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import controller.ReadFromCSV;
import controller.SaveToCSV;

import java.util.ArrayList;
import java.util.List;

public class PlayerView extends VBox {
  private final SaveToCSV saveToCSV;
  private final ObservableList<String> savedPlayers;
  private final List<PlayerInputRow> rows = new ArrayList<>();
  private Observer observer;

  public interface Observer {
    void onBack();
    void onStartGame(List<PlayerData> players);
  }

  /**
   * @param numPlayers Antall spillere brukeren har valgt på forrige side
   */
  public PlayerView(int numPlayers) {
    super(20);
    this.saveToCSV = new SaveToCSV("players.csv");
    ReadFromCSV r = new ReadFromCSV("players.csv");
    this.savedPlayers = FXCollections.observableArrayList(r.readPlayers());

    Label title = new Label("Ladders & Snakes");
    title.getStyleClass().add("label-title");

    Label instruction = new Label("Enter player names and choose a piece:");
    instruction.getStyleClass().add("label-sub");

    Button backbutton = new Button("Back");
    backbutton.getStyleClass().add("button-main");
    backbutton.setOnAction(e -> {
      if (observer != null) observer.onBack();
    });

    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(title, instruction);

    List<String> pieceOptions = List.of("Car", "Hat", "Dog", "Ship", "Plane", "Crown");
    for (int i = 0; i < numPlayers; i++) {
      String label = "Player " + (i + 1);
      PlayerInputRow row = new PlayerInputRow(label, savedPlayers, pieceOptions);
      rows.add(row);
      this.getChildren().add(row);
    }

    Button startButton = new Button("Start Game");
    startButton.getStyleClass().add("button-main");
    startButton.setOnAction(e -> handleStart());

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backbutton, startButton);
    this.getChildren().add(buttonBox);

    this.getStyleClass().add("root");
    this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
  }

  /** Henter data fra radene og lagrer nye spillernavn før spillet kjøres */
  private void handleStart() {
    List<PlayerData> players = new ArrayList<>();
    for (PlayerInputRow row : rows) {
      String name = row.getPlayerName();
      String piece = row.getSelectedPiece();
      if (!savedPlayers.contains(name) && row.shouldSaveNewPlayer()) {
        savedPlayers.add(name);
        saveToCSV.addPlayer(name);
      }
      players.add(new PlayerData(name, piece));
    }
    if (observer != null) observer.onStartGame(players);
  }

  public static class PlayerData {
    public final String name;
    public final String piece;

    public PlayerData(String name, String piece) {
      this.name = name;
      this.piece = piece;
    }
  }

  public void setObserver(Observer observer){
    this.observer = observer;
  }
}

