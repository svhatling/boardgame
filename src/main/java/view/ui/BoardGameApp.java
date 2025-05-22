package view.ui;


import javafx.application.Application;

/**
 * Main class for the board game.
 * <p>
 * Shows a menu for choosing a game variant, and user action tasks are given to the relevant view
 * classes.
 * </p>
 */
public class BoardGameApp{
  /**
   * Main method to launch the application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    Application.launch(MainView.class, args);
  }
}
