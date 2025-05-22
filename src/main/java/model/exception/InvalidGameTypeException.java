package model.exception;

/**
 * Exception thrown when the game type is invalid.
 */
public class InvalidGameTypeException extends RuntimeException {
  public InvalidGameTypeException(String message) {
    super(message);
  }
}
