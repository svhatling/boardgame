package model.entity;

import java.util.List;

/**
 * Represents a question in the game.
 * Each question has an ID, a question text, a list of options, and the correct answer.
 */
public class Questions {
  private int tileId;
  private String question;
  private List<String> options;
  private String answer;

  /**
   * Getter for the tile ID.
   */
  public int getTileId() {
    return tileId;
  }

  /**
   * Getter for the question text.
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Getter for the list of options.
   */
  public List<String> getOptions() {
    return options;
  }

  /**
   * Getter for the correct answer.
   */
  public String getAnswer() {
    return answer;
  }

}
