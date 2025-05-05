package view.ui;

import controller.PlayerRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.util.PieceImageLoader;

public class PlayerInputRow extends HBox {

  // Navne‐valg
  private final ToggleGroup nameGroup = new ToggleGroup();
  private final RadioButton newRadio     = new RadioButton("New player");
  private final RadioButton savedRadio   = new RadioButton("Saved player");
  private final ComboBox<String> savedCombo;

  // Navne‐felt for ny spiller
  private final TextField nameField      = new TextField();

  // Brikke‐valg
  private final ToggleGroup pieceGroup   = new ToggleGroup();
  private final List<ToggleButton> pieceButtons = new ArrayList<>();

  // “Save new player”-checkbox
  private final CheckBox saveNewCheck    = new CheckBox("Save new player");

  // Preview av brikken
  private final ImageView piecePreview   = new ImageView();

  // Backend‐data + callback
  private final List<PlayerRecord> savedRecords;
  private Consumer<Void> onPieceChanged;

  /**
   * @param label         “Player 1”, “Player 2” osv.
   * @param savedPlayers  navn fra CSV
   * @param pieceOptions  hvilke brikker som finnes
   * @param savedRecords  PlayerRecord‐objekter fra CSV
   */
  public PlayerInputRow(String label,
      List<String> savedPlayers,
      List<String> pieceOptions,
      List<PlayerRecord> savedRecords) {
    super(10);
    this.savedRecords = savedRecords;
    setAlignment(Pos.CENTER);
    this.setSpacing(10);
    this.getStyleClass().add("player-row");

    // Label “Player X”
    Label lbl = new Label(label);
    lbl.getStyleClass().add("player-label");

    // --- Navne‐velger ---
    newRadio.setToggleGroup(nameGroup);
    savedRadio.setToggleGroup(nameGroup);
    newRadio.setSelected(true);

    savedCombo = new ComboBox<>(FXCollections.observableArrayList(savedPlayers));
    // default disabled til “Saved player” velges
    savedCombo.setDisable(true);

    // TextField for ny spiller
    nameField.setPromptText("Name");
    nameField.getStyleClass().add("player-input-name-playerview");

    // Lytt på endring av radioknapper
    nameGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
      boolean isSaved = newT == savedRadio;
      savedCombo.setDisable(!isSaved);
      nameField.setDisable(isSaved);
      saveNewCheck.setDisable(isSaved);
      if (isSaved) {
        // hvis vi har valgt en eksisterende spiller, fyll inn nameField og brikke
        String sel = savedCombo.getValue();
        if (sel != null) {
          nameField.setText(sel);
          savedRecords.stream()
              .filter(r -> r.name.equals(sel))
              .findFirst()
              .ifPresent(r -> setSelectedPiece(r.piece));
        }
      } else {
        // ny spiller → rydd navn/brikke
        nameField.clear();
        pieceGroup.selectToggle(null);
        updatePreview(null);
      }
      notifyPieceChanged();
    });

    // når brukeren plukker et navn fra savedCombo:
    savedCombo.setOnAction(e -> {
      if (savedRadio.isSelected()) {
        String sel = savedCombo.getValue();
        if (sel != null) {
          nameField.setText(sel);
          savedRecords.stream()
              .filter(r -> r.name.equals(sel))
              .findFirst()
              .ifPresent(r -> setSelectedPiece(r.piece));
          notifyPieceChanged();
          updatePreview(getSelectedPiece());
        }
      }
    });

    saveNewCheck.getStyleClass().add("check-box-player");

    HBox nameBox = new HBox(5, newRadio, savedRadio, savedCombo, nameField, saveNewCheck);

    // --- Brikke‐velger med ToggleButtons ---
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

    // Preview‐bildet
    piecePreview.setFitWidth(40);
    piecePreview.setFitHeight(40);
    piecePreview.setPreserveRatio(true);

    // Legg alt inn i raden
    getChildren().addAll(lbl, nameBox, pieceBox, piecePreview);

    this.getStyleClass().add("player-input-row");
    nameBox.getStyleClass().add("name-box");
    pieceBox.getStyleClass().add("piece-box");
  }

  private void notifyPieceChanged() {
    if (onPieceChanged != null) onPieceChanged.accept(null);
  }

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

  /** Hent navnet brukeren valgte (enten fra combo eller fra tekstfelt) */
  public String getPlayerName() {
    return nameField.getText().trim();
  }

  /** Hent brikken */
  public String getSelectedPiece() {
    Toggle sel = pieceGroup.getSelectedToggle();
    return sel == null ? null : (String) sel.getUserData();
  }

  /**
   * Aktiver/deaktiver brikke‐knapper basert på hva som er ledig.
   * Hvis en tidligere valgt brikke nå er utilgjengelig, rydd valget.
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
    // Gjenopprett eventuelt gjeldende valg hvis det fortsatt er ok
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

  /** Forhåndsvelg en bestemt brikke (f.eks. fra CSV) */
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

  /** Skal vi lagre denne som ny spiller? */
  public boolean shouldSaveNewPlayer() {
    return newRadio.isSelected() && saveNewCheck.isSelected();
  }
}
