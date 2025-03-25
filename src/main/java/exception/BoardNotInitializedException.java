package exception;

public class BoardNotInitializedException extends RuntimeException {
  public BoardNotInitializedException(String message) {
    super(message);
  }
}
