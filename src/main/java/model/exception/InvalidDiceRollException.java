package model.exception;

public class InvalidDiceRollException extends RuntimeException {
  public InvalidDiceRollException(String message) {
    super(message);
  }
}
