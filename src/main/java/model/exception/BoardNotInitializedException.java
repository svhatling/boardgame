package model.exception;

/**
 * Exception thrown when the game board is not initialized.
 */
public class BoardNotInitializedException extends RuntimeException {
  public BoardNotInitializedException(String message) {
    super(message);
  }
}
