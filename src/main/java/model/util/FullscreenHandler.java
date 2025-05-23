package model.util;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Handles fullscreen mode for the application.
 * This class manages the transition to and from fullscreen mode,
 * preserving the window's position and size.
 */
public class FullscreenHandler {

  private final Stage stage;
  private double savedX, savedY, savedWidth, savedHeight;
  private boolean isSetup = false;

  public FullscreenHandler(Stage stage) {
    this.stage = stage;
  }

  /**
   * Sets up fullscreen handling for the given node. Should be called once when the view is
   * initialized.
   *
   * @param rootNode the root node of the scene to set up fullscreen handling for
   */
  public void setupFullscreenHandling(Node rootNode) {
    if (isSetup) {
      return;
    }

    // Listens to the fullscreen changes
    stage.fullScreenProperty().addListener((obs, wasFullScreen, isNowFullScreen) -> {
      if (!isNowFullScreen && wasFullScreen) {
        Platform.runLater(() -> {
          stage.setX(savedX);
          stage.setY(savedY);
          stage.setWidth(savedWidth);
          stage.setHeight(savedHeight);
        });
      }
    });

    // Adds F11 keyboard shortcut
    rootNode.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.F11) {
        toggleFullscreen();
      }
    });

    // Makes sure the node can receive key events
    rootNode.setFocusTraversable(true);
    Platform.runLater(rootNode::requestFocus);

    isSetup = true;
  }

  /**
   * Toggles fullscreen mode while preserving the window position.
   */
  public void toggleFullscreen() {
    if (!stage.isFullScreen()) {
      // Saves current position before entering fullscreen
      savedX = stage.getX();
      savedY = stage.getY();
      savedWidth = stage.getWidth();
      savedHeight = stage.getHeight();

      stage.setFullScreen(true);
    } else {
      stage.setFullScreen(false);
      // Position is restored automatically by the listener
    }
  }
}

