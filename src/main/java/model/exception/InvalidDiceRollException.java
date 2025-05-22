package model.exception;

/**
 * Exception thrown when the dice roll is invalid.
 */
public class InvalidDiceRollException extends RuntimeException {
  public InvalidDiceRollException(String message) {
    super(message);
  }
}
