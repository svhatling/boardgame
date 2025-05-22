package model.util;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

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
   */
  public void setupFullscreenHandling(Node rootNode) {
    if (isSetup) {
      return;
    }

    // Listen to fullscreen changes
    stage.fullScreenProperty().addListener((obs, wasFullScreen, isNowFullScreen) -> {
      if (!isNowFullScreen && wasFullScreen) {
        // When exiting fullscreen, restore position
        Platform.runLater(() -> {
          stage.setX(savedX);
          stage.setY(savedY);
          stage.setWidth(savedWidth);
          stage.setHeight(savedHeight);
        });
      }
    });

    // Add F11 keyboard shortcut
    rootNode.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.F11) {
        toggleFullscreen();
      }
    });

    // Make sure the node can receive key events
    rootNode.setFocusTraversable(true);
    Platform.runLater(() -> rootNode.requestFocus());

    isSetup = true;
  }

  /**
   * Toggles fullscreen mode while preserving window position.
   */
  public void toggleFullscreen() {
    if (!stage.isFullScreen()) {
      // Save current position before entering fullscreen
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

  /**
   * Enters fullscreen mode.
   */
  public void enterFullscreen() {
    if (!stage.isFullScreen()) {
      savedX = stage.getX();
      savedY = stage.getY();
      savedWidth = stage.getWidth();
      savedHeight = stage.getHeight();
      stage.setFullScreen(true);
    }
  }

  /**
   * Exits fullscreen mode.
   */
  public void exitFullscreen() {
    if (stage.isFullScreen()) {
      stage.setFullScreen(false);
    }
  }

  /**
   * Returns whether the stage is currently in fullscreen mode.
   */
  public boolean isFullScreen() {
    return stage.isFullScreen();
  }
}

