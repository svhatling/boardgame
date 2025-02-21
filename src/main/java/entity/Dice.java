package entity;

import java.util.ArrayList;
import java.util.List;

public class Dice extends Die {

  private List<Die> dice;
  private int sum;

  public Dice(int numberOfDice, int sides) {
    super(sides);
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
