package entity;

import exception.InvalidDiceRollException;
import java.util.ArrayList;
import java.util.List;

public class Dice extends Die {

  private List<Die> dice;
  private int sum;

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

  public int rollDice() {
    sum = 0;
    for (Die die : dice) {
      sum += die.rollDie();
    }
    return sum;
  }

  public int getSum() {
    return sum;
  }

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

  @Override
  public void setSides(int sides) {
    super.setSides(sides);
    for (Die die : dice) {
      die.setSides(sides);
    }
  }
}
