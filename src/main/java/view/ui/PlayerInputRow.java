// view/PlayerInputRow.java
package view.ui;

import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.collections.ObservableList;

public class PlayerInputRow extends HBox {
  private final ComboBox<String> combo;
  private final TextField nameField;
  private final ColorPicker colorPicker;

  /**
   * @param label        Tekst som vises foran raden (f.eks. "Spiller 1")
   * @param savedPlayers Liste over spillernavn fra CSV
   */
  public PlayerInputRow(String label, ObservableList<String> savedPlayers) {
    setSpacing(10);

    Label lbl = new Label(label);

    // ComboBox med «Ny spiller» + alle lagrede
    combo = new ComboBox<>(FXCollections.observableArrayList(savedPlayers));
    combo.getItems().add(0, "Ny spiller");
    combo.setValue("Ny spiller");

    // Navne-felt
    nameField = new TextField();
    nameField.setPromptText("Navn");

    // Fargevelger
    colorPicker = new ColorPicker(Color.WHITE);

    // Når man bytter mellom «Ny spiller» og en lagret spiller
    combo.setOnAction(e -> {
      String val = combo.getValue();
      if ("Ny spiller".equals(val)) {
        nameField.clear();
        nameField.setDisable(false);
      } else {
        nameField.setText(val);
        nameField.setDisable(true);
      }
    });

    getChildren().addAll(lbl, combo, nameField, colorPicker);
  }

  public String getPlayerName() {
    return nameField.getText().trim();
  }

  public Color getColor() {
    return colorPicker.getValue();
  }
}

