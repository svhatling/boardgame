// view/PlayerInputRow.java
package view.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.collections.ObservableList;

import java.util.List;

public class PlayerInputRow extends HBox {
  private final ComboBox<String> combo;
  private final TextField nameField;
  private final ComboBox<String> pieceCombo;
  private final CheckBox saveNewCheck;

  /**
   * @param label        Tekst som vises foran raden (f.eks. "Spiller 1")
   * @param savedPlayers Liste over spillernavn fra CSV
   */
  public PlayerInputRow(String label, ObservableList<String> savedPlayers, List<String> pieceOptions) {
    super(10);
    setAlignment(Pos.CENTER);

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

    saveNewCheck = new CheckBox("Save new player");
    saveNewCheck.getStyleClass().add("checkbox-save");

    // Når man bytter mellom «Ny spiller» og en lagret spiller
    combo.setOnAction(e -> {
      String val = combo.getValue();
      if ("New player".equals(val)) {
        nameField.clear();
        nameField.setDisable(false);
      } else {
        nameField.setText(val);
        nameField.setDisable(true);
      }
    });

    getChildren().addAll(lbl, combo, nameField, pieceCombo, saveNewCheck);
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
}

