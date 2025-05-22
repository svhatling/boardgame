
package view.ui;

import controller.PlayerRecord;
import controller.ReadFromCSV;
import controller.SaveToCSV;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.logic.GameType;
import model.util.FullscreenHandler;

public class PlayerView extends VBox {

  private final SaveToCSV saveToCSV;
  private final List<PlayerRecord> savedRecords;
  private final ObservableList<String> savedPlayers;
  private final List<PlayerInputRow> rows = new ArrayList<>();
  private final FullscreenHandler fullscreenHandler;
  private Observer observer;

  public interface Observer {

    void onBack();

    void onStartGame(List<PlayerData> players);
  }

  /**
   * @param numPlayers Number of players that was chosen on the previous "page"
   */
  public PlayerView(int numPlayers, GameType gameType, FullscreenHandler fullscreenHandler) {
    super(20);
    this.fullscreenHandler = fullscreenHandler;
    this.saveToCSV = new SaveToCSV("players.csv");
    ReadFromCSV r = new ReadFromCSV("players.csv");
    this.savedRecords = FXCollections.observableArrayList(r.readPlayers());

    this.savedPlayers = FXCollections.observableArrayList(
        savedRecords.stream()
            .map(pr -> pr.name)
            .collect(Collectors.toList())
    );

    Label title = new Label(gameType.getDisplayName());
    title.getStyleClass().add("label-title-player");

    Label instruction = new Label("Enter player names and choose a piece:");
    instruction.getStyleClass().add("label-sub-player");

    Button backbutton = new Button("Back");
    backbutton.getStyleClass().addAll("button-main-player", "back");
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
    startButton.getStyleClass().add("button-main-player");
    startButton.setOnAction(e -> handleStart());

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backbutton, startButton);
    this.getChildren().add(buttonBox);

    this.getStyleClass().add("root");
    this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

    fullscreenHandler.setupFullscreenHandling(this);
  }

  private void updateAvailablePieces() {
    // 1) Tell opp hvilke brikker som allerede er valgt
    Map<String, Long> used = rows.stream()
        .map(PlayerInputRow::getSelectedPiece)
        .filter(Objects::nonNull)
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

    // 2) Alle mulige brikker
    List<String> all = List.of("Car", "Hat", "Dog", "Ship", "Plane", "Crown");

    // 3) For hver rad, regn ut hvilke brikker som fortsatt er tilgjengelige
    for (PlayerInputRow row : rows) {
      String current = row.getSelectedPiece();
      List<String> opts = all.stream()
          .filter(p -> !used.containsKey(p) || p.equals(current))
          .collect(Collectors.toList());
      row.setPieceOptions(opts);
    }

    // 4) Dersom samme brikke er valgt i flere rader, tving de nederste over på en ledig brikke
    Map<String, List<PlayerInputRow>> byPiece = rows.stream()
        .filter(r -> r.getSelectedPiece() != null)
        .collect(Collectors.groupingBy(PlayerInputRow::getSelectedPiece));

    for (var entry : byPiece.entrySet()) {
      List<PlayerInputRow> dupped = entry.getValue();
      if (dupped.size() > 1) {
        String duplicatedPiece = entry.getKey();
        for (int i = 1; i < dupped.size(); i++) {
          PlayerInputRow row = dupped.get(i);

          // Regn ut hvilke som er ledige *etter* å ha tatt høyde for duplikatet
          // (samme logikk som over, men ekskluder duplicatedPiece)
          List<String> opts = all.stream()
              .filter(p -> !p.equals(duplicatedPiece))
              .filter(p -> {
                // også sjekk at vi ikke forsøker å gi en brikke som allerede er brukt fullt opp
                long count = used.getOrDefault(p, 0L);
                return count == 0 || p.equals(row.getSelectedPiece());
              })
              .collect(Collectors.toList());

          // Om vi har noe ledig, velg første
          if (!opts.isEmpty()) {
            row.setSelectedPiece(opts.get(0));
            // Oppdater tilgjengeligheten for knapper igjen
            row.setPieceOptions(opts);
          } else {
            row.setSelectedPiece(null);
          }
        }
      }
    }
  }


  /**
   * Gets data from the rows and saves new playernames before starting the game
   */
  private void handleStart() {
    for (PlayerInputRow row : rows) {
      String name  = row.getPlayerName();
      String piece = row.getSelectedPiece();
      if (name == null || name.isBlank() || piece == null) {
        // Show warning and stop
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid input");
        alert.setHeaderText(null);
        alert.setContentText("Please fill in name and choose piece for all players.");
        alert.showAndWait();
        return;
      }
    }

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

