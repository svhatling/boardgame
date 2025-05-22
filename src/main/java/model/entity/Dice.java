package model.entity;

import java.util.ArrayList;
import java.util.List;
import model.exception.InvalidDiceRollException;

/**
 * The Dice class represents a collection of dice. It allows rolling multiple dice and keeps track
 * of their values.
 */
public class Dice extends Die {

  private final List<Die> dice;
  private int sum;

  /**
   * Constructs dice objects with the specified number of dice and sides.
   *
   * @param numberOfDice the number of dice to create
   * @param sides the number of sides on each die
   * @throws InvalidDiceRollException if the number of dice is less than or equal to 0
   */
  public Dice(int numberOfDice, int sides) {
    super(sides);
    if (numberOfDice <= 0) {
      throw new InvalidDiceRollException("Number of dice must be greater than 0");
    }
    dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die(sides));
    }
    this.sum = 0;
  }

  /**
   * Rolls the dice.
   *
   * @return the sum of the values from the rolled dice
   */
  public int rollDice() {
    sum = 0;
    for (Die die : dice) {
      sum += die.rollDie();
    }
    return sum;
  }

  /**
   * Getter for the sum of the dice
   */
  public int getSum() {
    return sum;
  }

  /**
   * Getter for the values of the dice
   */
  public List<Integer> getDiceValues() {
    List<Integer> values = new ArrayList<>();
    for (Die die : dice) {
      if (die.getValue() == 0) {
        throw new InvalidDiceRollException("Dice has not been rolled");
      }
      values.add(die.getValue());
    }
    return values;
  }

  /**
   * Setter for the number of sides on each die.
   *
   * @param sides the number of sides to set
   */
  @Override
  public void setSides(int sides) {
    super.setSides(sides);
    for (Die die : dice) {
      die.setSides(sides);
    }
  }
}
