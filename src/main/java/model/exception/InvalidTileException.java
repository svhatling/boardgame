package model.exception;

/**
 * Exception thrown when the tile is invalid.
 */
public class InvalidTileException extends RuntimeException {
  public InvalidTileException(String message) {
    super(message);
  }
}
