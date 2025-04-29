// view/PlayerInputRow.java
package view.ui;

import controller.PlayerRecord;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PlayerInputRow extends HBox {

  private final ComboBox<String> combo;
  private final TextField nameField;
  private final ComboBox<String> pieceCombo;
  private final CheckBox saveNewCheck;
  private final List<PlayerRecord> savedRecords;
  private Consumer<Void> onPieceChanged;

  /**
   * @param label        Tekst som vises foran raden (f.eks. "Spiller 1")
   * @param savedPlayers Liste over spillernavn fra CSV
   */
  public PlayerInputRow(String label, ObservableList<String> savedPlayers,
      List<String> pieceOptions, List<PlayerRecord> savedRecords) {
    super(10);
    setAlignment(Pos.CENTER);
    this.savedRecords = savedRecords;

    Label lbl = new Label(label);

    // ComboBox med «Ny spiller» + alle lagrede
    combo = new ComboBox<>(FXCollections.observableArrayList(savedPlayers));
    combo.getItems().add(0, "New player");
    combo.setValue("New player");

    // Navne-felt
    nameField = new TextField();
    nameField.getStyleClass().add("player-input-name");
    nameField.setPromptText("Name");

    pieceCombo = new ComboBox<>(FXCollections.observableArrayList(pieceOptions));
    pieceCombo.setPromptText("Choose piece");
    pieceCombo.setOnAction(e -> notifyPieceChanged());

    saveNewCheck = new CheckBox("Save new player");
    saveNewCheck.getStyleClass().add("checkbox-save");

    // Når man bytter mellom «Ny spiller» og en lagret spiller
    combo.setOnAction(e -> handlePlayerSelection());

    getChildren().addAll(lbl, combo, nameField, pieceCombo, saveNewCheck);
  }


  private void handlePlayerSelection() {
    String val = combo.getValue();
    if (val.equals("New player")) {
      nameField.clear();
      nameField.setDisable(false);
      saveNewCheck.setDisable(false);
      pieceCombo.getSelectionModel().clearSelection();
    } else {
      nameField.setText(val);
      nameField.setDisable(true);
      saveNewCheck.setDisable(true);
      savedRecords.stream()
          .filter(r -> r.name.equals(val))
          .findFirst()
          .ifPresent(record -> pieceCombo.setValue(record.piece));
    }
    notifyPieceChanged();
  }

  private void notifyPieceChanged() {
    if (onPieceChanged != null) {
      onPieceChanged.accept(null);
    }
  }

  public void setOnPieceChanged(Consumer<Void> callback) {
    this.onPieceChanged = callback;
  }

  public String getPlayerName() {
    return nameField.getText().trim();
  }

  public String getSelectedPiece() {
    return pieceCombo.getValue();
  }

  public boolean shouldSaveNewPlayer() {
    return saveNewCheck.isSelected();
  }

  public void setPieceOptions(List<String> options) {
    String current = pieceCombo.getValue();
    pieceCombo.getItems().setAll(options);
    if (options.contains(current)) {
      pieceCombo.setValue(current);
    } else {
      pieceCombo.getSelectionModel().clearSelection();
    }
  }
}