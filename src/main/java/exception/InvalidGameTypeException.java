package exception;

public class InvalidGameTypeException extends RuntimeException {
  public InvalidGameTypeException(String message) {
    super(message);
  }
}
