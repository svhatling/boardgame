package view;

/**
 * Observer interface for board game.
 */
public interface BoardGameObserver {

  default void onBoardReady() { }
}