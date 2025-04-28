
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

  /**
   * @param numPlayers Antall spillere brukeren har valgt på forrige side
   */
  public PlayerView(int numPlayers, PlayerViewController controller) {
    super(20);
    this.saveToCSV = new SaveToCSV("players.csv");
    ReadFromCSV r = new ReadFromCSV("players.csv");
    this.savedPlayers = FXCollections.observableArrayList(r.readPlayers());

    Label title = new Label("Ladders & Snakes");
    title.getStyleClass().add("label-title");

    Label instruction = new Label("Enter player names and colors:");
    instruction.getStyleClass().add("label-sub");

    Button backbutton = new Button("Back");
    backbutton.getStyleClass().add("button-main");
    backbutton.setOnAction(e -> controller.goBack());

    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(title, instruction, backbutton);

    List<String> pieceOptions = List.of("Car", "Hat", "Dog", "Ship", "Plane", "Crown");
    for (int i = 0; i < numPlayers; i++) {
      String label = "Player " + (i + 1);
      PlayerInputRow row = new PlayerInputRow(label, savedPlayers, pieceOptions);
      rows.add(row);
      this.getChildren().add(row);
    }

    this.getStyleClass().add("root");
    this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
  }

  /** Henter data fra radene og lagrer nye spillernavn før spillet kjøres */
  private void handleStart() {
    for (PlayerInputRow row : rows) {
      String name = row.getPlayerName();
      if (!savedPlayers.contains(name)) {
        // Ny spiller: legg til CSV og i lokalt minne
        savedPlayers.add(name);
        saveToCSV.addPlayer(name);
      }
      Color color = row.getColor();
      // TODO: send videre til spillmotoren: name + color
    }
    // F.eks.: mainApp.startGame(playerDataList);
  }
}

