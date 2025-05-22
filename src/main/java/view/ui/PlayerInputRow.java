package view.ui;

import controller.PlayerRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.util.PieceImageLoader;

/**
 * A row in the player input form.
 */
public class PlayerInputRow extends HBox {

  private final RadioButton newRadio     = new RadioButton("New player");
  private final RadioButton savedRadio   = new RadioButton("Saved player");
  private final ComboBox<String> savedCombo;

  private final TextField nameField      = new TextField();

  private final ToggleGroup pieceGroup   = new ToggleGroup();
  private final List<ToggleButton> pieceButtons = new ArrayList<>();

  private final CheckBox saveNewCheck    = new CheckBox("Save new player");

  private final ImageView piecePreview   = new ImageView();

  private Consumer<Void> onPieceChanged;

  /**
   * Constructor for PlayerInputRow.
   * @param label         “Player 1,” “Player 2” and so on.
   * @param savedPlayers  name of the players from the CSV file.
   * @param pieceOptions  the pieces to choose from.
   * @param savedRecords  PlayerRecord‐objects for all players in the CSV file.
   */
  public PlayerInputRow(String label,
      List<String> savedPlayers,
      List<String> pieceOptions,
      List<PlayerRecord> savedRecords) {
    super(10);
    setAlignment(Pos.CENTER);
    this.setSpacing(10);
    this.getStyleClass().add("player-row");

    Label lbl = new Label(label);
    lbl.getStyleClass().add("player-label");

    ToggleGroup nameGroup = new ToggleGroup();
    newRadio.setToggleGroup(nameGroup);
    savedRadio.setToggleGroup(nameGroup);
    newRadio.setSelected(true);

    savedCombo = new ComboBox<>(FXCollections.observableArrayList(savedPlayers));

    savedCombo.setDisable(true);

    nameField.setPromptText("Name");
    nameField.getStyleClass().add("player-input-name-playerview");

    nameGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
      boolean isSaved = newT == savedRadio;
      savedCombo.setDisable(!isSaved);
      nameField.setDisable(isSaved);
      saveNewCheck.setDisable(isSaved);
      if (isSaved) {
        String sel = savedCombo.getValue();
        if (sel != null) {
          nameField.setText(sel);
          savedRecords.stream()
              .filter(r -> r.name().equals(sel))
              .findFirst()
              .ifPresent(r -> setSelectedPiece(r.piece()));
        }
      } else {
        nameField.clear();
        pieceGroup.selectToggle(null);
        updatePreview(null);
      }
      notifyPieceChanged();
    });

    savedCombo.setOnAction(e -> {
      if (savedRadio.isSelected()) {
        String sel = savedCombo.getValue();
        if (sel != null) {
          nameField.setText(sel);
          savedRecords.stream()
              .filter(r -> r.name().equals(sel))
              .findFirst()
              .ifPresent(r -> setSelectedPiece(r.piece()));
          notifyPieceChanged();
          updatePreview(getSelectedPiece());
        }
      }
    });

    saveNewCheck.getStyleClass().add("check-box-player");

    HBox nameBox = new HBox(5, newRadio, savedRadio, savedCombo, nameField, saveNewCheck);
    HBox pieceBox = new HBox(5);
    for (String piece : pieceOptions) {
      ToggleButton btn = new ToggleButton(piece);
      btn.setUserData(piece);
      btn.setToggleGroup(pieceGroup);
      btn.setOnAction(e -> {
        notifyPieceChanged();
        updatePreview(getSelectedPiece());
      });
      pieceButtons.add(btn);
      pieceBox.getChildren().add(btn);
    }

    piecePreview.setFitWidth(40);
    piecePreview.setFitHeight(40);
    piecePreview.setPreserveRatio(true);

    getChildren().addAll(lbl, nameBox, pieceBox, piecePreview);

    this.getStyleClass().add("player-input-row");
    nameBox.getStyleClass().add("name-box");
    pieceBox.getStyleClass().add("piece-box");
  }

  private void notifyPieceChanged() {
    if (onPieceChanged != null) onPieceChanged.accept(null);
  }

  /**
   * Setter for the onPieceChanged callback.
   * @param cb the callback to be called when the piece changes
   */
  public void setOnPieceChanged(Consumer<Void> cb) {
    this.onPieceChanged = cb;
  }

  private void updatePreview(String piece) {
    if (piece != null) {
      piecePreview.setImage(PieceImageLoader.get(piece));
    } else {
      piecePreview.setImage(null);
    }
  }

  /** Getter for the player name */
  public String getPlayerName() {
    return nameField.getText().trim();
  }

  /** Getter for the selected piece */
  public String getSelectedPiece() {
    Toggle sel = pieceGroup.getSelectedToggle();
    return sel == null ? null : (String) sel.getUserData();
  }

  /**
   * Setter for the piece options.
   * @param options the list of piece options to set
   */
  public void setPieceOptions(List<String> options) {
    String current = getSelectedPiece();
    for (ToggleButton btn : pieceButtons) {
      String piece = (String) btn.getUserData();
      boolean ok = options.contains(piece);
      btn.setDisable(!ok);
      if (!ok && btn.isSelected()) {
        pieceGroup.selectToggle(null);
        updatePreview(null);
      }
    }
    if (current != null && options.contains(current)) {
      pieceButtons.stream()
          .filter(b -> Objects.equals(b.getUserData(), current))
          .findFirst()
          .ifPresent(b -> {
            pieceGroup.selectToggle(b);
            updatePreview(current);
          });
    }
  }

  /** Setter for the selected piece.*/
  public void setSelectedPiece(String piece) {
    for (ToggleButton btn : pieceButtons) {
      if (Objects.equals(btn.getUserData(), piece)) {
        btn.setSelected(true);
        updatePreview(piece);
        return;
      }
    }
    pieceGroup.selectToggle(null);
    updatePreview(null);
  }

  /** @return true if the player should be saved to the CSV file. */
  public boolean shouldSaveNewPlayer() {
    return newRadio.isSelected() && saveNewCheck.isSelected();
  }
}
