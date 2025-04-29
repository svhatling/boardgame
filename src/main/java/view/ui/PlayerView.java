
package view.ui;

import controller.PlayerRecord;
import controller.ReadFromCSV;
import controller.SaveToCSV;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerView extends VBox {

  private final SaveToCSV saveToCSV;
  private final List<PlayerRecord> savedRecords;
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
    this.savedRecords = FXCollections.observableArrayList(r.readPlayers());

    this.savedPlayers = FXCollections.observableArrayList(
        savedRecords.stream()
            .map(pr -> pr.name)
            .collect(Collectors.toList())
    );

    Label title = new Label("Ladders & Snakes");
    title.getStyleClass().add("label-title");

    Label instruction = new Label("Enter player names and choose a piece:");
    instruction.getStyleClass().add("label-sub");

    Button backbutton = new Button("Back");
    backbutton.getStyleClass().add("button-main");
    backbutton.setOnAction(e -> {
      if (observer != null) {
        observer.onBack();
      }
    });

    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(title, instruction);

    List<String> pieceOptions = List.of("Car", "Hat", "Dog", "Ship", "Plane", "Crown");
    for (int i = 0; i < numPlayers; i++) {
      String label = "Player " + (i + 1);
      PlayerInputRow row = new PlayerInputRow(label, savedPlayers, pieceOptions, savedRecords);
      row.setOnPieceChanged(v -> updateAvailablePieces());
      rows.add(row);
      this.getChildren().add(row);
    }

    updateAvailablePieces();

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

  private void updateAvailablePieces() {
    Set<String> used = rows.stream()
        .map(PlayerInputRow::getSelectedPiece)
        .filter(p -> p != null)
        .collect(Collectors.toSet());

    List<String> all = List.of("Car", "Hat", "Dog", "Ship", "Plane", "Crown");
    for (PlayerInputRow row : rows) {
      String current = row.getSelectedPiece();
      List<String> opts = all.stream()
          .filter(p -> !used.contains(p) || p.equals(current))
          .collect(Collectors.toList());
      row.setPieceOptions(opts);
    }
  }

  /**
   * Henter data fra radene og lagrer nye spillernavn før spillet kjøres
   */
  private void handleStart() {
    List<PlayerData> players = new ArrayList<>();
    for (PlayerInputRow row : rows) {
      String name = row.getPlayerName();
      String piece = row.getSelectedPiece();
      boolean isNewPlayer = row.shouldSaveNewPlayer() && savedRecords.stream()
          .noneMatch(rec -> rec.name.equals(name));
      if (isNewPlayer) {
        PlayerRecord newRecord = new PlayerRecord(name, piece);
        savedRecords.add(newRecord);
        savedPlayers.add(name);
        saveToCSV.addPlayer(newRecord);
      }
      players.add(new PlayerData(name, piece));
    }
    if (observer != null) {
      observer.onStartGame(players);
    }
  }

  public static class PlayerData {

    public final String name;
    public final String piece;

    public PlayerData(String name, String piece) {
      this.name = name;
      this.piece = piece;
    }
  }

  public void setObserver(Observer observer) {
    this.observer = observer;
  }
}

