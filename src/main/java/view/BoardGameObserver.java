package view;

/**
 * Observer interface for board game.
 */
public interface BoardGameObserver {
  /**
   * Called when the user has chosen a number of players.
   *
   * @param count the number of players selected by the user
   */
  void onPlayerCountChosen(int count);
}